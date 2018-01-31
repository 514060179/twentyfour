package com.yinghai.twentyfour.app.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.yinghai.twentyfour.app.model.TotalOrderParamEntity;
import com.yinghai.twentyfour.app.service.TfOrderTotalService;
import com.yinghai.twentyfour.common.dao.TfCarMapper;
import com.yinghai.twentyfour.common.dao.TfOrderMapper;
import com.yinghai.twentyfour.common.dao.TfOrderTotalMapper;
import com.yinghai.twentyfour.common.dao.TfProductMapper;
import com.yinghai.twentyfour.common.model.TfOrder;
import com.yinghai.twentyfour.common.model.TfOrderTotal;
import com.yinghai.twentyfour.common.model.TfOrderTotalHelper;
import com.yinghai.twentyfour.common.model.TfProduct;

public class TfOrderTotalServiceImpl implements TfOrderTotalService {
	@Autowired
	private TfOrderTotalMapper tfOrderTotalMapper;
	@Autowired
	private TfOrderMapper tfOrderMapper;
	@Autowired
	private TfProductMapper tfProductMapper;
	@Autowired
	private TfCarMapper tfCarMapper;
	@Override
	@Transactional
	public TfOrderTotal generateOrder(Integer userId,Integer addressId, Integer sum,Integer qty, List<TotalOrderParamEntity> list) {
		//生成总订单
		TfOrderTotal total = new TfOrderTotal();
		total.settAmount(sum);
		total.settUserId(userId);
		Date d = new Date();
		total.settCreateTime(d);
		total.settAddressId(addressId);
		total.settStatus(1);//未支付
		total.settQty(qty);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		total.settOrderNo("pt"+sdf.format(d).toString().replace("-", "").replace(":", "").replace(" ", "")+userId+new Random().nextInt(999999)%9000);
		int i = tfOrderTotalMapper.insertSelective(total);
		String totalId = total.gettOrderNo();
		if(i!=1){
			throw new RuntimeException("创建总订单失败");
		}
		//生成条目订单
		for(TotalOrderParamEntity e:list){
			TfOrder order = new TfOrder();
			order.setoMasterId(e.getMid());
			order.setoUserId(userId);
			order.setoProductId(e.getPid());
			order.setoPayStatus(1);//未支付
			order.setoStatus(1);//未支付
			order.setoOrderType(3);//商品订单
			order.setoOrderNo(sdf.format(new Date()).toString().replace("-", "").replace(":", "").replace(" ", "")+userId+new Random().nextInt(999999)%900000);
			order.setoCreateTime(new Date());
			order.setoPrice(e.getPrice());
			order.setoQty(e.getAmount());
			order.setoIsOrderTotal(true);
			order.setoOrderTotalNo(totalId);//总订单id
			order.setoAmount(e.getPrice()*e.getAmount());//总价
			int j = tfOrderMapper.insertSelective(order);//新建条目订单
			//更新商品库存
			TfProduct product = new TfProduct();
			product.setProductId(e.getPid());
			product.setpTotal(e.getStock()-e.getAmount());//更新库存数
			product.setpUpdateTime(new Date());
			//更新库存
			int k = tfProductMapper.updateByPrimaryKeySelective(product);
			//删除对应购物车记录
			int l = tfCarMapper.deleteByPrimaryKey(e.getCarId());
			if(j!=1||k!=1||l!=1){
				throw new RuntimeException("商品子订单创建失败");
			}
		}
		return total;
	}

	@Override
	public TfOrderTotal findByOrderNo(String orderNo,Integer userId) {
		return tfOrderTotalMapper.findByOrderNo(orderNo,userId);
	}

	@Override
	public TfOrderTotalHelper findByUserId(Integer totalId, Integer userId) {
		return tfOrderTotalMapper.findOrderByUserId(totalId,userId);
	}

