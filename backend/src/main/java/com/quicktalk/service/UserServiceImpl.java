package com.quicktalk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;

import com.quicktalk.bean.LoginRequestBean;
import com.quicktalk.bean.MessageResponseBean;
import com.quicktalk.bean.RegisterUserRequestBean;
import com.quicktalk.entity.Users;
import com.quicktalk.projection.UserProjection;
import com.quicktalk.repository.UserRepository;
import com.quicktalk.utilities.Utility;


@Service("UserServiceImpl")
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserRepository userRepo;
	
	private static final Logger USER_SERVICE_LOG = LoggerFactory.getLogger(UserServiceImpl.class);

	public List<UserProjection> getAllUsers() {
		
		List<UserProjection> users = new ArrayList<>();
		USER_SERVICE_LOG.info("UserServiceImpl :: in getAllUsers()");
		try {
			users = userRepo.findAllProjectedBy();
		} catch (Exception e) {
			USER_SERVICE_LOG.info("UserServiceImpl :: Exception in getAllUsers :: {}",e);
		}
		USER_SERVICE_LOG.info("UserServiceImpl :: exit getAllUsers()");
		return users;
	}
	
	public MessageResponseBean registerUser(RegisterUserRequestBean registerUserRequest) {
		
		USER_SERVICE_LOG.info("UserServiceImpl :: in registerUser() :: registerUserRequest {}",registerUserRequest.toString());
		MessageResponseBean userResp = new MessageResponseBean();
		String errorMessage = validateRegisterUserReq(registerUserRequest);
		if (errorMessage.equals("")) {
			try {
				Users newUser = new Users(registerUserRequest.getUsername(), registerUserRequest.getEmail(),
						registerUserRequest.getPassword());
	
				Users registerUserResp = userRepo.saveAndFlush(newUser);
				userResp.setUserId(registerUserResp.getUserId().toString());
				userResp.setMessage("Successfully registered "+registerUserResp.getUsername());
				USER_SERVICE_LOG.info("UserServiceImpl :: User registered successfully :: exit registerUser()");
			} catch(Exception e) {
				USER_SERVICE_LOG.info("UserServiceImpl :: Exception in registerUser :: {}",e);
				userResp.setMessage("Failed to register user "+registerUserRequest.getUsername());
				USER_SERVICE_LOG.info("UserServiceImpl :: User registeration failed :: exit registerUser()");
			}
		}
		else {
			USER_SERVICE_LOG.info("UserServiceImpl :: Request validation failed :: exit registerUser()");
			userResp.setMessage(errorMessage);
		}
		return userResp;
	}

	private String validateRegisterUserReq(RegisterUserRequestBean registerUserRequest) {
		
		String error = "";
		USER_SERVICE_LOG.info("UserServiceImpl :: in validateRegisterUserReq()");
		if(Utility.isNull(registerUserRequest.getUsername()))
			error = "Username cannot be empty";
		else if(Utility.isNull(registerUserRequest.getEmail()))
			error = "Email cannot be empty";
		else if(Utility.isNull(registerUserRequest.getPassword()))
			error ="Password cannot be empty";
	
		USER_SERVICE_LOG.info("UserServiceImpl :: exit validateRegisterUserReq() :: error {}",error);
		return error;
	}
	@Override
    public MessageResponseBean loginUser(LoginRequestBean loginRequest) {
		String email = loginRequest.getEmail();
		String password = loginRequest.getPassword();
	
		Optional<Users> userOpt = userRepo.findByEmail(email);
	
		if (userOpt.isPresent()) {
			Users user = userOpt.get();
			
			// Check the password
			if (password.equals(user.getPassword())) {
				return new MessageResponseBean(user.getUserId().toString(), "Successfully logged in", user.getUsername()); // Include username
			} else {
				return new MessageResponseBean(null, "Invalid credentials", null); // Invalid password
			}
		} else {
			return new MessageResponseBean(null, "Invalid credentials", null); // User not found
		}
	}
}