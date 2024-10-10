package com.quicktalk.bean;

import javax.xml.bind.annotation.XmlElement;

public class MessageResponseBean {

	private String userId;
	private String message;

	public String getUserId() {
		return userId;
	}

	@XmlElement(name = "userId")
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	@XmlElement(name = "message")
	public void setMessage(String message) {
		this.message = message;
	}

}
