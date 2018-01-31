package com.yinghai.twentyfour.test;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by Administrator on 2017/7/24.
 */
public class ChangePasswordFilter extends AccessControlFilter {

    /**
     * 登录地址
     */
    static final String LOGIN_URL = "/login.html";
    /**
     * 修改密码地址
     */
    static final String NEW_PASSWORD_URL = "/login/new_password.html";
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if (subject.getPrincipal() == null) {// 表示没有登录，重定向到登录页面
            saveRequest(request);
            WebUtils.issueRedirect(request, response, LOGIN_URL);
        } else {
            String first = "";
            if ("".equals(first)) {
                if (StringUtils.hasText(NEW_PASSWORD_URL)) {// 如果首次登录未修改密码，则跳转到修改密码页面
                    WebUtils.issueRedirect(request, response, NEW_PASSWORD_URL);
                } else {
                    WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }
        return true;
    }
}
