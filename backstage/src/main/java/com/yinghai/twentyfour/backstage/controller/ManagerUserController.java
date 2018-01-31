package com.yinghai.twentyfour.backstage.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yinghai.twentyfour.backstage.model.ManagerUser;
import com.yinghai.twentyfour.backstage.model.Menu;
import com.yinghai.twentyfour.backstage.service.ManagerUserService;
import com.yinghai.twentyfour.backstage.service.MenuService;
import com.yinghai.twentyfour.common.util.EncryptUtil;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.ResponseUtils;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/7/24.
 */
@RequestMapping("/admin/user")
@Controller
public class ManagerUserController {
    @Autowired
    private ManagerUserService managerUserService;
    @Autowired
    private MenuService menuService;
    private Logger log = Logger.getLogger(this.getClass());
    @RequestMapping(value = "find", method = RequestMethod.GET)
    public void test(HttpServletRequest request, HttpServletResponse response){

        System.out.println(managerUserService.findOneById(1));

    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String test1(HttpServletRequest request, HttpServletResponse response, ModelMap model){

        ManagerUser user = managerUserService.findOneById(1);
        Subject user1 = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("simon", "111");
        if (!user1.isAuthenticated()){
            //使用shiro来验证
            token.setRememberMe(true);
            user1.login(token);//验证角色和权限
        }
        ServletContext application = request.getSession().getServletContext();
        List<Menu> list = menuService.findAll();
        application.setAttribute("menuList",list);
        model.addAttribute("page",list);
//        model.addAttribute("query",map);
//        model.addAttribute("driverList",driverList);
        model.addAttribute("pageNo",1);
        model.addAttribute("pageSize",10);
        model.addAttribute("recordCount",55);
        model.addAttribute("pageCount",5);
//        ResponseUtils.renderJson(response,"login succes!");
        return "test/test";
    }
    @RequestMapping(value = "login2", method = RequestMethod.GET)
    public String test2(HttpServletRequest request, HttpServletResponse response){

        return "test/test1";
    }
    @RequestMapping(value = "login3", method = RequestMethod.GET)
    public void test3(HttpServletRequest request, HttpServletResponse response){//http://119.28.15.112:8083/images/test.mp3
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Methods", "POST,GET");
            response.setDateHeader("Expires", 0);
            response.sendRedirect("http://119.28.15.112:8083/images/test.mp3");
//            request.setCharacterEncoding("utf-8");
//            try {
//                request.getRequestDispatcher("http://119.28.15.112:8083/images/test.mp3") .forward(request,response);
//            } catch (ServletException e) {
//                e.printStackTrace();
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @RequestMapping("/list")
	public String list(HttpServletRequest request, ModelMap model) throws ParseException {
	      try {
	            request.setCharacterEncoding("utf-8");
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }
			//页码
			Integer pageNo = TransformUtils.toInt(request.getParameter("page"));//页数
			Integer pageStartSize = TransformUtils.toInt(request.getParameter("pageStartSize"));//每页存在的条数
			pageNo = pageNo == 0 ? 1 : pageNo;
			pageStartSize = pageStartSize == 0 ? 10 : pageStartSize;

	        ManagerUser query = new ManagerUser();
	      //用户名
			String username = request.getParameter("username");
			if(StringUtil.notEmpty(username)){
				query.setUsername("%"+username.trim()+"%");
			}
			//邮箱
			String email = request.getParameter("email");
			if(StringUtil.notEmpty(email)){
				query.setEmail("%"+email.trim()+"%");
			}
			Page<ManagerUser> page = managerUserService.findListPage(pageNo, pageStartSize, query);
			   	model.addAttribute("page", page);
			   	model.addAttribute("pageNo", page.getPageNum());
		        //查詢條件數據，模糊查詢的需要去掉第一位與最後一位字節
		        if(query.getUsername()!=null&&query.getUsername().startsWith("%")){
		        	query.setUsername(username.trim());
		        }
		        if(query.getEmail()!=null&&query.getEmail().startsWith("%")){
		        	query.setEmail(email.trim());
		        }
			   	model.addAttribute("managerUser", query);
		        model.addAttribute("pageSize", page.getPageSize());
		        model.addAttribute("recordCount", page.getTotal());
		        model.addAttribute("pageCount", page.getPages());
		return "managerUser/list";
	}
	 /**
     * 跳转到新增页面，判断是否是新增还是修改
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/edit")
    public String editOrSave(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
        log.debug("======edit or new one manager======");
        try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //String act = request.getParameter("act");
        String result = "managerUser/edit";
        String id = request.getParameter("id");
        if(StringUtil.notEmpty(id)){
            if(!StringUtil.empty(id)){
                model.addAttribute("managerUser",managerUserService.selectByPrimaryKey((Integer.valueOf(id))));
            }else{
                log.error("======edit one adv.id can not null======");
                model.addAttribute("msg","id can not be null");
                return "500";
            }
        }
        return result;
    }
	 /**
     * 處理新增修改
     * @param request
     * @param model
     * @return
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws IllegalStateException 
     */
    @RequestMapping("/save")
    public void save(HttpServletRequest request, HttpServletResponse response,ModelMap model) throws ParseException, IllegalStateException, IOException {
    	 log.debug("======新增或修改數據======");
         try {
 			request.setCharacterEncoding("utf-8");
 		} catch (UnsupportedEncodingException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
         ManagerUser user = new ManagerUser();
         String id = request.getParameter("id");
         String act="add";
         if(StringUtil.notEmpty(id)){
        	 user.setId(TransformUtils.toInt(id));
        	 act="upd";
         }
     	String username = request.getParameter("username") ;//用户名
     	if(StringUtil.notEmpty(username)){
     		user.setUsername(username);
     	}
     
    	String password = request.getParameter("password") ;//密码
    	if(StringUtil.notEmpty(password)){
    		user.setPassword(new SimpleHash("md5", password.toCharArray(), ByteSource.Util.bytes(username), 2).toString());
     	}
    	String email = request.getParameter("email") ;//是否使用
		ManagerUser user1= managerUserService.findByEmail(email);
		if(StringUtil.notEmpty(email)){
    		user.setEmail(email);
			if(user1!=null&&StringUtil.notEmpty(id)&&user1.getId()!=TransformUtils.toInt(id)){//在这3种情况下时避免更新操作出现邮箱无变化然而更新失败的问题
				ResponseVo.send101Code(response, "该邮箱已存在，请重新输入邮箱");
				return;
			}
     	}
    	user.setLastUpdated(new Date());
    	if("add".equals(act)){//新增
    		// 判断该用户名是否存在,新增时才需要判断
			user1= managerUserService.findByName(username);
         	if(user1!=null){
         		ResponseVo.send101Code(response, "该用户已存在，请重新输入用户名称");
				return;
         	}
    		user.setCreateTime(new Date());
    		int i = managerUserService.saveManagerUser(user);
    		if(i<1){//i<1说明该数据新增失败
    			ResponseVo.send108Code(response, "创建用户失败，数据库新增出错");
				return;
    		}else{
    			ResponseVo.common("1", "create success", new JSONObject(), response);
				return;
    		}
    	}else{//修改
    		user.setLastUpdated(new Date());
   		 int i =  managerUserService.updateManagerUser(user);
   		 if(i>0){
   			ResponseVo.common("2", "update success", new JSONObject(), response);
   			return;
            }
    	}
    }
    /**
     * 删除功能
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping("/del")
	public void del(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		log.debug("======删除管理员!======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String id = request.getParameter("id");
		int i = managerUserService.deleteManagerUser((Integer.valueOf(id)));
		if (i <= 0) {
			ResponseVo.send120Code(response, "delete fail");
		} else {
			ResponseVo.common("1", "create success", new JSONObject(), response);
		}
	}
}
