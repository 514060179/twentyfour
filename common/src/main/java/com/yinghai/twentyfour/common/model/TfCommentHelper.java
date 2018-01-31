package com.yinghai.twentyfour.common.model;

public class TfCommentHelper extends TfComment {
	private TfUser tfUser;
	private TfArticle tfArticle;
	private TfProduct tfProduct;
	private TfMaster tfMaster;
	public TfUser getTfUser() {
		return tfUser;
	}
	public void setTfUser(TfUser tfUser) {
		this.tfUser = tfUser;
	}
	public TfArticle getTfArticle() {
		return tfArticle;
	}
	public void setTfArticle(TfArticle tfArticle) {
		this.tfArticle = tfArticle;
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
}
