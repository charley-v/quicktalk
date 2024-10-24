package com.quicktalk.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Request model for user registration")
public class RegisterUserRequestBean {

	@ApiModelProperty(value = "Indicates name of the user", required=true, example="johnDoe")
    private String username;
    
	@ApiModelProperty(value = "Indicates email ID of the user", required=true, example="john@abc.com")
	private String email;
	
	@ApiModelProperty(value = "Indicates user password", required=true, example="qwerty123")
    private String password;
    
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "RegisterUserRequestBean [username=" + username + ", email=" + email + ", password=" + password + "]";
	}

}
