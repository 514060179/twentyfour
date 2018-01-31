package com.yinghai.twentyfour.common.model;

import java.util.Date;

public class TfOrderTotal {
    private Integer totalId;

    private String tOrderNo;

    private Integer tAmount;

    private Date tCreateTime;

    private Integer tUserId;
    
    private Integer tAddressId;
    
    private Integer tStatus;
    
    private Integer tQty;
    
    private Integer tParentId;
    
    private String tPayNo;
    
    private Integer tMasterId;

    private String tExpressNo;
    
    private Integer tExpressCompanyId;
    
    private String tExpressCompany;
    
    public Integer gettMasterId() {
		return tMasterId;
	}

	public void settMasterId(Integer tMasterId) {
		this.tMasterId = tMasterId;
	}

	public Integer gettParentId() {
		return tParentId;
	}

	public void settParentId(Integer tParentId) {
		this.tParentId = tParentId;
	}

	public String gettPayNo() {
		return tPayNo;
	}

	public void settPayNo(String tPayNo) {
		this.tPayNo = tPayNo;
	}
    
    public String gettExpressNo() {
		return tExpressNo;
	}

	public void settExpressNo(String tExpressNo) {
		this.tExpressNo = tExpressNo;
	}

	public Integer gettExpressCompanyId() {
		return tExpressCompanyId;
	}

	public void settExpressCompanyId(Integer tExpressCompanyId) {
		this.tExpressCompanyId = tExpressCompanyId;
	}

	public String gettExpressCompany() {
		return tExpressCompany;
	}

	public void settExpressCompany(String tExpressCompany) {
		this.tExpressCompany = tExpressCompany;
	}
	public Integer gettQty() {
		return tQty;
	}

	public void settQty(Integer tQty) {
		this.tQty = tQty;
	}

	public Integer gettStatus() {
		return tStatus;
	}

	public void settStatus(Integer tStatus) {
		this.tStatus = tStatus;
	}

	public Integer gettAddressId() {
		return tAddressId;
	}

	public void settAddressId(Integer tAddressId) {
		this.tAddressId = tAddressId;
	}

	public Integer getTotalId() {
        return totalId;
    }

    public void setTotalId(Integer totalId) {
        this.totalId = totalId;
    }

    public String gettOrderNo() {
        return tOrderNo;
    }

    public void settOrderNo(String tOrderNo) {
        this.tOrderNo = tOrderNo == null ? null : tOrderNo.trim();
    }

    public Integer gettAmount() {
        return tAmount;
    }

    public void settAmount(Integer tAmount) {
        this.tAmount = tAmount;
    }

    public Date gettCreateTime() {
        return tCreateTime;
    }

    public void settCreateTime(Date tCreateTime) {
        this.tCreateTime = tCreateTime;
    }

    public Integer gettUserId() {
        return tUserId;
    }

    public void settUserId(Integer tUserId) {
        this.tUserId = tUserId;
    }
    
  
}