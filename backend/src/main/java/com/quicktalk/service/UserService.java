package com.quicktalk.service;

import java.util.List;

import com.quicktalk.bean.LoginRequestBean;
import com.quicktalk.bean.MessageResponseBean;
import com.quicktalk.bean.RegisterUserRequestBean;
import com.quicktalk.projection.UserProjection;

public interface UserService {

	List<UserProjection> getAllUsers();
	MessageResponseBean registerUser(RegisterUserRequestBean registerUserRequest);
	MessageResponseBean loginUser(LoginRequestBean loginRequest);
}
