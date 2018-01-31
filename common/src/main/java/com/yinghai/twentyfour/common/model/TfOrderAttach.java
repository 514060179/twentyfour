package com.yinghai.twentyfour.common.model;

import java.util.Date;

public class TfOrderAttach {
    private Integer attachId;

    private Integer ahOrderId;

    private String ahName;

    private Boolean ahSex;

    private Date ahBirthday;

    private String ahCountryCode;

    private String ahTel;

    private String ahBirthplace;

    private Date ahCreateTime;

    private String ahDescribe;

    public Integer getAttachId() {
        return attachId;
    }

    public void setAttachId(Integer attachId) {
        this.attachId = attachId;
    }

    public Integer getAhOrderId() {
        return ahOrderId;
    }

    public void setAhOrderId(Integer ahOrderId) {
        this.ahOrderId = ahOrderId;
    }

    public String getAhName() {
        return ahName;
    }

    public void setAhName(String ahName) {
        this.ahName = ahName == null ? null : ahName.trim();
    }

    public Boolean getAhSex() {
        return ahSex;
    }

    public void setAhSex(Boolean ahSex) {
        this.ahSex = ahSex;
    }

    public Date getAhBirthday() {
        return ahBirthday;
    }

    public void setAhBirthday(Date ahBirthday) {
        this.ahBirthday = ahBirthday;
    }

    public String getAhCountryCode() {
        return ahCountryCode;
    }

    public void setAhCountryCode(String ahCountryCode) {
        this.ahCountryCode = ahCountryCode == null ? null : ahCountryCode.trim();
    }

    public String getAhTel() {
        return ahTel;
    }

    public void setAhTel(String ahTel) {
        this.ahTel = ahTel == null ? null : ahTel.trim();
    }

    public String getAhBirthplace() {
        return ahBirthplace;
    }

    public void setAhBirthplace(String ahBirthplace) {
        this.ahBirthplace = ahBirthplace == null ? null : ahBirthplace.trim();
    }

    public Date getAhCreateTime() {
        return ahCreateTime;
    }

    public void setAhCreateTime(Date ahCreateTime) {
        this.ahCreateTime = ahCreateTime;
    }

    public String getAhDescribe() {
        return ahDescribe;
    }

    public void setAhDescribe(String ahDescribe) {
        this.ahDescribe = ahDescribe == null ? null : ahDescribe.trim();
    }
}