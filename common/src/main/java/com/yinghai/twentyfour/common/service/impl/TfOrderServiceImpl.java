package com.yinghai.twentyfour.common.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradeRefundResponse;
//import com.alipay.api.AlipayApiException;
//import com.alipay.api.response.AlipayTradeRefundResponse;
import com.yinghai.twentyfour.common.constant.PushCode;
import com.yinghai.twentyfour.common.constant.WeChat;
import com.yinghai.twentyfour.common.dao.TfBusinessMapper;
import com.yinghai.twentyfour.common.dao.TfMasterMapper;
import com.yinghai.twentyfour.common.dao.TfMasterWalletMapper;
import com.yinghai.twentyfour.common.dao.TfOrderAttachMapper;
import com.yinghai.twentyfour.common.dao.TfOrderMapper;
import com.yinghai.twentyfour.common.dao.TfOrderTotalMapper;
import com.yinghai.twentyfour.common.dao.TfProductMapper;
import com.yinghai.twentyfour.common.dao.TfUserMapper;
import com.yinghai.twentyfour.common.im.constant.App;
import com.yinghai.twentyfour.common.im.method.Message;
import com.yinghai.twentyfour.common.im.model.CodeSuccessResult;
import com.yinghai.twentyfour.common.im.msg.TxtMessage;
import com.yinghai.twentyfour.common.model.TfBusiness;
import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.model.TfMasterWallet;
import com.yinghai.twentyfour.common.model.TfOrder;
import com.yinghai.twentyfour.common.model.TfOrderAttach;
import com.yinghai.twentyfour.common.model.TfOrderTotal;
import com.yinghai.twentyfour.common.model.TfOrderTotalHelper;
import com.yinghai.twentyfour.common.model.TfProduct;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.service.TfOrderService;
import com.yinghai.twentyfour.common.service.TfProductService;
import com.yinghai.twentyfour.common.service.TfUserService;
import com.yinghai.twentyfour.common.util.*;
import com.yinghai.twentyfour.common.vo.MasterSchedule;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/24.
 */
public class TfOrderServiceImpl implements TfOrderService {

    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TfOrderMapper tfOrderMapper;
    @Autowired
    private TfOrderAttachMapper tfOrderAttachMapper;
    @Autowired
    private TfMasterWalletMapper tfMasterWalletMapper;
    @Autowired
    private TfOrderTotalMapper tfOrderTotalMapper;
    @Autowired
    private TfProductMapper tfProductMapper;
    @Autowired
    private TfBusinessMapper tfBusinessMapper;

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public TfOrder createOrder(TfOrder tfOrder, TfOrderAttach tfOrderAttach) {

        int i = tfOrderMapper.insertSelective(tfOrder);
        tfOrderAttach.setAhOrderId(tfOrder.getOrderId());
        i += tfOrderAttachMapper.insertSelective(tfOrderAttach);
        return tfOrder;
    }

    @Override
    public List<TfOrder> findByPage(Integer start, Integer pageSize, TfOrder tfOrder) {
        //PageHelper.startPage(pageNumber,pageSize);
        return tfOrderMapper.findPageByCondition(start,pageSize,tfOrder);
    }
    @Override
    public Page<TfOrder> findBackByPage(Integer pageNumber, Integer pageSize, TfOrder tfOrder){
    	PageHelper.startPage(pageNumber,pageSize);
    	return tfOrderMapper.findBackPageByCondition(tfOrder);
    }

    @Override
    public TfOrder findByOrderNo(String orderNo,Integer userId) {
        return tfOrderMapper.findByOrderNoAndUser(orderNo,userId);
    }

    @Override
    public int callbackUpdateStatus(String orderNo,Integer payWay) {
        TfOrder tfOrder = new TfOrder();
        tfOrder.setoOrderNo(orderNo);
        tfOrder.setoStatus(TfOrder.orderStatusPaidDone);
        tfOrder.setoPayStatus(TfOrder.payStatusPaidDone);
        tfOrder.setoPayWay(payWay);
        tfOrder.setoPayTime(new Date());
        tfOrder.setoUpdateTime(new Date());
        return tfOrderMapper.updateByConditon(tfOrder);
    }

