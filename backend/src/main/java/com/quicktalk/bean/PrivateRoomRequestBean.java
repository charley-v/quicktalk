package com.quicktalk.bean;

public class PrivateRoomRequestBean {

    private String firstUserId;
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
