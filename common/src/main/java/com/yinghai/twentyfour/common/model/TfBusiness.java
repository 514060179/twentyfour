package com.yinghai.twentyfour.common.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.beans.factory.annotation.Autowired;

import com.yinghai.twentyfour.common.service.TfBusinessTypeService;
import com.yinghai.twentyfour.common.util.SpringContextUtils;

public class TfBusiness {
    private Integer businessId;

    private Integer bMasterId;

    private String bName;

    private Integer bPrice;

    private Long bDeals;

    private Date bCreateTime;

    private Date bUpdateTime;

    private String bIntroduction;
    
    private String bType;
    
    private String[] bTypeArray;
    
    private String bTypeName;
    
    public String getbTypeName() {
    	String s = bType;
    	if(s==null){
    		return "";
    	}
    	String[] array = s.split(",");
    	StringBuffer name = new StringBuffer();
    	EhCacheManager cacheManager = (EhCacheManager)SpringContextUtils.getBeanById("cacheManager");
    	if(cacheManager==null){
    		return "";
    	}
    	Cache<String, Object> cache = cacheManager.getCache("typeNameMap");
    	if(cache==null){
    		return "";
    	}
    	Map<String,String> map = (Map<String, String>) cache.get("typeMap");
    	for(int i=0;i<array.length;i++){
    		if(map.get(array[i])!=null){
    			name.append(map.get(array[i]));
    			if(i!=array.length-1){
    				name.append(",");
    			}
    		}else{
    			return "";
    		}
    	}
    	if(name.length()==0){
    		return "";
    	}
		return name.toString();
	}

	public void setbTypeName(String bTypeName) {
		this.bTypeName = bTypeName;
	}

	public String[] getbTypeArray() {
		return bTypeArray;
	}

	public void setbTypeArray(String[] bTypeArray) {
		this.bTypeArray = bTypeArray;
	}

	public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getbMasterId() {
        return bMasterId;
    }

    public void setbMasterId(Integer bMasterId) {
        this.bMasterId = bMasterId;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName == null ? null : bName.trim();
    }

    public Integer getbPrice() {
        return bPrice;
    }

    public void setbPrice(Integer bPrice) {
        this.bPrice = bPrice;
    }

    public Long getbDeals() {
        return bDeals;
    }

    public void setbDeals(Long bDeals) {
        this.bDeals = bDeals;
    }

    public Date getbCreateTime() {
        return bCreateTime;
    }

    public void setbCreateTime(Date bCreateTime) {
        this.bCreateTime = bCreateTime;
    }

    public Date getbUpdateTime() {
        return bUpdateTime;
    }

    public void setbUpdateTime(Date bUpdateTime) {
        this.bUpdateTime = bUpdateTime;
    }

    public String getbIntroduction() {
        return bIntroduction;
    }

    public void setbIntroduction(String bIntroduction) {
        this.bIntroduction = bIntroduction == null ? null : bIntroduction.trim();
    }

	public String getbType() {
		return bType;
	}

	public void setbType(String bType) {
		this.bType = bType;
	}
}