package com.yinghai.twentyfour.backstage.interceptor;

import com.yinghai.twentyfour.common.constant.Constant;
import com.yinghai.twentyfour.common.util.ResponseVo;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
//            WebUtils.issueRedirect(request, response, LOGIN_URL);
        }
        if ("XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"))) {// ajax请求
            ResponseVo.send999Code((HttpServletResponse) response,"\u5F53\u524D\u7528\u6237\u6CA1\u6709\u767B\u5F55\uFF01");
            return false;
        }
        //保存Request和Response 到登录后的链接
        saveRequestAndRedirectToLogin(request, response);
        return false;
    }
}
