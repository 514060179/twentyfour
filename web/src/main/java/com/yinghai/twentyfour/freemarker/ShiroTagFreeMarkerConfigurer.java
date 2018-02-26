package com.yinghai.twentyfour.freemarker;

import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.template.TemplateException;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;

/**
 * Created by Administrator on 2018/2/23.
 */
public class ShiroTagFreeMarkerConfigurer extends FreeMarkerConfigurer {

    @Override
    public void afterPropertiesSet() throws IOException, TemplateException {
        super.afterPropertiesSet();
        this.getConfiguration().setSharedVariable("shiro", new ShiroTags());
        this.getConfiguration().setNumberFormat("#");//防止页面输出数字,变成2,000
        //可以添加很多自己的要传输到页面的[方法、对象、值]
    }
}
