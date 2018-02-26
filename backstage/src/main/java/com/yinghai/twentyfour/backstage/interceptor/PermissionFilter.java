package com.yinghai.twentyfour.backstage.interceptor;

import com.yinghai.twentyfour.common.util.ResponseUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2018/2/23.
 */
public class PermissionFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //获取subject
        Subject subject = getSubject(request,response);
        if(null!=mappedValue){
            String[] arrea = (String[]) mappedValue;
            for (String s: arrea
                    ) {
                if (subject.isPermitted(s)){
                    return true;
                }
            }
        }
        HttpServletRequest httpRequest = ((HttpServletRequest)request);
        String uri = httpRequest.getRequestURI();//获取URI
        String basePath = httpRequest.getContextPath();//获取basePath
        if(null != uri && uri.startsWith(basePath)){
            uri = uri.replaceFirst(basePath, "");
        }
        if(subject.isPermitted(uri)){
            return Boolean.TRUE;
        }
        ResponseUtils.renderJson((HttpServletResponse) response, "没有权限操作！");
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        Subject subject = getSubject(request, response);
        if (subject.getPrincipal() == null) {//表示没有登录，重定向到登录页面
            saveRequest(request);
            PrintWriter printWriter = response.getWriter();
            printWriter.print("没有权限操作！");
            printWriter.flush();
            printWriter.close();
        }
        return false;
    }
}
