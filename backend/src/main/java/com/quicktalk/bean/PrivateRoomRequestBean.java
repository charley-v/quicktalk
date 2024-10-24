package com.quicktalk.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Request model for creating/fetching private chat room")
public class PrivateRoomRequestBean {

	@ApiModelProperty(value = "Details of first user ID of private chat", required=true, example="1")
    private String firstUserId;
	
	@ApiModelProperty(value = "Details of second user ID of private chat", required=true, example="2")
    private String secondUserId;
    
	public String getFirstUserId() {
		return firstUserId;
	}
	public void setFirstUserId(String firstUserId) {
		this.firstUserId = firstUserId;
	}
	public String getSecondUserId() {
		return secondUserId;
	}
	public void setSecondUserId(String secondUserId) {
		this.secondUserId = secondUserId;
	}
	@Override
	public String toString() {
		return "PrivateRoomRequestBean [firstUserId=" + firstUserId + ", secondUserId=" + secondUserId + "]";
	}

}
