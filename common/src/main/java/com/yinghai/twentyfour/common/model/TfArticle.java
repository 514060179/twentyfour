package com.yinghai.twentyfour.common.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;

import com.yinghai.twentyfour.common.util.SpringContextUtils;

public class TfArticle {
	public static final int type_product=1;
	public static final int type_article=1;
    private Integer articleId;

    private Integer aMasterId;

    private String aType;
    
    private String aTitle;

    private Date aPublishDate;

    private Date aAbortDate;

    private String aImg;

    private String aUrl;

    private Date aCreateTime;

    private Date aUpdateTime;

    private String aContent;
    
    private Long aReadAmount;
    
    private Boolean isCollection;
    
    private Integer queryUserId;
    
    private Boolean aDelete;
    
    private List<TfImgTmp> imgList;
    
    private TfMaster tfMaster;
    
    private String[] aTypeArray;
    
    private String aTypeName;
    
    public String getaTypeName() {
    	String s = aType;
    	if(s==null){
    		return "";
    	}
    	String[] array = s.split(",");
    	StringBuffer name = new StringBuffer();
    	EhCacheManager cacheManager = (EhCacheManager)SpringContextUtils.getBeanById("cacheManager");
    	Cache<String, Object> cache = cacheManager.getCache("typeNameMap");
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

	public void setaTypeName(String aTypeName) {
		this.aTypeName = aTypeName;
	}

	public String[] getaTypeArray() {
		return aTypeArray;
	}

	public void setaTypeArray(String[] aTypeArray) {
		this.aTypeArray = aTypeArray;
	}

	public Long getaReadAmount() {
		return aReadAmount;
	}

	public void setaReadAmount(Long aReadAmount) {
		this.aReadAmount = aReadAmount;
	}

	public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getaMasterId() {
        return aMasterId;
    }

    public void setaMasterId(Integer aMasterId) {
        this.aMasterId = aMasterId;
    }

    public String getaType() {
        return aType;
    }

    public void setaType(String aType) {
        this.aType = aType == null ? null : aType.trim();
    }

    public Date getaPublishDate() {
        return aPublishDate;
    }

    public void setaPublishDate(Date aPublishDate) {
        this.aPublishDate = aPublishDate;
    }

    public Date getaAbortDate() {
        return aAbortDate;
    }

    public void setaAbortDate(Date aAbortDate) {
        this.aAbortDate = aAbortDate;
    }

    public String getaImg() {
        return aImg;
    }

    public void setaImg(String aImg) {
        this.aImg = aImg == null ? null : aImg.trim();
    }

    public String getaUrl() {
        return aUrl;
    }

    public void setaUrl(String aUrl) {
        this.aUrl = aUrl == null ? null : aUrl.trim();
    }

    public Date getaCreateTime() {
        return aCreateTime;
    }

    public void setaCreateTime(Date aCreateTime) {
        this.aCreateTime = aCreateTime;
    }

    public Date getaUpdateTime() {
        return aUpdateTime;
    }

    public void setaUpdateTime(Date aUpdateTime) {
        this.aUpdateTime = aUpdateTime;
    }

    public String getaContent() {
        return aContent;
    }

    public void setaContent(String aContent) {
        this.aContent = aContent == null ? null : aContent.trim();
    }

	public String getaTitle() {
		return aTitle;
	}

	public void setaTitle(String aTitle) {
		this.aTitle = aTitle;
	}

	public Boolean getIsCollection() {
		return isCollection;
	}

	public void setIsCollection(Boolean isCollection) {
		this.isCollection = isCollection;
	}

	public Integer getQueryUserId() {
		return queryUserId;
	}

	public void setQueryUserId(Integer queryUserId) {
		this.queryUserId = queryUserId;
	}

	public List<TfImgTmp> getImgList() {
		return imgList;
	}

	public void setImgList(List<TfImgTmp> imgList) {
		this.imgList = imgList;
	}

	public Boolean getaDelete() {
		return aDelete;
	}

	public void setaDelete(Boolean aDelete) {
		this.aDelete = aDelete;
	}

	public TfMaster getTfMaster() {
		return tfMaster;
	}

	public void setTfMaster(TfMaster tfMaster) {
		this.tfMaster = tfMaster;
	}
}