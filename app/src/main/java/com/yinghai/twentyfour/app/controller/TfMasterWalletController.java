package com.yinghai.twentyfour.app.controller;

import com.yinghai.twentyfour.common.model.TfCashRecord;
import com.yinghai.twentyfour.common.model.TfMasterWallet;
import com.yinghai.twentyfour.common.service.TfCashRecordService;
import com.yinghai.twentyfour.common.service.TfMasterWalletService;
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
import java.util.Date;

/**
 * Created by Administrator on 2017/10/27.
 */
@Controller
@RequestMapping("app/account")
public class TfMasterWalletController {

    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TfMasterWalletService tfMasterWalletService;
    @Autowired
    private TfCashRecordService tfCashRecordService;

    @RequestMapping(value = "wallet", method = RequestMethod.POST)
    public void wallet(HttpServletRequest request, HttpServletResponse response){

        String masterId = request.getParameter("masterId");
        TfMasterWallet tfMasterWallet = tfMasterWalletService.findWalletByMasterId(Integer.parseInt(masterId));
        if (tfMasterWallet==null){
            tfMasterWallet = new TfMasterWallet();
        }
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONObject res = new JSONObject();
        res.put("tfMasterWallet",JSONObject.fromObject(tfMasterWallet,config));
        ResponseVo.send1Code(response,"success",res);
    }

    @RequestMapping(value = "takeCash", method = RequestMethod.POST)
    public void takeCash(HttpServletRequest request, HttpServletResponse response){

        String masterId = request.getParameter("masterId");
        String amountStr = request.getParameter("amount");
        TfCashRecord tfCashRecord = new TfCashRecord();
        tfCashRecord.setrMasterId(Integer.parseInt(masterId));
//        TfCashRecord cashRecord = tfCashRecordService.findNoCompleteCashRecord(Integer.parseInt(masterId));
//        if (cashRecord!=null){
//            ResponseVo.send603Code(response,"已有提现正在进行中");
//            return;
//        }
        TfMasterWallet tfMasterWallet = tfMasterWalletService.findWalletByMasterId(Integer.parseInt(masterId));
        Integer amount = 0;
        try{
            amount = Integer.parseInt(amountStr);
        }catch (Exception e){
            log.error(e);
            ResponseVo.send101Code(response,"amount不为int");
            e.printStackTrace();
        }
        if (amount<=0){
            ResponseVo.send601Code(response,"amount不能小于等于0");
            return;
        }
        if(tfMasterWallet.getwBalance()<amount){
            ResponseVo.send602Code(response,"提现金额不能大于账户余额！");
            return;
        }
        tfCashRecord.setrAmount(amount);
        tfCashRecord.setrCreateTime(new Date());
        tfCashRecord.setrStatus(TfCashRecord.APPLY_CASH_CODE);

        TfCashRecord send = tfCashRecordService.createTfCashRecord(tfCashRecord);
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONObject res = new JSONObject();
        res.put("tfCashRecord",JSONObject.fromObject(send,config));
        if (send!=null){
            ResponseVo.send1Code(response,"success",res);
        }else{
            ResponseVo.send106Code(response,"提现失败，稍后重试！");
        }
    }

    @RequestMapping(value = "cashRecord", method = RequestMethod.POST)
    public void cashRecord(HttpServletRequest request, HttpServletResponse response){

        String masterId = request.getParameter("masterId");

        String page = request.getParameter("page");
        if (StringUtil.empty(page)){
            page = "1";
        }
        String pageSize = request.getParameter("pageSize");
        if (StringUtil.empty(pageSize)){
            pageSize = "10";
        }
        TfCashRecord tfCashRecord = new TfCashRecord();
        tfCashRecord.setrMasterId(Integer.parseInt(masterId));
        Page<TfCashRecord> list = tfCashRecordService.findListByPage(Integer.parseInt(page),Integer.parseInt(pageSize),tfCashRecord) ;
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONObject responseJson = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(list.getResult(),config);
        responseJson.put("tfCashRecordList",jsonArray);
        responseJson.put("total",list.getTotal());
        responseJson.put("pages",list.getPages());
        responseJson.put("pageSize",list.getPageSize());
        responseJson.put("pageNum",list.getPageNum());
        ResponseVo.send1Code(response,"success",responseJson);
    }

}
