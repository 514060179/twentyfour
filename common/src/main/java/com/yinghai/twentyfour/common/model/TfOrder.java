package com.yinghai.twentyfour.common.model;

import java.util.Date;

public class TfOrder {
    /**
     * 未支付
     */
    public final static Integer payStatusNoPay = 1;
    /**
     * 已支付
     */
    public final static Integer payStatusPaidDone = 2;
    /**
     * 退款中
     */
    public final static Integer payStatusRebackMoney = 997;
    /**
     * 已经退款
     */
    public final static Integer payStatusRebackMoneyDone = 998;
    /**
     * 已取消
     */
    public final static Integer payStatusCancel = 999;
    /**
     * 未支付
     */
    public final static Integer orderStatusNoPay = 1;
    /**
     * 已支付
     */
    public final static Integer orderStatusPaidDone = 2;
    /**
     * 已确定
     */
    public final static Integer orderStatusMakeSure = 3;
    /**
     * 进行中
     */
    public final static Integer orderStatusHaveInHand = 4;
    /**
     * 已完成
     */
    public final static Integer orderStatusHaveDone = 5;
    /**
     * 已退款
     */
    public final static Integer orderStatusRebackMoneyDone = 998;
    /**
     * 已取消
     */
    public final static Integer orderStatusRebackCancel = 999;
    /**
     * 在线占卜订单
     */
    public final static Integer orderTypeOnlineDivination = 1;
    /**
     * 到访订单
     */
    public final static Integer orderTypeVisitDivination = 2;
    /**
     * 商品订单
     */
    public final static Integer orderTypeProduct = 3;
    /**
     * 微信支付
     */
    public final static Integer payWayWeChat= 1;
    /**
     * 支付宝支付
     */
    public final static Integer payWayAlipay= 2;

    private Integer orderId;

    private Integer oMasterId;

    private Integer oUserId;

    private Integer oProductId;

    private Integer oBusinessId;

    private Integer oPayStatus;

    private Date oPayTime;

    private Integer oStatus;
    //1在线占卜订单2到访占卜订单3商品订单
    private Integer oOrderType;

    private String oOrderNo;

    private String oLogisticsNo;

    private Date oCreateTime;

    private Date oUpdateTime;

    private Date oAppointmentTime;

    private Integer oPrice;

    private Integer oQty;

    private Integer oAmount;

    private Boolean oIsOrderTotal;

    private String oOrderTotalNo;

    private TfOrderAttach tfOrderAttach;

    private TfMaster tfMaster;

    private TfBusiness tfBusiness;

    private TfProduct tfProduct;

    private Integer oPayWay;

    private Date oCompleteTime;

    private TfUser tfUser;

    private Date oCancelTime;

    private Integer oCancelType;
    //用于查询
    private String[] statusArray;
    
    private Integer oAppointPeriod;
    
    public Integer getoAppointPeriod() {
		return oAppointPeriod;
	}

	public void setoAppointPeriod(Integer oAppointPeriod) {
		this.oAppointPeriod = oAppointPeriod;
	}

	public String[] getStatusArray() {
        return statusArray;
    }

    public void setStatusArray(String[] statusArray) {
        this.statusArray = statusArray;
    }

    public Date getoCancelTime() {
        return oCancelTime;
    }

    public void setoCancelTime(Date oCancelTime) {
        this.oCancelTime = oCancelTime;
    }

    public Integer getoCancelType() {
        return oCancelType;
    }

    public void setoCancelType(Integer oCancelType) {
        this.oCancelType = oCancelType;
    }

    public TfUser getTfUser() {
        return tfUser;
    }

    public void setTfUser(TfUser tfUser) {
        this.tfUser = tfUser;
    }

    public Date getoCompleteTime() {
        return oCompleteTime;
    }

    public void setoCompleteTime(Date oCompleteTime) {
        this.oCompleteTime = oCompleteTime;
    }

    public Integer getoPayWay() {
        return oPayWay;
    }

    public void setoPayWay(Integer oPayWay) {
        this.oPayWay = oPayWay;
    }

