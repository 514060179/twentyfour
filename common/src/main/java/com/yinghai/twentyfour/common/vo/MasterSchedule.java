package com.yinghai.twentyfour.common.vo;

import java.util.Date;

/**
 * Created by Administrator on 2017/10/26.
 */
public class MasterSchedule {

    /**
     * 预约日期
     */
    private Date msDate;

    /**
     * 订单总数
     */
    private Integer msQty;

    public Date getMsDate() {
        return msDate;
    }

    public void setMsDate(Date msDate) {
        this.msDate = msDate;
    }

    public Integer getMsQty() {
        return msQty;
    }

    public void setMsQty(Integer msQty) {
        this.msQty = msQty;
    }
}
