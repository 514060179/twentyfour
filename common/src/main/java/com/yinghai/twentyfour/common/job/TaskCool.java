package com.yinghai.twentyfour.common.job;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yinghai.twentyfour.common.model.TfOrder;
import com.yinghai.twentyfour.common.service.TfOrderService;
@Component
public class TaskCool {
    @Autowired
    private TfOrderService tfOrderService;
    private Logger log = Logger.getLogger(this.getClass());
    SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
    /**
     * 订单定时任务，每隔5分钟查询一次30分钟后未支付的订单，并将订单改为取消
     */
    public void orderJob(){
    	 System.out.println("开始清除未支付订单 .... ");
    	Date time1 = new Date(System.currentTimeMillis() -30*60*1000);//30分钟之前的时间
    	  int i = tfOrderService.updateUnpaidTotalOrder(sdf.format(time1));
    	  log.info("================================="+new Date()+"=========================更新条数为"+i);
    }

}
