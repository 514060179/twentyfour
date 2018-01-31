package com.yinghai.twentyfour.app.controller;

import com.yinghai.twentyfour.common.model.TfBusinessType;
import com.yinghai.twentyfour.common.service.TfBusinessTypeService;
import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */
@Controller
@RequestMapping("app/tfBusinessType")
public class TfBusinessTypeController {

    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TfBusinessTypeService tfBusinessTypeService;

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public void businessTypeList(HttpServletRequest request, HttpServletResponse response){

        String pageNum = request.getParameter("page");
        String pageSize = request.getParameter("pageSize");

        JSONObject responseJson = new JSONObject();
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
        List<TfBusinessType> list = new ArrayList<TfBusinessType>();
        if (StringUtil.empty(pageNum)||StringUtil.empty(pageSize)){
            list = tfBusinessTypeService.findAll();
            responseJson.put("total",list.size());
            responseJson.put("pages",1);
            responseJson.put("pageSize",list.size());
            responseJson.put("pageNum",1);
        }else{
            int page=0,size=0;
            try{
                page = Integer.parseInt(pageNum);
                size = Integer.parseInt(pageSize);
            }catch (Exception e){
                ResponseVo.send104Code(response,"page/pageSize格式有误！");
                log.error("获取业务类型列表page/pageSize格式有误！");
                e.printStackTrace();
            }
            Page<TfBusinessType> p = tfBusinessTypeService.findByPage(page,size);
            list = p.getResult();
            responseJson.put("total",p.getTotal());
            responseJson.put("pages",p.getPages());
            responseJson.put("pageSize",p.getPageSize());
            responseJson.put("pageNum",p.getPageNum());
        }
        JSONArray jsonArray = JSONArray.fromObject(list,config);
        responseJson.put("tfBusinessTypeList",jsonArray);
        ResponseVo.send1Code(response,"success",responseJson);
    }
}
