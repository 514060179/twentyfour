package com.yinghai.twentyfour.backstage.controller;

import com.yinghai.twentyfour.common.model.VersionControl;
import com.yinghai.twentyfour.common.service.VersionControllerService;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import jxl.common.Logger;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/7.
 */
@RequestMapping("version")
@Controller
public class BackVersionController {
	private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private VersionControllerService versionControllerService;
    /**
     * 获取版本列表
     * @param request
     * @param response
     */
    @RequestMapping("/list")
    public String getList(HttpServletRequest request,HttpServletResponse response,ModelMap model){
    	log.info("查询版本记录列表");
    	Integer id = TransformUtils.toInt(request.getParameter("id"));
    	VersionControl version = new VersionControl();
    	if(id>0){
    		version.setId(id);
    	}
    	String versionId = request.getParameter("versionId");
    	if(!StringUtil.empty(versionId)){
    		version.setVersionId(versionId);
    	}
    	Integer forceUpdate = TransformUtils.toInteger(request.getParameter("forceUpdate"));
    	if(forceUpdate!=null){
    		if(forceUpdate==1){
    			version.setForceUpdate(true);
    		}else if(forceUpdate==0){
    			version.setForceUpdate(false);
    		}
    	}
    	Integer deviceType = TransformUtils.toInt(request.getParameter("deviceType"));
    	if(deviceType>0){
    		version.setDeviceType(deviceType.toString());
    	}
    	String realm = request.getParameter("realm");
    	if(StringUtil.empty(realm)){
    		if("master".equals(realm)){
    			version.setRealm(realm);
    		}else if("user".equals(realm)){
    			version.setRealm(realm);
    		}
    	}
    	String describe = request.getParameter("describe");
    	if(!StringUtil.empty(describe)){
    		version.setDescribe(describe);
    	}
    	int pageSize = 10;
    	int pageNo = 1;
    	Integer ps = TransformUtils.toInt(request.getParameter("pageSize"));
    	Integer pn = TransformUtils.toInt(request.getParameter("pageNo"));
    	//根据条件查询版本记录
    	Page<VersionControl> page = versionControllerService.queryVersion(pageSize,pageNo,version);
    	JSONObject responseObj = new JSONObject();
    	model.addAttribute("page", page);
    	model.addAttribute("pageNo", page.getPageNum());
    	model.addAttribute("pageSize", page.getPageSize());
    	model.addAttribute("recordCount", page.getTotal());
    	model.addAttribute("pageCount", page.getPages());
    	model.addAttribute("version", version);
    	return "version/list";
    }
    /**
     * 进入版本记录编辑页面
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("/edit")
    public String toEdit(HttpServletRequest request,HttpServletResponse response,ModelMap model){
    	log.info("进入版本记录编辑页面");
    	//根据id查询对应的版本记录
    	Integer id = TransformUtils.toInt(request.getParameter("id"));
    	if(id>0){
    		//查询该版本记录
    		//根据id查询版本记录
    		VersionControl ver = versionControllerService.findById(id);
    		model.addAttribute("version", ver);
    	}
    	return "version/edit";
    }
    /**
     *  编辑或新增版本
     * @param request
     * @param response
     */
    @RequestMapping(value="editOrAdd",method = RequestMethod.POST)
    public void editOrAdd(HttpServletRequest request,HttpServletResponse response){
    	log.info("新增或编辑版本记录");
    	Integer id = TransformUtils.toInt(request.getParameter("id"));
    	String versionId = request.getParameter("versionId");
    	if(StringUtil.empty(versionId)){
    		ResponseVo.send101Code(response, "versionId为空");
    		return;
    	}
    	Boolean forceUpdate = TransformUtils.toBoolean(request.getParameter("forceUpdate"));
    	String deviceType = request.getParameter("deviceType");
    	if(StringUtil.empty(deviceType)){
    		ResponseVo.send101Code(response, "deviceType为空");
    		return;
    	}
    	String realm = request.getParameter("realm");
    	if(StringUtil.empty(realm)){
    		ResponseVo.send101Code(response, "realm为空");
    		return;
    	}
    	String describe = request.getParameter("describe");
    	if(StringUtil.empty(describe)){
    		ResponseVo.send101Code(response, "describe为空");
    		return;
    	}
    	VersionControl version = new VersionControl();
    	version.setVersionId(versionId);
    	version.setForceUpdate(forceUpdate);
    	if("1".equals(deviceType)||"2".equals(deviceType)){
    		version.setDeviceType(deviceType);
    	}else{
    		ResponseVo.send103Code(response, "deviceType错误");
    		return;
    	}
    	if("master".equals(realm)||"user".equals(realm)){
    		version.setRealm(realm);
    	}else{
    		ResponseVo.send103Code(response, "realm错误");
    		return;
    	}
    	version.setDescribe(describe);
    	if(id>0){//编辑
    		version.setId(id);
    		version.setUpdateTime(new Date());
    		int num = versionControllerService.updateVersionController(version);
    		if(num!=1){//数据更新失败
    			ResponseVo.send106Code(response, "数据出错，请稍候重试或咨询客服人员");
    			return;
    		}
    		ResponseVo.send1Code(response, "success", new JSONObject());
    	}else{//新增
    		version.setCreateTime(new Date());
    		int num = versionControllerService.saveVersionController(version);
    		if(num!=1){
    			ResponseVo.send106Code(response, "数据出错，请稍候重试或咨询客服人员");
    			return;
    		}
    		ResponseVo.send1Code(response, "success", new JSONObject());
    	}
    }
    
    @RequestMapping(value = "save", method = RequestMethod.GET)
    public void save(HttpServletRequest request, HttpServletResponse response){
        String deviceType = request.getParameter("deviceType"); //设备类型 1 ios 2android
        VersionControl v = new VersionControl();
        if(StringUtil.empty(deviceType)){
            ResponseVo.send101Code(response,"deviceType is null");
            return;
        }else{
            v.setDeviceType(deviceType);
        }
        String realm = request.getParameter("realm"); //司机端、乘客端
        if(StringUtil.empty(realm)){
            ResponseVo.send101Code(response,"realm is null");
            return;
        }
        v.setRealm(realm);
        String describe = request.getParameter("describe");
        if(!StringUtil.empty(describe)){
            v.setDescribe(describe);
        }
        String versionId = request.getParameter("versionId");
        if(StringUtil.empty(versionId)){
            ResponseVo.send101Code(response,"versionId is null");
            return;
        }
        String forceUpdate = request.getParameter("forceUpdate");
        if(StringUtil.empty(forceUpdate)){
            v.setForceUpdate(false);
        }else{
            v.setForceUpdate(Boolean.parseBoolean(forceUpdate));
        }
        v.setVersionId(versionId);
        v.setCreateTime(new Date());
        int i = versionControllerService.saveVersionController(v);
        if (i>0){
            ResponseVo.send1Code(response,"success",new JSONObject());
        }else{
            ResponseVo.send106Code(response,"新增失败！");
        }
    }
}
