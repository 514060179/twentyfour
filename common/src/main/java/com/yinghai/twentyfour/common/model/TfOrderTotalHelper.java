package com.yinghai.twentyfour.common.model;

import java.util.List;

public class TfOrderTotalHelper extends TfOrderTotal {
	private List<TfOrder> orderList;
	private TfAddress address;
	public TfAddress getAddress() {
		return address;
	}

	public void setAddress(TfAddress address) {
		this.address = address;
	}

	public List<TfOrder> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<TfOrder> orderList) {
		this.orderList = orderList;
	}
	
}
