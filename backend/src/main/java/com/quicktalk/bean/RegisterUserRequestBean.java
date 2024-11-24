package com.quicktalk.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Request model for user registration")
public class RegisterUserRequestBean {
	
	@ApiModelProperty(value = "Indicates ID token of the user", required=true, example="vvwygwTTTviviiqiHI8HB")
	private String idToken;
    
	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

	@Override
	public String toString() {
		return "RegisterUserRequestBean [idToken=" + idToken + "]";
	}

}
