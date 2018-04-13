package com.yinghai.twentyfour.common.job;

import com.yinghai.twentyfour.common.model.TfOrder;
import com.yinghai.twentyfour.common.service.TfOrderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/4/11.
 */
@Component
public class RefundTask {

    @Autowired
    private TfOrderService tfOrderService;

    private Logger log = Logger.getLogger(this.getClass());

    public void refundJob(){
        List<TfOrder> tfOrderList = tfOrderService.findAllHadPaidList();
        System.out.println("=========");
        if (tfOrderList.size()>0){
            for (TfOrder order:tfOrderList
                 ) {
                boolean success = tfOrderService.refuundMoney(order);
                if (success){
                    log.info("大师未确定订单退款成功：#"+order.getOrderId()+"/支付方式"+order.getoPayWay()+"/退款金额(单位分)"+order.getoAmount());
                    System.out.println("大师未确定订单退款成功：#"+order.getOrderId()+"/支付方式"+order.getoPayWay()+"/退款金额(单位分)"+order.getoAmount());
                }
            }
        }
    }
}
