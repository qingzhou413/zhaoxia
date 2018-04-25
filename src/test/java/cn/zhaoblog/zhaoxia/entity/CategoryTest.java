package cn.zhaoblog.zhaoxia.entity;

import cn.zhaoblog.zhaoxia.biz.CategoryBiz;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.List;

/**
 * Created by 16204 on 2017/10/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-context.xml")
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
public class CategoryTest {

    @Autowired
    private CategoryBiz biz;

    @Test
    public void listRoot() {
        List<Category> list = biz.findRoots();
        System.out.println(list);
    }

    @Test
    public void findSub(){
        List<Category> list = biz.findByParentId(6);
        System.out.println(list);
    }

    @Test
    public void saveRoot() {
        Category cat = new Category();
        cat.setName("蔬菜");
        cat.setParentId(-1);
        biz.save(cat);
        System.out.println(cat.getId());
    }

    @Test
    public void saveSub(){
        Category cat = new Category();
        cat.setName("苹果");
        cat.setParentId(6);
        biz.save(cat);
    }
}