	@Override
	@Transactional
	public TfOrderTotal createOrder(Integer userId, Integer addressId, Map<Integer, Integer> sum,
			Map<Integer, Integer> qty,Integer sums,Integer qtys, Map<Integer, List<TotalOrderParamEntity>> map) {
		//生成支付订单
		TfOrderTotal payOrder = new TfOrderTotal();
		payOrder.settAmount(sums);
		payOrder.settUserId(userId);
		payOrder.settCreateTime(new Date());
		payOrder.settAddressId(addressId);
		payOrder.settStatus(1);//未支付
		payOrder.settQty(qtys);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		payOrder.settOrderNo("pt"+sdf.format(new Date()).toString().replace("-", "").replace(":", "").replace(" ", "")+userId+new Random().nextInt(999999)%9000);
		int i = tfOrderTotalMapper.insertSelective(payOrder);
		Integer payTotalId = payOrder.getTotalId();//支付订单Id
		String payOrderNo = payOrder.gettOrderNo();//支付订单号
		if(i!=1){
			throw new RuntimeException("生成支付订单出错");
		}
		//按大师分类生成总订单
		int s1 = map.size();
		int s2 = sum.size();
		int s3 = qty.size();
		if(s2!=s1||s3!=s1){
			throw new RuntimeException("参数出错");
		}
		Set<Entry<Integer, List<TotalOrderParamEntity>>> orderEntrySet = map.entrySet();
		Iterator<Entry<Integer, List<TotalOrderParamEntity>>> it1 = orderEntrySet.iterator();
		while(it1.hasNext()){
			TfOrderTotal t = new TfOrderTotal();
			Entry<Integer, List<TotalOrderParamEntity>> orderEntry = it1.next();
			t.settAmount(sum.get(orderEntry.getKey()));
			t.settUserId(userId);
			t.settAddressId(addressId);
			t.settStatus(1);//未支付
			t.settQty(qty.get(orderEntry.getKey()));
			t.settOrderNo("pt"+sdf.format(new Date()).toString().replace("-", "").replace(":", "").replace(" ", "")+userId+new Random().nextInt(999999)%9000);
			t.settCreateTime(new Date());
			t.settParentId(payTotalId);
			t.settPayNo(payOrderNo);
			t.settMasterId(orderEntry.getKey());
			int j = tfOrderTotalMapper.insertSelective(t);
			Integer totalId = t.getTotalId();//对应大师总订单Id
			String orderNo = t.gettOrderNo();//对应大师总订单号
			//生成条目订单
			List<TotalOrderParamEntity> orderList = orderEntry.getValue();
			for(TotalOrderParamEntity e:orderList){
				TfOrder order = new TfOrder();
				order.setoMasterId(e.getMid());
				order.setoUserId(userId);
				order.setoProductId(e.getPid());
				order.setoPayStatus(1);//未支付
				order.setoStatus(1);//未支付
				order.setoOrderType(3);//商品订单
				order.setoOrderNo(sdf.format(new Date()).toString().replace("-", "").replace(":", "").replace(" ", "")+userId+new Random().nextInt(999999)%900000);
				order.setoCreateTime(new Date());
				order.setoPrice(e.getPrice());
				order.setoQty(e.getAmount());
				order.setoIsOrderTotal(true);
				order.setoOrderTotalNo(orderNo);//总订单号
				order.setoAmount(e.getPrice()*e.getAmount());//总价
				int k = tfOrderMapper.insertSelective(order);//新建条目订单
				//更新商品库存
				TfProduct product = new TfProduct();
				product.setProductId(e.getPid());
				product.setpTotal(e.getStock()-e.getAmount());//更新库存数
				product.setpUpdateTime(new Date());
				//更新库存
				int n = tfProductMapper.updateByPrimaryKeySelective(product);
				//删除对应购物车记录
				int l = tfCarMapper.deleteByPrimaryKey(e.getCarId());
				if(k!=1||n!=1||l!=1){
					throw new RuntimeException("商品子订单创建失败");
				}
			}
			
		}
		return payOrder;
	}

	@Override
	public List<TfOrderTotal> findByPayOrderNo(String orderNo) {
		return tfOrderTotalMapper.findByPayOrderNo(orderNo);
	}


}
