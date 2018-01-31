package com.yinghai.twentyfour.app.controller;

import com.yinghai.twentyfour.app.service.HistoryService;
import com.yinghai.twentyfour.common.model.TfBusiness;
import com.yinghai.twentyfour.common.model.TfHistory;
import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.service.TfBusinessService;
import com.yinghai.twentyfour.common.service.TfMasterService;
import com.yinghai.twentyfour.common.service.TfUserService;
import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/10/24.
 */
@Controller
@RequestMapping("app/business")
public class TfBusinessController {

    Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private TfMasterService tfMasterService;
    @Autowired
    private TfBusinessService tfBusinessService;
    @Autowired
    private HistoryService tfHistoryService;
    @Autowired
    private TfUserService tfUserService;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public void save(HttpServletRequest request, HttpServletResponse response){
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        TfBusiness tfBusiness = new TfBusiness();
        String masterId = request.getParameter("masterId");
        if (StringUtil.empty(masterId)){
            ResponseVo.send101Code(response,"masterId为空！");
            return;
        }
        try {
//            TfMaster tfMaster = tfMasterService.selectByPrimaryKey(Integer.parseInt(masterId));
//            if(tfMaster==null){
//                ResponseVo.send102Code(response,"该大师id不存在！id="+masterId);
//                log.error("该大师id不存在！id="+masterId);
//                return;
//            }
            tfBusiness.setbMasterId(Integer.parseInt(masterId));
        } catch (Exception e) {
            log.error(e);
            ResponseVo.send101Code(response,"masterId格式有误！");
            e.printStackTrace();
        }
        String name = request.getParameter("name");
        if(StringUtil.empty(name)){
            ResponseVo.send101Code(response,"name为空！");
            return;
        }
        tfBusiness.setbName(name);
        String type = request.getParameter("type");
        if(StringUtil.empty(type)){
            ResponseVo.send101Code(response,"type为空！");
            return;
        }
        tfBusiness.setbType(type);
        String introduction = request.getParameter("introduction");
        if(StringUtil.empty(introduction)){
            ResponseVo.send101Code(response,"introduction为空！");
            return;
        }
        tfBusiness.setbIntroduction(introduction);
        String price = request.getParameter("price");
        if(StringUtil.empty(price)){
            ResponseVo.send101Code(response,"price为空！");
            return;
        }
        try{
            tfBusiness.setbPrice(Integer.parseInt(price));
        }catch (Exception e){
            ResponseVo.send101Code(response,"price不为int");
            log.error(e);
            return;
        }
        if(Integer.parseInt(price)<=0){
            ResponseVo.send701Code(response,"price不能小于等于0");
            return;
        }
        tfBusiness.setbCreateTime(new Date());
        int i = tfBusinessService.saveBusiness(tfBusiness);
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONObject responseJson = new JSONObject();
        responseJson.put("tfBusiness",JSONObject.fromObject(tfBusiness,config));
        if(i>0){
            ResponseVo.send1Code(response,"success",responseJson );
        }else{
            ResponseVo.send106Code(response,"新增业务失败！");
        }
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public void list(HttpServletRequest request, HttpServletResponse response){
        String masterId = request.getParameter("masterId");
        if (StringUtil.empty(masterId)){
            ResponseVo.send101Code(response,"masterId为空！");
            return;
        }
        String page = request.getParameter("page");
        if (StringUtil.empty(page)){
            page = "1";
        }
        String pageSize = request.getParameter("pageSize");
        if (StringUtil.empty(pageSize)){
            pageSize = "10";
        }
        Page<TfBusiness> list = null;
        TfBusiness tfBusiness = new TfBusiness();
        tfBusiness.setbMasterId(Integer.parseInt(masterId));
        String type = request.getParameter("type");//大师ID
		if (StringUtil.notEmpty(type)) {
			//tfBusiness.setbType(type);
			String[] array = type.split(",");
			tfBusiness.setbTypeArray(array);
		}
        try{
            list = tfBusinessService.findByPage(Integer.parseInt(page),Integer.parseInt(pageSize),tfBusiness);
        }catch (Exception e){
            log.error("分页查询订单时传入page或pageSize不为int ："+page+"/"+pageSize);
            ResponseVo.send101Code(response,"page值或pageSize值不为int");
            e.printStackTrace();
        }
        if (list==null||list.size()<=0){
            ResponseVo.send1Code(response,"success",new JSONObject());
            return;
        }

        JSONObject responseJson = new JSONObject();
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONArray jsonArray = JSONArray.fromObject(list.getResult(),config);
        responseJson.put("tfBusinessList",jsonArray);
        responseJson.put("total",list.getTotal());
        responseJson.put("pages",list.getPages());
        responseJson.put("pageSize",list.getPageSize());
        responseJson.put("pageNum",list.getPageNum());
        ResponseVo.send1Code(response,"success",responseJson);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public void update(HttpServletRequest request, HttpServletResponse response){

        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        TfBusiness tfBusiness = new TfBusiness();
        String businessId = request.getParameter("businessId");
        String masterId = request.getParameter("masterId");
        if (StringUtil.empty(businessId)){
            ResponseVo.send101Code(response,"businessId为空！");
            return;
        }
        //业务查询
        TfBusiness business = null;
        try{
            business = tfBusinessService.findById(Integer.parseInt(businessId),Integer.parseInt(masterId));
            if(business==null){
                ResponseVo.send102Code(response,"该业务id不存在:"+businessId);
                return;
            }
        }catch (Exception e){
            ResponseVo.send101Code(response,"businessId不为int");
            log.error(e);
            e.printStackTrace();
            return;
        }
        try{
            tfBusiness.setBusinessId(Integer.parseInt(businessId));
        }catch (Exception e){
            log.error(e);
            ResponseVo.send101Code(response,"businessId不为int");
            e.printStackTrace();
            return;
        }
        String name = request.getParameter("name");
        if(!StringUtil.empty(name)){
            tfBusiness.setbName(name);
        }
        String introduction = request.getParameter("introduction");
        if(!StringUtil.empty(introduction)){
            tfBusiness.setbIntroduction(introduction);
        }
        String price = request.getParameter("price");
        if(!StringUtil.empty(price)){
            try{
                tfBusiness.setbPrice(Integer.parseInt(price));
            }catch (Exception e){
                log.error(e);
                ResponseVo.send101Code(response,"price不为int");
                e.printStackTrace();
                return;
            }
        }
        if(Integer.parseInt(price)<=0){
            ResponseVo.send701Code(response,"price不能小于等于0");
            return;
        }
        tfBusiness.setbUpdateTime(new Date());
        int i = tfBusinessService.updateBusiness(tfBusiness);
        if(i>0){
            ResponseVo.send1Code(response,"success",new JSONObject() );
        }else{
            ResponseVo.send106Code(response,"更新业务失败！");
        }
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public void delete(HttpServletRequest request, HttpServletResponse response){

        String businessId = request.getParameter("businessId");
        String masterId = request.getParameter("masterId");
        if (StringUtil.empty(businessId)){
            ResponseVo.send101Code(response,"businessId为空！");
            return;
        }
        TfBusiness business = null;
        try{
            business = tfBusinessService.findById(Integer.parseInt(businessId),Integer.parseInt(masterId));
            if(business==null){
                ResponseVo.send102Code(response,"该业务id不存在:"+businessId);
                return;
            }
        }catch (Exception e){
            log.error(e);
            ResponseVo.send101Code(response,"businessId不为int");
            e.printStackTrace();
            return;
        }
        //查询该大师最低业务价格
        TfMaster tfMaster = new TfMaster();
        List<TfBusiness> list = tfBusinessService.findLowerBusiness(Integer.parseInt(masterId));
        if (list!=null&&list.size()==2){
            if (business.getbPrice()==list.get(0).getbPrice()){//删除当前的业务与最低业务比较
                tfMaster.setmBargain(list.get(1).getbPrice());
            }
        }else{
            tfMaster.setmBargain(0);
        }

        tfMaster.setMasterId(Integer.parseInt(masterId));
        tfMaster.setmUpdateTime(new Date());
        int i = tfBusinessService.deleteBusiness(Integer.parseInt(businessId), tfMaster);
        if (i > 0) {
            ResponseVo.send1Code(response, "success", new JSONObject());
        } else {
            ResponseVo.send106Code(response, "业务删除失败！");
        }
    }
}
