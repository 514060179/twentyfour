package com.yinghai.twentyfour.common.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;

import com.yinghai.twentyfour.common.util.SpringContextUtils;

public class TfProduct {
    private Integer productId;

    private Integer pMasterId;

    private String pName;

    private Integer pPrice;

    private Integer pTotal;

    private String pSize;

    private String pImg;

    private Long pDeals;

    private String pAttribution;

    private Boolean pFreeShipping;

    private Long pHot;

    private Boolean pOffline;

    private Boolean pDelete;

    private Date pCreateTime;

    private Date pUpdateTime;

    private String pIntroduction;
    
    private Boolean isCollection;
    
    private List<TfImgTmp> imgList;
    
    private Integer userId;
    
    private String pType;
    
    private String[] pTypeArray;
    
    private String pTypeName;
    
    public String getpTypeName() {
    	String s = pType;
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

	public void setpTypeName(String pTypeName) {
		this.pTypeName = pTypeName;
	}

	public String[] getpTypeArray() {
		return pTypeArray;
	}

	public void setpTypeArray(String[] pTypeArray) {
		this.pTypeArray = pTypeArray;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getpMasterId() {
        return pMasterId;
    }

    public void setpMasterId(Integer pMasterId) {
        this.pMasterId = pMasterId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName == null ? null : pName.trim();
    }

    public Integer getpPrice() {
        return pPrice;
    }

    public void setpPrice(Integer pPrice) {
        this.pPrice = pPrice;
    }

    public Integer getpTotal() {
        return pTotal;
    }

    public void setpTotal(Integer pTotal) {
        this.pTotal = pTotal;
    }

    public String getpSize() {
        return pSize;
    }

    public void setpSize(String pSize) {
        this.pSize = pSize == null ? null : pSize.trim();
    }

    public String getpImg() {
        return pImg;
    }

    public void setpImg(String pImg) {
        this.pImg = pImg == null ? null : pImg.trim();
    }

    public Long getpDeals() {
        return pDeals;
    }

    public void setpDeals(Long pDeals) {
        this.pDeals = pDeals;
    }

    public String getpAttribution() {
        return pAttribution;
    }

    public void setpAttribution(String pAttribution) {
        this.pAttribution = pAttribution == null ? null : pAttribution.trim();
    }

    public Boolean getpFreeShipping() {
        return pFreeShipping;
    }

    public void setpFreeShipping(Boolean pFreeShipping) {
        this.pFreeShipping = pFreeShipping;
    }

    public Long getpHot() {
        return pHot;
    }

    public void setpHot(Long pHot) {
        this.pHot = pHot;
    }

    public Boolean getpOffline() {
        return pOffline;
    }

    public void setpOffline(Boolean pOffline) {
        this.pOffline = pOffline;
    }

    public Boolean getpDelete() {
        return pDelete;
    }

    public void setpDelete(Boolean pDelete) {
        this.pDelete = pDelete;
    }

    public Date getpCreateTime() {
        return pCreateTime;
    }

    public void setpCreateTime(Date pCreateTime) {
        this.pCreateTime = pCreateTime;
    }

    public Date getpUpdateTime() {
        return pUpdateTime;
    }

    public void setpUpdateTime(Date pUpdateTime) {
        this.pUpdateTime = pUpdateTime;
    }

    public String getpIntroduction() {
        return pIntroduction;
    }

    public void setpIntroduction(String pIntroduction) {
        this.pIntroduction = pIntroduction == null ? null : pIntroduction.trim();
    }

	public List<TfImgTmp> getImgList() {
		return imgList;
	}

	public void setImgList(List<TfImgTmp> imgList) {
		this.imgList = imgList;
	}

	public Boolean getIsCollection() {
		return isCollection;
	}

	public void setIsCollection(Boolean isCollection) {
		this.isCollection = isCollection;
	}

	public String getpType() {
		return pType;
	}

	public void setpType(String pType) {
		this.pType = pType;
	}

}