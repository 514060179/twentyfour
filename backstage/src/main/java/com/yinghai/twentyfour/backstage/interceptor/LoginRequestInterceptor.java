package com.yinghai.twentyfour.backstage.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.yinghai.twentyfour.backstage.dao.MenuMapper;
import com.yinghai.twentyfour.backstage.model.ManagerUser;

public class LoginRequestInterceptor implements HandlerInterceptor {
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object arg2) throws Exception {
        HttpSession session = req.getSession(true);
        // 从session 里面获取用户名的信息
        Object obj = session.getAttribute("spcarManager");
        Object menu = session.getAttribute("menu");
        String requestPath = req.getRequestURI();
        String[] lable = requestPath.split("\\/");

        int lableIndex = lable.length - 2;
        String lables = lable[lableIndex];
        ManagerUser managerUser = (ManagerUser)obj;
        boolean jurisdiction = false;
//        if(managerUser!=null){
//            if (managerUser.getRoleId()!=0){
//                Map<String,Object> map = new HashMap<String,Object>();
//                map.put("lable",lables);
//                Menu menu1 = menuMapper.findMenus(map).get(0);
//                jurisdiction = !menu1.getJurisdiction();
//            }else{
//                jurisdiction = true;
//            }
//        }
        // 判断如果没有取到用户信息，就跳转到登陆页面，提示用户进行登陆
        if (obj == null || "".equals(obj.toString()) || menu == null || "".equals(menu.toString())||!jurisdiction) {
            res.sendRedirect(req.getContextPath() + "/managerLogin/tologin");
            return false;
        }
        return true;
    }


}
