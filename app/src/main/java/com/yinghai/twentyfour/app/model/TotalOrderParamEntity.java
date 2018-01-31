package com.yinghai.twentyfour.app.model;

public class TotalOrderParamEntity {
	private Integer carId;//购物车记录id
	private Integer mid;//购物车记录对应大师id
	private Integer pid;//购物车记录对应产品id
	private Integer price;//购物车记录对应商品单价
	private Integer amount;//购物车记录对应订单数量
	private Integer stock;//购物车记录对应商品库存
	public Integer getCarId() {
		return carId;
	}
	public void setCarId(Integer carId) {
		this.carId = carId;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getMid() {
		return mid;
	}
	public void setMid(Integer mid) {
		this.mid = mid;
	}
}
