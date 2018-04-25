package cn.zhaoblog.zhaoxia.biz;

import cn.zhaoblog.zhaoxia.dto.GoodsDto;
import cn.zhaoblog.zhaoxia.entity.*;
import cn.zhaoblog.zhaoxia.exception.GoodsException;
import cn.zhaoblog.zhaoxia.mapper.GoodsMapper;
import cn.zhaoblog.zhaoxia.mapper.GoodsSpecDetMapper;
import com.joysuch.core.pager.PageBean;
import com.joysuch.core.pager.PageParam;
import com.joysuch.core.util.MapParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 16204 on 2017/10/8.
 */
@Service
public class GoodsBiz {

    @Autowired
    private GoodsMapper mapper;
    @Autowired
    private GoodsSpecDetMapper specMapper;
    @Autowired
    private CategoryBiz catBiz;
    @Autowired
    private BrandBiz brandBiz;
    @Autowired
    private PackStyleBiz packStyleBiz;

    @Transactional(rollbackFor = {Exception.class})
    public void save(Goods goods){
        List<GoodsSpecDet> specDets = goods.getSpecDets();
        if(specDets == null || specDets.size() == 0){
            throw GoodsException.SPEC_DET_CANT_BE_EMPTY;
        }
        mapper.insert(goods);
        long goodsId = goods.getId();
        for(GoodsSpecDet spec : specDets){
            spec.setGoodsId(goodsId);
            specMapper.insert(spec);
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public void edit(Goods goods){
        List<GoodsSpecDet> specDets = goods.getSpecDets();
        if(specDets == null || specDets.size() == 0){
            throw GoodsException.SPEC_DET_CANT_BE_EMPTY;
        }
        mapper.update(goods);
        long goodsId = goods.getId();

        List<Long> keepedSpecs = new ArrayList<>();
        for(GoodsSpecDet spec : specDets){
            if (spec.getId() == 0) {
                spec.setGoodsId(goodsId);
                specMapper.insert(spec);
            } else {
                specMapper.update(spec);
            }
            keepedSpecs.add(spec.getId());
        }
        List<GoodsSpecDet> specs = specMapper.selectByGoodsId(goodsId);
        if(specs.size() > keepedSpecs.size()) {
            specMapper.deleteExceptKeeped(goodsId, keepedSpecs);
        }
    }

    public PageBean<GoodsDto> list(PageParam page, Map<String, Object> params){
        Map<String, Object> paramMap = page.getParamMap();
        paramMap.putAll(params);
        int totalCount = mapper.selectCountByCondition(paramMap);
        List<GoodsDto> data = new ArrayList<>();
        List<Goods> list = mapper.selectByCondition(paramMap);
        if(list != null && list.size() > 0){
            for(Goods goods : list){
                fillSpecDets(goods);
                GoodsDto dto = convDto(goods);
                data.add(dto);
            }
        }
        return new PageBean<>(page.getPageNum(), page.getNumPerPage(), totalCount, data);
    }

    private void fillSpecDets(Goods goods) {
        long goodsId = goods.getId();
        List<GoodsSpecDet> specs = specMapper.selectByGoodsId(goodsId);
        goods.setSpecDets(specs);
    }

    private GoodsDto convDto(Goods goods) {
        long rootCatId = goods.getRootCatId();
        Category rootCat = catBiz.findById(rootCatId);
        long subCatId = goods.getSubCatId();
        Category subCat = catBiz.findById(subCatId);

        String category = rootCat.getName() + "-" + subCat.getName();

        long brandId = goods.getBrandId();
        Brand brd = brandBiz.findById(brandId);
        String brand = brd == null ? "" : brd.getName();

        long packId = goods.getPackId();
        PackStyle packStyle = packStyleBiz.findById(packId);
        String pack = packStyle == null ? "" : packStyle.getName();

        GoodsDto dto = new GoodsDto(goods, category, brand, pack);
        return dto;
    }

    public List<Goods> getBySubCatId(long catId) {
        return mapper.selectByCondition(new MapParam().add("subCatId", catId).add("active", 1).build());
    }

    public Goods findById(Long id) {
        Goods dbItem = mapper.selectById(id);
        if(dbItem != null){
            fillSpecDets(dbItem);
        }
        return dbItem;
    }

    public GoodsSpecDet findGoodsSpecById(Long specId){
        return specMapper.selectById(specId);
    }

    /**
     * 人气商品
     * @return
     */
    public List<Goods> hot() {
        List<Goods> list = mapper.selectHot();
        if(list != null && list.size() > 0){
            for(Goods goods : list){
                fillSpecDets(goods);
            }
        }
        return list;
    }

    /**
     * 首页商品列表
     * @return
     */
    public List<Goods> home() {
        List<Goods> list = mapper.selectHome();
        if(list != null && list.size() > 0){
            for(Goods goods : list){
                fillSpecDets(goods);
            }
        }
        return list;
    }

    @Transactional(rollbackFor = {Exception.class})
    public void addViewCount(Long id) {
        mapper.addViewCount(id);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void setActive(Long id, Integer active) {
        mapper.updateActive(id, active);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteById(Long id) {
        specMapper.deleteByGoodsId(id);
        mapper.deleteById(id);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void addSaleCount(Long goodsId, Integer count) {
        mapper.addSaleCount(goodsId, count);
    }

    @Transactional(rollbackFor = {Exception.class})
    public synchronized void reduceLeftCount(Long specId, Integer count) {
        GoodsSpecDet spec = specMapper.selectById(specId);
        Goods goods = mapper.selectById(spec.getGoodsId());
        if (spec.getLeftCnt() < count) {
            throw new GoodsException(GoodsException.LEFT_COUNT_LIMIT.getCode(), "商品[" + goods.getName() + spec.getSpecName() + "]库存不足了");
        }
        specMapper.updateLeftCount(specId, spec.getLeftCnt() - count);
    }

    @Transactional(rollbackFor = {Exception.class})
    public synchronized void addLeftCount(Long specId, Integer count) {
        specMapper.addLeftCount(specId, count);
    }

    @Transactional(rollbackFor = {Exception.class})
    public synchronized boolean updatePriceAndLeft(String barCode, String price, String leftCount) {
        int c = specMapper.updatePriceAndLeftCount(barCode, new BigDecimal(Double.valueOf(price)), Integer.valueOf(leftCount));
        return c > 0;
    }

    @Transactional(rollbackFor = {Exception.class})
    public synchronized String uploadGoodsExcel(List<GoodsSpecDet> list) {
        StringBuilder buf = new StringBuilder();
        for (GoodsSpecDet spec : list) {
            int c = specMapper.updatePriceAndLeftCount(spec.getBarCode(), spec.getPrice(), spec.getLeftCnt());
            buf.append(spec.getBarCode())
                    .append("    ")
                    .append(c > 0 ? "成功" : "失败")
                    .append("\n");
        }
        return buf.toString();
    }
}
