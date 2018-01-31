package com.yinghai.twentyfour.common.model;

import java.util.Date;

public class TfCollection {
	public static final int cn_type_master=1;
	public static final int cn_type_article=2;
	public static final int cn_type_product=3;
    private Integer collectionId;

    private Integer cnUserId;

    private Integer cnProductId;

    private Integer cnArticleId;

    private Integer cnMasterId;

    private Date cnCreateTime;

    private Integer cnType;

    public Integer getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Integer collectionId) {
        this.collectionId = collectionId;
    }

    public Integer getCnUserId() {
        return cnUserId;
    }

    public void setCnUserId(Integer cnUserId) {
        this.cnUserId = cnUserId;
    }

    public Integer getCnProductId() {
        return cnProductId;
    }

    public void setCnProductId(Integer cnProductId) {
        this.cnProductId = cnProductId;
    }

    public Integer getCnArticleId() {
        return cnArticleId;
    }

    public void setCnArticleId(Integer cnArticleId) {
        this.cnArticleId = cnArticleId;
    }

    public Integer getCnMasterId() {
        return cnMasterId;
    }

    public void setCnMasterId(Integer cnMasterId) {
        this.cnMasterId = cnMasterId;
    }

    public Date getCnCreateTime() {
        return cnCreateTime;
    }

    public void setCnCreateTime(Date cnCreateTime) {
        this.cnCreateTime = cnCreateTime;
    }

    public Integer getCnType() {
        return cnType;
    }

    public void setCnType(Integer cnType) {
        this.cnType = cnType;
    }
}