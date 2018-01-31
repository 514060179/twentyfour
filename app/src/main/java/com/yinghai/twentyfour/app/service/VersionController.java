package com.yinghai.twentyfour.app.service;

import com.yinghai.twentyfour.common.model.VersionControl;
import com.yinghai.twentyfour.common.service.VersionControllerService;
import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import com.yinghai.twentyfour.common.util.ResponseVo;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/7.
 */
@Controller
@RequestMapping("app/version")
public class VersionController {

    @Autowired
    private VersionControllerService versionControllerService;
    @RequestMapping("now")
    public void version(HttpServletRequest request, HttpServletResponse response){
        String deviceType = request.getParameter("deviceType"); //设备类型 1 ios 2android
        VersionControl v = new VersionControl();
        if("".equals(deviceType)||deviceType==null){
            ResponseVo.send101Code(response,"deviceType is null");
            return;
        }else{
            v.setDeviceType(deviceType);
        }
        String realm = request.getHeader("realm"); //司机端、乘客端
        if("".equals(realm)||realm==null){
            realm = "master";
        }
        v.setRealm(realm);
        VersionControl versionControl = versionControllerService.findByCondition(v);
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONObject data = JSONObject.fromObject(versionControl, config);
        ResponseVo.send1Code(response,"success",data);
    }
}
