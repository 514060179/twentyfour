package com.yinghai.twentyfour.common.model;

import java.util.Date;

public class TfComment {
    private Integer commentId;

    private Integer ctArticleId;

    private Integer ctProductId;

    private Integer ctDiscussantId;

    private Date ctCreateTime;

    private Integer ctType;

    private String ctContent;
    
    private Boolean ctInvisible;
    
	public Boolean getCtInvisible() {
		return ctInvisible;
	}

	public void setCtInvisible(Boolean ctInvisible) {
		this.ctInvisible = ctInvisible;
	}

	public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getCtArticleId() {
        return ctArticleId;
    }

    public void setCtArticleId(Integer ctArticleId) {
        this.ctArticleId = ctArticleId;
    }

    public Integer getCtProductId() {
        return ctProductId;
    }

    public void setCtProductId(Integer ctProductId) {
        this.ctProductId = ctProductId;
    }

    public Integer getCtDiscussantId() {
        return ctDiscussantId;
    }

    public void setCtDiscussantId(Integer ctDiscussantId) {
        this.ctDiscussantId = ctDiscussantId;
    }

    public Date getCtCreateTime() {
        return ctCreateTime;
    }

    public void setCtCreateTime(Date ctCreateTime) {
        this.ctCreateTime = ctCreateTime;
    }

    public Integer getCtType() {
        return ctType;
    }

    public void setCtType(Integer ctType) {
        this.ctType = ctType;
    }

    public String getCtContent() {
        return ctContent;
    }

    public void setCtContent(String ctContent) {
        this.ctContent = ctContent == null ? null : ctContent.trim();
    }

	@Override
	public String toString() {
		return "TfComment [commentId=" + commentId + ", ctArticleId=" + ctArticleId + ", ctProductId=" + ctProductId
				+ ", ctDiscussantId=" + ctDiscussantId + ", ctCreateTime=" + ctCreateTime + ", ctType=" + ctType
				+ ", ctContent=" + ctContent + ", ctInvisible=" + ctInvisible + "]";
	}
}