package com.yinghai.twentyfour.test;

import com.yinghai.twentyfour.common.model.VersionControl;
import com.yinghai.twentyfour.common.service.VersionControllerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Administrator on 2017/7/20.
 */
//指定bean注入的配置文件
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring-mvc.xml" })
//使用标准的JUnit @RunWith注释来告诉JUnit使用Spring TestRunner
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringTestCase extends AbstractJUnit4SpringContextTests {
    @Autowired
    private VersionControllerService versionControllerService;
    @Autowired
    private EhCacheTestService ehCacheTestService;
    @Test
    public void test() throws InterruptedException {
        VersionControl v = new VersionControl();
        v.setId(1);
        System.out.println("第一次调用：" + versionControllerService.findByCondition(v));
        Thread.sleep(2000);
        System.out.println("第二次调用：" + versionControllerService.findByCondition(v));
        VersionControl versionControl = new VersionControl();
        versionControl.setId(1);
        versionControl.setPriority(12);
        versionControllerService.updateVersionController(versionControl);
        Thread.sleep(2000);

        System.out.println("更新之后一：" + versionControllerService.findByCondition(v));
        Thread.sleep(5000);
        System.out.println("再过5秒后：" + versionControllerService.findByCondition(v));
    }
}
