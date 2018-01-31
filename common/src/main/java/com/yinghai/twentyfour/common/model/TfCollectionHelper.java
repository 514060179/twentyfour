package com.yinghai.twentyfour.common.model;
/**
 * 我的收藏组合查询实体类
 * @author Administrator
 *
 */
public class TfCollectionHelper extends TfCollection {
	private TfMaster master;
	private TfArticle article;
	private TfProduct product;
	public TfMaster getMaster() {
		return master;
	}
	public void setMaster(TfMaster master) {
		this.master = master;
	}
	public TfArticle getArticle() {
		return article;
	}
	public void setArticle(TfArticle article) {
		this.article = article;
	}
	public TfProduct getProduct() {
		return product;
	}
	public void setProduct(TfProduct product) {
		this.product = product;
	}
	
}
