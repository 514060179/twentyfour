package com.yinghai.twentyfour.common.model;

import java.util.Date;

public class TfImClient {
    private Integer accid;

    private Integer icId;

    private String icName;

    private String icToken;

    private String icEmail;

    private Date icBirth;

    private Boolean icGender;

    private Integer icType;

    private Date icCreateTime;

    private Date icUpdateTime;

    public Integer getAccid() {
        return accid;
    }

    public void setAccid(Integer accid) {
        this.accid = accid;
    }

    public Integer getIcId() {
        return icId;
    }

    public void setIcId(Integer icId) {
        this.icId = icId;
    }

    public String getIcName() {
        return icName;
    }

    public void setIcName(String icName) {
        this.icName = icName == null ? null : icName.trim();
    }

    public String getIcToken() {
        return icToken;
    }

    public void setIcToken(String icToken) {
        this.icToken = icToken == null ? null : icToken.trim();
    }

    public String getIcEmail() {
        return icEmail;
    }

    public void setIcEmail(String icEmail) {
        this.icEmail = icEmail == null ? null : icEmail.trim();
    }

    public Date getIcBirth() {
        return icBirth;
    }

    public void setIcBirth(Date icBirth) {
        this.icBirth = icBirth;
    }

    public Boolean getIcGender() {
        return icGender;
    }

    public void setIcGender(Boolean icGender) {
        this.icGender = icGender;
    }

    public Integer getIcType() {
        return icType;
    }

    public void setIcType(Integer icType) {
        this.icType = icType;
    }

    public Date getIcCreateTime() {
        return icCreateTime;
    }

    public void setIcCreateTime(Date icCreateTime) {
        this.icCreateTime = icCreateTime;
    }

    public Date getIcUpdateTime() {
        return icUpdateTime;
    }

    public void setIcUpdateTime(Date icUpdateTime) {
        this.icUpdateTime = icUpdateTime;
    }
}