    @Override
    @Transactional
    public int callbackUpdate(String orderNo, Integer payWay) {
        TfOrder tfOrder = new TfOrder();
        tfOrder.setoOrderNo(orderNo);
        tfOrder.setoOrderTotalNo(orderNo);
        tfOrder.setoStatus(TfOrder.orderStatusPaidDone);
        tfOrder.setoPayStatus(TfOrder.payStatusPaidDone);
        tfOrder.setoPayWay(payWay);
        tfOrder.setoPayTime(new Date());
        tfOrder.setoUpdateTime(new Date());
        int i = tfOrderMapper.updateByConditonTotal(tfOrder);
        //更新总订单状态
        TfOrderTotal record = new TfOrderTotal();
        TfOrderTotal r = tfOrderTotalMapper.findOrderTotalByNO(orderNo);
        if(r==null){
        	log.error("总订单不存在");
        	throw new RuntimeException("总订单不存在");
        }
        record.setTotalId(r.getTotalId());
        record.settStatus(2);
        int j = tfOrderTotalMapper.updateByPrimaryKeySelective(record);
        if(j!=1){
        	log.error("更新总订单状态失败");
        	throw new RuntimeException("更新总订单状态失败");
        }
        return i+j;
    }

    @Override
    public TfOrder findById(Integer orderId,Integer masterId) {
        return tfOrderMapper.selectByPrimaryKey(orderId,masterId,null);
    }

    @Override
    public TfOrder selectById(Integer orderId, Integer userId) {
        return tfOrderMapper.selectByPrimaryKey(orderId,null,userId);
    }

    @Override
    public int masterMakeSure(Integer orderId) {
        TfOrder tfOrder = new TfOrder();
        tfOrder.setOrderId(orderId);
        tfOrder.setoStatus(TfOrder.orderStatusMakeSure);
        return tfOrderMapper.updateByPrimaryKeySelective(tfOrder);
    }

    @Override
    public TfOrder findAllModelById(Integer orderId) {
        return tfOrderMapper.findOrderWithAllModelById(orderId);
    }

