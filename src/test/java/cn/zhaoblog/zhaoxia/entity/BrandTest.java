package cn.zhaoblog.zhaoxia.entity;

import cn.zhaoblog.zhaoxia.biz.BrandBiz;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Created by 16204 on 2017/10/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-context.xml")
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
public class BrandTest {

    @Autowired
    private BrandBiz biz;

    @Test
    public void newBrand(){
        Brand bran = new Brand();
        bran.setName("红富士");
        biz.save(bran);
    }

}
