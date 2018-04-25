package cn.zhaoblog.zhaoxia.mapper;

import cn.zhaoblog.zhaoxia.entity.GoodsSpecDet;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author qingzhou
 */
public interface GoodsSpecDetMapper {

    /**
     * 插入商品规格
     * @param spec
     */
    void insert(GoodsSpecDet spec);

    /**
     * 根据商品ID查询所有规格
     * @param goodsId
     * @return
     */
    List<GoodsSpecDet> selectByGoodsId(long goodsId);

    /**
     * 根据ID查询
     * @param specId
     * @return
     */
    GoodsSpecDet selectById(Long specId);

    /**
     * 删除商品
     */
    void deleteByGoodsId(@Param("goodsId") Long goodsId);

    /**
     * 删除除了保留之外的规格
     */
    void deleteExceptKeeped(@Param("goodsId") long id, @Param("keepedSpecs") List<Long> keepedSpecs);

    /**
     * 更新规格
     */
    void update(GoodsSpecDet spec);

    /**
     * 更新库存
     */
    void updateLeftCount(@Param("id") Long id, @Param("leftCount") int leftCount);

    /**
     * 增加库存
     */
    void addLeftCount(@Param("id") Long id, @Param("count") int count);

    /**
     * 更新价格和剩余数量
     */
    int updatePriceAndLeftCount(@Param("barCode") String barCode, @Param("price") BigDecimal price, @Param("leftCount") Integer leftCount);
}
