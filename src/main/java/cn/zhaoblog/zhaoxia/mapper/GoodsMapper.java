package cn.zhaoblog.zhaoxia.mapper;

import cn.zhaoblog.zhaoxia.entity.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author qingzhou
 */
public interface GoodsMapper {

    /**
     * 根据条件计算结果数量
     * @param params
     * @return
     */
    int selectCountByCondition(Map<String, Object> params);

    /**
     * 条件查询
     * @param params
     * @return
     */
    List<Goods> selectByCondition(Map<String, Object> params);

    /**
     * 新增
     * @param goods
     */
    void insert(Goods goods);

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    Goods selectById(Long id);

    /**
     * 人气商品
     * @return
     */
    List<Goods> selectHot();

    /**
     * 首页商品
     * @return
     */
    List<Goods> selectHome();

    /**
     * 添加查看次数
     * @param id
     */
    void addViewCount(Long id);

    /**
     * 设置上架下架
     * @param id
     * @param active
     */
    void updateActive(@Param("id") Long id, @Param("active") Integer active);

    /**
     * 删除商品
     * @param id
     */
    void deleteById(Long id);

    /**
     * 更新属性
     */
    void update(Goods goods);

    /**
     * 添加销售数量
     */
    void addSaleCount(@Param("id") Long id, @Param("count") Integer count);
}
