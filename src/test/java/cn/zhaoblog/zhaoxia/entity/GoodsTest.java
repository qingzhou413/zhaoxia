package cn.zhaoblog.zhaoxia.entity;

import cn.zhaoblog.zhaoxia.biz.GoodsBiz;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 16204 on 2017/10/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-context.xml")
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
public class GoodsTest {

    @Autowired
    private GoodsBiz biz;

    @Test
    public void newGoods(){
        Goods g = new Goods();
        g.setName("红富士苹果");
        g.setRootCatId(6);
        g.setSubCatId(8);
        g.setBrandId(1);
        g.setDesc("精品红富士苹果，不甜不要钱");
        g.setDetImgsStr("imgs/hongfushi_pingguo_1.jpg,imgs/hongfushi_pingguo_2.jpg");
        g.setHeadImg("imgs/hongfushi_pingguo_0.jpg");
        g.setPackId(1);
        g.setSaleCount(10);
        List<GoodsSpecDet> specs = new ArrayList<>();
        GoodsSpecDet spec1 = new GoodsSpecDet();
        spec1.setPrice(new BigDecimal("6"));
        spec1.setSpecName("500克");
        specs.add(spec1);
        GoodsSpecDet spec2 = new GoodsSpecDet();
        spec2.setPrice(new BigDecimal("10"));
        spec2.setSpecName("1千克");
        specs.add(spec2);
        g.setSpecDets(specs);
        biz.save(g);
    }
}
