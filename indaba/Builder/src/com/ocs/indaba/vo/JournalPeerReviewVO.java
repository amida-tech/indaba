package com.ocs.indaba.vo;

import java.util.Date;


public class JournalPeerReviewVO {
	private UserDisplay reviewer;
	private int id;
	private Integer msgboardId;
	private String opinions = "";
	private Date submitTime;
	
	public UserDisplay getReviewer() {
		return reviewer;
	}

	public void setReviewer(UserDisplay reviewer) {
		this.reviewer = reviewer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getMsgboardId() {
		return msgboardId;
	}

	public void setMsgboardId(Integer msgboardId) {
		this.msgboardId = msgboardId;
	}

	public String getOpinions() {
		return opinions;
	}

	public void setOpinions(String opinions) {
		this.opinions = opinions;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}
}
