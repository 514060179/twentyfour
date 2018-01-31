package com.yinghai.twentyfour.backstage.controller;

import com.yinghai.twentyfour.common.model.VersionControl;
import com.yinghai.twentyfour.common.service.VersionControllerService;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @Autowired
    private VersionControllerService versionControllerService;

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
