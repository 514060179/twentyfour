package com.yinghai.twentyfour.app.interceptor;

import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.service.TfMasterService;
import com.yinghai.twentyfour.common.service.TfUserService;
import com.yinghai.twentyfour.common.util.ResponseUtils;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/5/8.
 */
public class AccessTokenInterceptor implements HandlerInterceptor {

    Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TfMasterService tfMasterService;
    @Autowired
    private TfUserService tfUserService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        JSONObject responseObject = new JSONObject();
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String realm = request.getHeader("Realm");
        if (StringUtil.empty(realm)){
            ResponseVo.send101Code(response,"缺少Realm参数！");
            return false;
        }
        String masterId = request.getParameter("masterId");
        String userId = request.getParameter("userId");
        String deviceId = "";
        switch (realm){
            case "master":
                if (StringUtil.empty(masterId)){
                    ResponseVo.send101Code(response,"缺少masterId参数！");
                    return false;
                }
                TfMaster tfMaster = tfMasterService.selectByPrimaryKey(Integer.parseInt(masterId));
                if (tfMaster==null){
                    try {
                        if(tfMaster==null){
                            ResponseVo.send102Code(response,"该大师id不存在！id="+masterId);
                            log.error("该大师id不存在！id="+masterId);
                            return false;
                        }
                    } catch (Exception e) {
                        log.error(e);
                        ResponseVo.send101Code(response,"masterId格式有误！");
                        e.printStackTrace();
                        return false;
                    }
                }
                //单点登录
                deviceId = tfMaster.getmDeviceId();

                break;
            case "user":
                if (StringUtil.empty(userId)){
                    ResponseVo.send101Code(response,"缺少userId参数！");
                    return false;
                }
                if(TransformUtils.toInt(userId)!=-1){//游客的ID为-1
	                TfUser tfUser = tfUserService.selectByPrimaryKey(Integer.parseInt(userId));
	                if(tfUser==null){
	                    try {
	                        if(tfUser==null){
	                            ResponseVo.send102Code(response,"该用户id不存在！id="+userId);
	                            log.error("该用户id不存在！id="+userId);
	                            return false;
	                        }
	                    } catch (Exception e) {
	                        log.error(e);
	                        ResponseVo.send101Code(response,"userId格式有误！");
	                        e.printStackTrace();
	                        return false;
	                    }
	                }
	                //单点登录
	                deviceId = tfUser.getuDeviceId();
                }
                break;
            default:
                ResponseVo.send101Code(response,"请求头Realm填写有误:Realm="+realm);
                log.error("请求头Realm填写有误:Realm="+realm);
                return false;
        }
        String token = request.getHeader("Authorization");// 通行证
        if(StringUtil.empty(token)&&TransformUtils.toInt(userId)!=-1){
            ResponseVo.send101Code(response,"Authorization参数为空！");
            return false;
        }
  
        if (TransformUtils.toInt(userId)!=-1&&!token.equals(deviceId)) {//游客过滤
            ResponseVo.send111Code(response,"检测到其他设备登录，需要重新登录！");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
