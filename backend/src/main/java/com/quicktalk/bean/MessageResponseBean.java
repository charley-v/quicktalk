package com.quicktalk.bean;

import javax.xml.bind.annotation.XmlElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Represents user registration ID & corresponding message")
public class MessageResponseBean {

	@ApiModelProperty(value = "Indicates user ID of the registered user. Can be null if registartion fails", example="1")
	private String userId;
	
	@ApiModelProperty(value = "Indicates message regarding user registration request", required=true, example="Successfully registered John Doe")
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