    @Override
    public List<MasterSchedule> findMasterSchedule(Integer masterId, Integer status, Date msDate) {
        return tfOrderMapper.findMasterSchedule(masterId,status,msDate);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public int cancelOrder(TfOrder order) {
        TfOrder update = new TfOrder();
        update.setOrderId(order.getOrderId());
        update.setoStatus(TfOrder.orderStatusRebackMoneyDone);
        update.setoUpdateTime(new Date());
        update.setoCancelTime(new Date());
        update.setoCancelType(order.getoCancelType());
        //退款操作
        int payType = order.getoPayWay();
        int orderType = order.getoOrderType();
        TfOrder totalOrder = null;
        String orderNo = "";
        String outRequestNo = null;
        String totalFee=order.getoAmount()+"";//支付总金额 
        String refundFee = order.getoAmount()+""; //退款金额
        if (TfOrder.orderTypeOnlineDivination==orderType){//在线占卜订单
            orderNo = order.getoOrderNo();
            totalOrder = order;
            
        }else if (TfOrder.orderTypeProduct==orderType){//商品订单
            orderNo = order.getoOrderTotalNo();
            outRequestNo = order.getoOrderNo();
            totalOrder = findByOrderNo(order.getoOrderNo(), order.getoUserId());//商品订单时需要将总订单查询出来
            if(totalOrder==null){
            	return -1;
            }
            if(totalOrder.getoOrderTotalNo().equals(order.getoOrderNo())){//如果这两个值相等，说明该订单为总订单，总订单不给予退款
            	return -2;
            }
            totalFee = totalOrder.getoAmount()+"";//当订单为商品订单时，订单总金额为总订单的价钱
        }
        if (TfOrder.payWayWeChat==payType){//微信支付
            //商品退款，获取总订单编号,占卜订单
            try {
                Map map = WeChatPayUtils.orderrefund(orderNo,totalFee,refundFee,WeChat.weixinAPPPayType);
                if("FAIL".equals(map.get("return_code"))){
                    log.error("======退款操作失败！======"+map.get("return_msg")+map.get("err_code_des"));
                    throw new RuntimeException("退款操作失败"+map.get("return_msg")+map.get("err_code_des"));
                }
            } catch (Exception e) {
                log.error(e);
                e.printStackTrace();
                throw new RuntimeException("退款失败！",e);
            }
        }else if (TfOrder.payWayAlipay==payType){//支付宝支付
            //商品退款、占卜订单
            try {
                AlipayTradeRefundResponse refundResponse = AlipayUtil.refund(orderNo,outRequestNo,(double)order.getoAmount()/100,"退款原因");
                if(!refundResponse.isSuccess()){
                    log.error(refundResponse.getBody());
                    throw new RuntimeException("退款失败！"+refundResponse.getBody());
                }
            } catch (AlipayApiException e) {
                log.error(e);
                e.printStackTrace();
                throw new RuntimeException("退款失败！",e);
            }
        }
        update.setoPayStatus(TfOrder.payStatusRebackMoneyDone);
        int i = tfOrderMapper.updateByPrimaryKeySelective(update);
        String upushCode = "";
        String upushMsg = "";
        String mpushCode = "";
        String mpushMsg = "";
        if (order.getoCancelType()==1){//大师取消
        	upushCode = PushCode.UOrderCancelByMaster;
            upushMsg = PushCode.UOrderCancelByMasterMsg;
            mpushCode = PushCode.MOrderCancelByMaster;
            mpushMsg = PushCode.MOrderCancelByMasterMsg;
        }else if (order.getoCancelType()==2){//用户取消
            upushCode = PushCode.UOrderCancelByUser;
            upushMsg = PushCode.UOrderCancelByUserMsg;
            mpushCode = PushCode.MOrderCancelByUser;
            mpushMsg = PushCode.MOrderCancelByUserMsg;
        }
        //通知用户退款成功
        Message message = new Message();
        try {
            JSONObject jsonObject = new JSONObject();
            JsonConfig config = new JsonConfig();
            config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
            JSONObject res = new JSONObject();
            TfOrder order1 = order;
//            order1.setTfUser(null);
            JSONObject or = JSONObject.fromObject(order1, config);
            res.put("order",or);
            res.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            jsonObject.put("code", upushCode);
            jsonObject.put("msg",upushMsg);
            jsonObject.put("data",res);
            jsonObject = JSONObject.fromObject(jsonObject, config);
//            System.out.println("推送："+order1.getTfUser().getuIm());
//
//            System.out.println("推送："+order1.getTfMaster().getmIm());
            //用户
            CodeSuccessResult r1 = message.publishSystem("admin",new String[]{order1.getTfUser().getuIm()},new TxtMessage(jsonObject.toString(),""),upushMsg,jsonObject.toString(), 1, 1);
            //CodeSuccessResult r1 = message.publishPrivate("admin",new String[]{order.getTfUser().getuIm()},new TxtMessage(jsonObject.toString(),""),upushMsg,jsonObject.toString(),"1",1,1,1,1);
            if (r1.getCode()!=200){
                log.error("取消订单通知用户失败！"+r1);
            }

            //大师
            jsonObject.put("code", mpushCode);
            jsonObject.put("msg",mpushMsg);
            CodeSuccessResult r2 = message.publishSystem("admin",new String[]{order1.getTfMaster().getmIm()},new TxtMessage(jsonObject.toString(),""),mpushMsg,jsonObject.toString(), 1, 1);
            //CodeSuccessResult r2 = message.publishPrivate("admin",new String[]{order.getTfMaster().getmIm()},new TxtMessage(jsonObject.toString(),""),mpushMsg,jsonObject.toString(),"1",1,1,1,1);
            if (r2.getCode()!=200){
                log.error("取消订单通知大师失败！"+r2);
            }
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
        //通知退款大师订单取消
        return i;
    }

    @Override
    @Transactional
    public int cancelUpdateOrder(TfOrder order) {
        TfOrder update = new TfOrder();
        update.setOrderId(order.getOrderId());
        update.setoStatus(TfOrder.orderStatusRebackCancel);
        update.setoCancelType(order.getoCancelType());
        update.setoUpdateTime(new Date());
        update.setoCancelTime(new Date());
        update.setoPayStatus(TfOrder.payStatusCancel);
        int i = tfOrderMapper.updateByPrimaryKeySelective(update);
        if (i>0){
            //通知用户端
            Message message = new Message();
            CodeSuccessResult r1 = null;
            try {
            	r1 = message.publishSystem("admin", new String[]{order.getTfUser().getuIm()}, new TxtMessage("退款成功",""), "退款成功", "退款信息", 1,1);
                //r1 = message.publishPrivate("admin",new String[]{order.getTfUser().getuIm()},new TxtMessage("退款成功",""),"退款成功","退款信息","1",1,1,1,1);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e);
            }
            if (r1.getCode()!=200){
                log.error("取消订单通知用户失败！"+r1);
            }
        }
        return i;
    }

    @Override
	public TfOrder findAllModelByOrderId(Integer orderId, Integer isProduct) {
		return tfOrderMapper.findOrderWithAllModelByOrderId(orderId,isProduct);
	}

	@Override
	public Map<String, Object> findFinishOrderByTime(Integer masterId, Date start) {
		return tfOrderMapper.findFinishOrderByTime(masterId,start);
	}

	@Override
	public TfOrder findByUserId(Integer orderId, Integer userId) {
		return tfOrderMapper.selectByPrimaryKey(orderId, null, userId);
	}

	@Override
	@Transactional
	public int completeOrder(TfOrder order) {
		//更新订单状态，然后更新大师余额
		TfOrder o = new TfOrder();
		o.setOrderId(order.getOrderId());
		o.setoStatus(TfOrder.orderStatusHaveDone);
		o.setoUpdateTime(new Date());
		int i = tfOrderMapper.updateByPrimaryKeySelective(o);
		if(i!=1){
			log.error("订单状态更新失败，completeOrder:"+order.getOrderId());
			throw new RuntimeException("订单状态更新失败");
		}
		//更新大师余额
		Integer masterId = order.getoMasterId();
		TfMasterWallet w = tfMasterWalletMapper.findWalletByMasterId(masterId);
		if(w==null){
			log.error("大师钱包数据不存在,masterId:"+masterId);
			throw new RuntimeException("大师钱包数据不存在");
		}
		Integer b = w.getwBalance();
		b = b + order.getoAmount();
		//更新大师余额
		TfMasterWallet record = new TfMasterWallet();
		record.setWalletId(w.getWalletId());
		record.setwBalance(b);
		record.setwUpdateTime(new Date());
		int j = tfMasterWalletMapper.updateByPrimaryKeySelective(record);
		if(j!=1){
			log.error("大师余额更新失败，completeOrder:"+order.getOrderId());
			throw new RuntimeException("大师余额更新失败");
		}
		//更新业务成交量
		TfBusiness business = tfBusinessMapper.selectByPrimaryKey(order.getoBusinessId());
		if(business==null){
			log.error("订单对应业务不存在，completeOrder:"+order.getOrderId());
			throw new RuntimeException("订单对应业务不存在");
		}
		TfBusiness busy = new TfBusiness();
		busy.setBusinessId(business.getBusinessId());
		busy.setbDeals(business.getbDeals()+order.getoQty());
		busy.setbUpdateTime(new Date());
		int k = tfBusinessMapper.updateByPrimaryKeySelective(busy);
		if(k!=1){
			log.error("订单对应业务成交量更新失败，completeOrder:"+order.getOrderId());
			throw new RuntimeException("订单对应业务成交量更新失败");
		}
		//推送消息给大师
		order.setoStatus(TfOrder.orderStatusHaveDone);
		JSONObject res = new JSONObject();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		res.put("order",JSONObject.fromObject(order, config));
		JSONObject json = new JSONObject();
		json.put("code", PushCode.MOrderCompleteByUser);
		json.put("msg", PushCode.MOrderCompleteByUserMsg);
		json.put("data", res);
		json = JSONObject.fromObject(json, config);
		TxtMessage m = new TxtMessage(json.toString(), "");
		Message message = new Message();
		String[] strs = {"master"+order.getoMasterId()};
		try {
			CodeSuccessResult result = message.publishSystem(App.admin, strs, m, PushCode.MOrderCompleteByUserMsg, json.toString(), 1, 1);
			if(result.getCode()!=200){
				throw new RuntimeException("推送消息发送失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("推送消息发送失败");
		}
		return 1;
	}

	@Override
	@Transactional
	public int completeProductOrder(TfOrderTotalHelper h) {
		List<TfOrder> list = h.getOrderList();
		for(TfOrder o:list){
			//判断订单状态是否正确
			if(o.getoStatus()==TfOrder.orderStatusHaveInHand){//进行中的订单
				//更新订单状态
				TfOrder record = new TfOrder();
				record.setOrderId(o.getOrderId());
				record.setoStatus(TfOrder.orderStatusHaveDone);
				record.setoUpdateTime(new Date());
				int i = tfOrderMapper.updateByPrimaryKeySelective(record);
				if(i!=1){
					log.error("订单状态更新失败,orderId:"+o.getOrderId());
					throw new RuntimeException("订单状态更新失败");
				}
				Integer sum = o.getoAmount();
				//更新大师余额
				TfMasterWallet m = tfMasterWalletMapper.findWalletByMasterId(o.getoMasterId());
				if(m==null){
					log.error("大师钱包不存在,masterId:"+o.getoMasterId());
					throw new RuntimeException("大师钱包不存在");
				}
				sum = sum + m.getwBalance();
				TfMasterWallet w = new TfMasterWallet();
				w.setWalletId(m.getWalletId());
				w.setwBalance(sum);
				w.setwUpdateTime(new Date());
				int j = tfMasterWalletMapper.updateByPrimaryKeySelective(w);
				if(j!=1){
					log.error("大师余额更新失败，orderId:"+o.getOrderId());
					throw new RuntimeException("大师余额更新失败");
				}
				//更新商品成交量
					//查询该商品
				TfProduct product = tfProductMapper.selectByPrimaryKey(o.getoProductId());
				if(product!=null){
					TfProduct p = new TfProduct();
					p.setProductId(product.getProductId());
					p.setpDeals(product.getpDeals()+o.getoQty());
					p.setpUpdateTime(new Date());
					int k = tfProductMapper.updateByPrimaryKeySelective(p);
					if(k!=1){
						log.error("商品成交量更新失败，orderId:"+o.getOrderId());
						throw new RuntimeException("商品成交量更新失败");
					}
				}else{
					log.error("对应商品不存在，productId:"+o.getoProductId());
					throw new RuntimeException("对应商品不存在");
				}
			}else if(o.getoStatus()!=TfOrder.orderStatusRebackCancel&&o.getoStatus()!=TfOrder.orderStatusRebackMoneyDone){
				log.error("订单状态有误,orderId:"+o.getOrderId());
				return -1;
			}
		}
		JSONObject res = new JSONObject();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		res.put("totalOrder",JSONObject.fromObject(h, config));
		JSONObject json = new JSONObject();
		json.put("code", PushCode.UOrderComplete);
		json.put("msg", PushCode.UOrderCompleteMsg);
		json.put("data", res);
		json = JSONObject.fromObject(json, config);
		//订单完成推送
		TxtMessage m = new TxtMessage(json.toString(), "");
		Message message = new Message();
		String[] strs = {"user"+h.gettUserId()};
		try {
			CodeSuccessResult result = message.publishSystem(App.admin, strs, m, PushCode.UOrderCompleteMsg, json.toString(), 1, 1);
			if(result.getCode()!=200){
				throw new RuntimeException("推送消息发送失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("推送消息发送失败");
		}
		return 1;
	}

	@Override
	public Page<TfOrderTotalHelper> findProductOrderByPage(Integer pageNumber, Integer pageSize,TfOrder tfOrder) {
		Integer start = (pageNumber-1)*pageSize;
		return tfOrderMapper.findProductByCondition(tfOrder,start,pageSize);
	}
	@Override
	public int updateOrder(TfOrder tfOrder) {
		// TODO Auto-generated method stub
		return tfOrderMapper.updateByPrimaryKeySelective(tfOrder);
	}
	
	@Override
	@Transactional
	public int callbackUpdateOrder(TfOrderTotal totalOrder, Integer payWay) {
		//判断是用父总订单支付还是用子总订单支付
		if(totalOrder.gettParentId()==null){//父总订单支付
			//更新父总订单状态
			TfOrderTotal t1 = new TfOrderTotal();
			t1.setTotalId(totalOrder.getTotalId());
			t1.settStatus(2);//已支付
			int i = tfOrderTotalMapper.updateByPrimaryKeySelective(t1);
			if(i!=1){
				log.error("父总订单状态更新失败");
				throw new RuntimeException("总订单状态更新失败");
			}
			//更新对应子总订单状态,更新子订单的状态等参数
			List<TfOrderTotal> list = tfOrderTotalMapper.findByPayOrderNo(totalOrder.gettOrderNo());
			if(list==null||list.size()<1){
				log.error("对应的子总订单不存在");
				throw new RuntimeException("总订单数据出错");
			}
			for(TfOrderTotal t:list){
				//更新子总订单
				TfOrderTotal t2 = new TfOrderTotal();
				t2.setTotalId(t.getTotalId());
				t2.settStatus(2);//已支付
				int j = tfOrderTotalMapper.updateByPrimaryKeySelective(t2);
				if(j!=1){
					log.error("子总订单状态更新失败");
					throw new RuntimeException("总订单状态更新失败");
				}
				//更新子订单参数状态
				List<TfOrder> orders = tfOrderMapper.findOrdersByTotalNo(t.gettOrderNo());
				if(orders==null||orders.size()<1){
					log.error("对应的子订单不存在");
					throw new RuntimeException("子订单数据出错");
				}
				for(TfOrder o:orders){
					TfOrder order = new TfOrder();
					order.setOrderId(o.getOrderId());
					order.setoPayStatus(TfOrder.orderStatusPaidDone);
					order.setoStatus(TfOrder.payStatusPaidDone);
					order.setoPayWay(payWay);
					order.setoPayTime(new Date());
					order.setoUpdateTime(new Date());
					int k = tfOrderMapper.updateByPrimaryKeySelective(order);
					if(k!=1){
						throw new RuntimeException("子订单状态更新失败");
					}
				}
			}
			return i;
		}else{//子总订单支付
			//更新子总订单状态，更新子订单的状态等参数
			TfOrderTotal t3 = new TfOrderTotal();
			t3.setTotalId(totalOrder.getTotalId());
			t3.settPayNo(totalOrder.gettOrderNo());
			t3.settStatus(TfOrder.payStatusPaidDone);
			int i = tfOrderTotalMapper.updateByPrimaryKeySelective(t3);
			if(i!=1){
				throw new RuntimeException("总订单状态更新失败");
			}
			//更新对应子订单状态
			List<TfOrder> orders = tfOrderMapper.findOrdersByTotalNo(totalOrder.gettOrderNo());
			if(orders==null||orders.size()<1){
				log.error("对应的子订单不存在,totoalId="+totalOrder.getTotalId());
				throw new RuntimeException("子订单数据出错");
			}
			for(TfOrder o:orders){
				TfOrder order = new TfOrder();
				order.setOrderId(o.getOrderId());
				order.setoPayStatus(TfOrder.orderStatusPaidDone);
				order.setoStatus(TfOrder.payStatusPaidDone);
				order.setoPayWay(payWay);
				order.setoPayTime(new Date());
				order.setoUpdateTime(new Date());
				int j = tfOrderMapper.updateByPrimaryKeySelective(order);
				if(j!=1){
					throw new RuntimeException("子订单状态更新失败");
				}
			}
			return i;
		}
		
	}

	@Override
	public List<TfOrderTotalHelper> selectProductOrderByPage(Integer start, Integer pageSize, TfOrder tfOrder) {
		return tfOrderMapper.selectProductOrderByPage(start,pageSize,tfOrder);
	}
	

	@Override
	@Transactional
	public int cancelUpdateProductTotalOrder(TfOrderTotal tfOrderTotal,Integer cancelType) {
		//更新子总订单和子订单状态
		List<TfOrder> l1 = tfOrderMapper.findOrdersByTotalNo(tfOrderTotal.gettOrderNo());
		for(TfOrder o:l1){
			TfOrder order = new TfOrder();
			order.setOrderId(o.getOrderId());
			order.setoStatus(TfOrder.orderStatusRebackCancel);
			order.setoCancelType(cancelType);
			order.setoUpdateTime(new Date());
			order.setoCancelTime(new Date());
			order.setoPayStatus(TfOrder.payStatusCancel);
	        int i = tfOrderMapper.updateByPrimaryKeySelective(order);
			if(i!=1){
				throw new RuntimeException("订单更新失败,orderId"+o.getOrderId());
			}
			//恢复产品库存
				//查询商品
			TfProduct p = tfProductMapper.selectByPrimaryKey(o.getoProductId());
			if(p==null){
				log.error("对应商品信息不存在,orderId="+o.getOrderId());
				throw new RuntimeException("对应商品信息不存在");
			}
			TfProduct product = new TfProduct();
			product.setProductId(p.getProductId());
			product.setpTotal(p.getpTotal()+o.getoQty());//更新库存数
			product.setpUpdateTime(new Date());
			//更新库存
			int n = tfProductMapper.updateByPrimaryKeySelective(product);
			if(n!=1){
				log.error("商品库存信息更新失败,orderId="+o.getOrderId()+";productId="+p.getProductId());
				throw new RuntimeException("商品库存信息更新失败");
			}
		}
		TfOrderTotal total = new TfOrderTotal();
		total.setTotalId(tfOrderTotal.getTotalId());
		total.settStatus(TfOrder.orderStatusRebackCancel);
		int j = tfOrderTotalMapper.updateByPrimaryKeySelective(total);
		//通知用户端，已取消订单
		return j;
	}

	@Override
	@Transactional
	public int cancelProductOrder(TfOrderTotalHelper tfOrderTotal,Integer cancelType) {
		//判断支付订单是子总订单还是父总订单
		//退款操作
		List<TfOrder> orders = tfOrderTotal.getOrderList();
		if(orders==null||orders.size()<1){
			return -4;//对应商品订单不存在
		}
		int payType = orders.get(0).getoPayWay();
		String orderNo = "";
		String outRequestNo = null;
		String totalFee = tfOrderTotal.gettAmount()+"";//支付总金额
		String refundFee = tfOrderTotal.gettAmount()+""; //退款金额
		if(!tfOrderTotal.gettOrderNo().equals(tfOrderTotal.gettPayNo())){//父总订单为支付订单
			TfOrderTotal parentTotal = tfOrderTotalMapper.findOrderTotalByNO(tfOrderTotal.gettPayNo());
			if(parentTotal==null){
				return -3;//父总订单不存在
			}
			orderNo = parentTotal.gettOrderNo();
			outRequestNo = tfOrderTotal.gettOrderNo();
			totalFee = parentTotal.gettAmount()+"";
		}else{//子总订单为支付订单
			orderNo = tfOrderTotal.gettOrderNo();
			outRequestNo = tfOrderTotal.gettOrderNo();
		}
		if (TfOrder.payWayWeChat==payType){//微信支付
            //商品退款，获取总订单编号,占卜订单
            try { 
                Map map = WeChatPayUtils.orderrefund(orderNo,totalFee,refundFee,WeChat.weixinAPPPayType);
                if("FAIL".equals(map.get("return_code"))){
                    log.error("======退款操作失败！======"+map.get("return_msg")+map.get("err_code_des"));
                    throw new RuntimeException("退款操作失败"+map.get("return_msg")+map.get("err_code_des"));
                }
            } catch (Exception e) {
                log.error(e);
                e.printStackTrace();
                throw new RuntimeException("退款失败！",e);
            }
        }else if (TfOrder.payWayAlipay==payType){//支付宝支付
            //商品退款、占卜订单
            try {
                AlipayTradeRefundResponse refundResponse = AlipayUtil.refund(orderNo,outRequestNo,(double)tfOrderTotal.gettAmount()/100,"退款原因");
                if(!refundResponse.isSuccess()){
                    log.error(refundResponse.getBody());
                    throw new RuntimeException("退款失败！"+refundResponse.getBody());
                }
            } catch (AlipayApiException e) {
                log.error(e);
                e.printStackTrace();
                throw new RuntimeException("退款失败！",e);
            }
        }
		//更新订单状态和子总订单状态
		TfOrderTotal t = new TfOrderTotal();
		t.setTotalId(tfOrderTotal.getTotalId());
		t.settStatus(TfOrder.orderStatusRebackMoneyDone);
		int i = tfOrderTotalMapper.updateByPrimaryKeySelective(t);
		for(TfOrder o:orders){
			TfOrder o1 = new TfOrder();
			o1.setOrderId(o.getOrderId());
			o1.setoPayStatus(TfOrder.payStatusRebackMoneyDone);
			o1.setoStatus(TfOrder.orderStatusRebackMoneyDone);
			o1.setoUpdateTime(new Date());
			o1.setoCancelType(cancelType);//用户取消
			o1.setoCancelTime(new Date());
			int j = tfOrderMapper.updateByPrimaryKeySelective(o1);
			if(j!=1){
				log.error("订单退款信息更新失败");
				throw new RuntimeException("订单退款信息更新失败");
			}
			//恢复产品库存         
			//查询商品
			TfProduct p = tfProductMapper.selectByPrimaryKey(o.getoProductId());
			if(p==null){
				log.error("对应商品信息不存在,orderId="+o.getOrderId());
				throw new RuntimeException("对应商品信息不存在");
			}
			TfProduct product = new TfProduct();
			product.setProductId(p.getProductId());
			product.setpTotal(p.getpTotal()+o.getoQty());//更新库存数
			product.setpUpdateTime(new Date());
			//更新库存
			int n = tfProductMapper.updateByPrimaryKeySelective(product);
			if(n!=1){
				log.error("商品库存信息更新失败,orderId="+o.getOrderId()+";productId="+p.getProductId());
				throw new RuntimeException("商品库存信息更新失败");
			}
		}
		
        String upushCode = "";
        String upushMsg = "";
        String mpushCode = "";
        String mpushMsg = "";
       //用户取消
        upushCode = PushCode.UOrderCancelByUser;
        upushMsg = PushCode.UOrderCancelByUserMsg;
        mpushCode = PushCode.MOrderCancelByUser;
        mpushMsg = PushCode.MOrderCancelByUserMsg;
        //通知用户退款成功
        Message message = new Message();
        try {
            JSONObject jsonObject = new JSONObject();
            JsonConfig config = new JsonConfig();
            config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
            JSONObject res = new JSONObject();
            TfOrderTotal ot1 = (TfOrderTotal)tfOrderTotal;
//            order1.setTfUser(null);
            JSONObject or = JSONObject.fromObject(ot1, config);
            res.put("tfOrderTotal",or);
            res.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            jsonObject.put("code", upushCode);
            jsonObject.put("msg",upushMsg);
            jsonObject.put("data",res);
            jsonObject = JSONObject.fromObject(jsonObject, config);
            TfUser user = orders.get(0).getTfUser();
            TfMaster master = orders.get(0).getTfMaster();
            //用户
            System.out.println("取消商品订单推送给用户信息:"+jsonObject.toString());
            CodeSuccessResult r1 = message.publishSystem("admin",new String[]{user.getuIm()},new TxtMessage(jsonObject.toString(),""),upushMsg,jsonObject.toString(), 1, 1);
            //CodeSuccessResult r1 = message.publishPrivate("admin",new String[]{order.getTfUser().getuIm()},new TxtMessage(jsonObject.toString(),""),upushMsg,jsonObject.toString(),"1",1,1,1,1);
            if (r1.getCode()!=200){
                log.error("取消订单通知用户失败！"+r1);
                throw new RuntimeException("取消订单通知用户失败");
            }

            //大师
            jsonObject.put("code", mpushCode);
            jsonObject.put("msg",mpushMsg);
            System.out.println("取消商品订单推送给大师信息:"+jsonObject.toString());
            CodeSuccessResult r2 = message.publishSystem("admin",new String[]{master.getmIm()},new TxtMessage(jsonObject.toString(),""),mpushMsg,jsonObject.toString(), 1, 1);
            //CodeSuccessResult r2 = message.publishPrivate("admin",new String[]{order.getTfMaster().getmIm()},new TxtMessage(jsonObject.toString(),""),mpushMsg,jsonObject.toString(),"1",1,1,1,1);
            if (r2.getCode()!=200){
                log.error("取消订单通知大师失败！"+r2);
                throw new RuntimeException("取消订单通知大师失败");
            }
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
        //通知退款大师订单取消
        return i;
	}

	@Override
	public List<TfOrder> findOrderByProductId(TfOrder o, Integer cId) {
		return tfOrderMapper.findOrderByProductId(o,cId);
	}

	@Override
	public List<TfOrder> autoCancleOrder(Date time1, Date time2) {
		// TODO Auto-generated method stub
		return tfOrderMapper.findUnpaidTotalOrder(time1, time2);
	}

	@Override
	public int updateUnpaidTotalOrder(String time1) {
		// TODO Auto-generated method stub
		return tfOrderMapper.updateUnpaidTotalOrder(time1);
	}
}