    public static Integer getPayStatusNoPay() {
        return payStatusNoPay;
    }

    public TfProduct getTfProduct() {
        return tfProduct;
    }

    public void setTfProduct(TfProduct tfProduct) {
        this.tfProduct = tfProduct;
    }

    public TfMaster getTfMaster() {
        return tfMaster;
    }

    public void setTfMaster(TfMaster tfMaster) {
        this.tfMaster = tfMaster;
    }

    public TfBusiness getTfBusiness() {
        return tfBusiness;
    }

    public void setTfBusiness(TfBusiness tfBusiness) {
        this.tfBusiness = tfBusiness;
    }

    public TfOrderAttach getTfOrderAttach() {
        return tfOrderAttach;
    }

    public void setTfOrderAttach(TfOrderAttach tfOrderAttach) {
        this.tfOrderAttach = tfOrderAttach;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getoMasterId() {
        return oMasterId;
    }

    public void setoMasterId(Integer oMasterId) {
        this.oMasterId = oMasterId;
    }

    public Integer getoUserId() {
        return oUserId;
    }

    public void setoUserId(Integer oUserId) {
        this.oUserId = oUserId;
    }

    public Integer getoProductId() {
        return oProductId;
    }

    public void setoProductId(Integer oProductId) {
        this.oProductId = oProductId;
    }

    public Integer getoBusinessId() {
        return oBusinessId;
    }

    public void setoBusinessId(Integer oBusinessId) {
        this.oBusinessId = oBusinessId;
    }

    public Integer getoPayStatus() {
        return oPayStatus;
    }

    public void setoPayStatus(Integer oPayStatus) {
        this.oPayStatus = oPayStatus;
    }

    public Date getoPayTime() {
        return oPayTime;
    }

    public void setoPayTime(Date oPayTime) {
        this.oPayTime = oPayTime;
    }

    public Integer getoStatus() {
        return oStatus;
    }

    public void setoStatus(Integer oStatus) {
        this.oStatus = oStatus;
    }

    public Integer getoOrderType() {
        return oOrderType;
    }

    public void setoOrderType(Integer oOrderType) {
        this.oOrderType = oOrderType;
    }

    public String getoOrderNo() {
        return oOrderNo;
    }

    public void setoOrderNo(String oOrderNo) {
        this.oOrderNo = oOrderNo == null ? null : oOrderNo.trim();
    }

    public String getoLogisticsNo() {
        return oLogisticsNo;
    }

    public void setoLogisticsNo(String oLogisticsNo) {
        this.oLogisticsNo = oLogisticsNo == null ? null : oLogisticsNo.trim();
    }

    public Date getoCreateTime() {
        return oCreateTime;
    }

    public void setoCreateTime(Date oCreateTime) {
        this.oCreateTime = oCreateTime;
    }

    public Date getoUpdateTime() {
        return oUpdateTime;
    }

    public void setoUpdateTime(Date oUpdateTime) {
        this.oUpdateTime = oUpdateTime;
    }

    public Date getoAppointmentTime() {
        return oAppointmentTime;
    }

    public void setoAppointmentTime(Date oAppointmentTime) {
        this.oAppointmentTime = oAppointmentTime;
    }

    public Integer getoPrice() {
        return oPrice;
    }

    public void setoPrice(Integer oPrice) {
        this.oPrice = oPrice;
    }

    public Integer getoQty() {
        return oQty;
    }

    public void setoQty(Integer oQty) {
        this.oQty = oQty;
    }

    public Integer getoAmount() {
        return oAmount;
    }

    public void setoAmount(Integer oAmount) {
        this.oAmount = oAmount;
    }

    public Boolean getoIsOrderTotal() {
        return oIsOrderTotal;
    }

    public void setoIsOrderTotal(Boolean oIsOrderTotal) {
        this.oIsOrderTotal = oIsOrderTotal;
    }

    public String getoOrderTotalNo() {
        return oOrderTotalNo;
    }

    public void setoOrderTotalNo(String oOrderTotalNo) {
        this.oOrderTotalNo = oOrderTotalNo;
    }
}