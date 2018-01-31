package com.yinghai.twentyfour.common.model;

import java.util.Date;

public class TfBusinessType {
    private Integer businessTypeId;

    private String typeName;

    private String typeDescription;

    private Date typeCreateTime;

    private Date typeUpdateTime;

    public Integer getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Integer businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription == null ? null : typeDescription.trim();
    }

    public Date getTypeCreateTime() {
        return typeCreateTime;
    }

    public void setTypeCreateTime(Date typeCreateTime) {
        this.typeCreateTime = typeCreateTime;
    }

    public Date getTypeUpdateTime() {
        return typeUpdateTime;
    }

    public void setTypeUpdateTime(Date typeUpdateTime) {
        this.typeUpdateTime = typeUpdateTime;
    }
}