package com.yinghai.twentyfour.common.model;

import java.util.Date;

public class TfAddress {
    private Integer addressId;

    private Integer asUserId;

    private String asConsigneeName;

    private String asCountryCode;

    private String asTel;

    private String asDetails;

    private Date asCreateTime;

    private Date asUpdateTime;
    
    private Boolean asDefault;
    
    private String asStreet;
    
    private Boolean asSex;
    
    public Boolean getAsSex() {
		return asSex;
	}

	public void setAsSex(Boolean asSex) {
		this.asSex = asSex;
	}

	public String getAsStreet() {
		return asStreet;
	}

	public void setAsStreet(String asStreet) {
		this.asStreet = asStreet;
	}

	public Boolean getAsDefault() {
		return asDefault;
	}

	public void setAsDefault(Boolean asDefault) {
		this.asDefault = asDefault;
	}

	public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getAsUserId() {
        return asUserId;
    }

    public void setAsUserId(Integer asUserId) {
        this.asUserId = asUserId;
    }

    public String getAsConsigneeName() {
        return asConsigneeName;
    }

    public void setAsConsigneeName(String asConsigneeName) {
        this.asConsigneeName = asConsigneeName == null ? null : asConsigneeName.trim();
    }

    public String getAsCountryCode() {
        return asCountryCode;
    }

    public void setAsCountryCode(String asCountryCode) {
        this.asCountryCode = asCountryCode == null ? null : asCountryCode.trim();
    }

    public String getAsTel() {
        return asTel;
    }

    public void setAsTel(String asTel) {
        this.asTel = asTel == null ? null : asTel.trim();
    }

    public String getAsDetails() {
        return asDetails;
    }

    public void setAsDetails(String asDetails) {
        this.asDetails = asDetails == null ? null : asDetails.trim();
    }

    public Date getAsCreateTime() {
        return asCreateTime;
    }

    public void setAsCreateTime(Date asCreateTime) {
        this.asCreateTime = asCreateTime;
    }

    public Date getAsUpdateTime() {
        return asUpdateTime;
    }

    public void setAsUpdateTime(Date asUpdateTime) {
        this.asUpdateTime = asUpdateTime;
    }
}