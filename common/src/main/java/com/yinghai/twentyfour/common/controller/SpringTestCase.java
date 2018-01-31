package com.yinghai.twentyfour.common.controller;

import com.yinghai.twentyfour.common.model.VersionControl;
import com.yinghai.twentyfour.common.service.VersionControllerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runner.RunWith;
/**
 * Created by Administrator on 2017/7/20.
 */
//指定bean注入的配置文件
@ContextConfiguration(locations = { "file:src/webapp/WEB-INF/spring-mvc.xml" })
//使用标准的JUnit @RunWith注释来告诉JUnit使用Spring TestRunner
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringTestCase extends AbstractJUnit4SpringContextTests {
    @Autowired
    private VersionControllerService versionControllerService;
    @Test
    public void test(){
        versionControllerService.findByCondition(new VersionControl());
    }
}
