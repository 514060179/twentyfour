package com.yinghai.twentyfour.common.model;

import java.io.Serializable;
import java.util.Date;

public class VersionControl implements Serializable {
    private Integer id;

    private String versionId;

    private Boolean forceUpdate;

    private String deviceType;

    private String realm;

    private Date createTime;

    private Date updateTime;

    private Integer priority;

    private String describe;

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId == null ? null : versionId.trim();
    }

    public Boolean getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(Boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType == null ? null : deviceType.trim();
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm == null ? null : realm.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "VersionControl{" +
                "id=" + id +
                ", versionId='" + versionId + '\'' +
                ", forceUpdate=" + forceUpdate +
                ", deviceType='" + deviceType + '\'' +
                ", realm='" + realm + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", priority=" + priority +
                '}';
    }
}