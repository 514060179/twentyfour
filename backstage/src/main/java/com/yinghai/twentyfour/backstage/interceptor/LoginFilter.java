package com.yinghai.twentyfour.backstage.interceptor;

import com.yinghai.twentyfour.common.constant.Constant;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by Administrator on 2018/2/23.
 */
public class LoginFilter extends AccessControlFilter {

    static final String LOGIN_URL = Constant.http+"/twentyfour/";
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        Subject subject = SecurityUtils.getSubject();
        Object token = subject.getPrincipal();

        //获取subject
        if(null!=mappedValue){
            String[] arrea = (String[]) mappedValue;
            for (String s: arrea
                    ) {
                if (subject.isPermitted(s)){
                    return true;
                }
            }
        }

        if(null != token || isLoginRequest(request, response)){// && isEnabled()
            return Boolean.TRUE;
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        Subject subject = getSubject(request, response);
        if (subject.getPrincipal() == null) {//表示没有登录，重定向到登录页面
            saveRequest(request);
            WebUtils.issueRedirect(request, response, LOGIN_URL);
        }
        //保存Request和Response 到登录后的链接
//        saveRequestAndRedirectToLogin(request, response);
        return false;
    }
}
