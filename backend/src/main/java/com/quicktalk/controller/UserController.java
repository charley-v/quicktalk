package com.quicktalk.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.quicktalk.bean.MessageResponseBean;
import com.quicktalk.bean.RegisterUserRequestBean;
import com.quicktalk.projection.UserProjection;
import com.quicktalk.service.UserService;

@RestController
public class UserController {

	private static final Logger USER_CONTROLLER_LOG = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;
	
	@GetMapping(value = "/users")
	public ResponseEntity<?> getAllUsers() {	
		USER_CONTROLLER_LOG.info("UserController :: in getAllUsers()");
		List<UserProjection> userResp = userService.getAllUsers();
		if(userResp.isEmpty()) {
			USER_CONTROLLER_LOG.info("UserController :: exit getAllUsers()");
			return ResponseEntity.notFound().build();
		}
		else {
			USER_CONTROLLER_LOG.info("UserController :: exit getAllUsers()");
			return ResponseEntity.status(HttpStatus.OK).body(userResp);
		}
	}
	
	@PostMapping(value = "/user")
	public ResponseEntity<MessageResponseBean> registerUser(@RequestBody RegisterUserRequestBean registerUserRequest) {	
		USER_CONTROLLER_LOG.info("UserController :: in registerUser()");
		MessageResponseBean registerUserResp = userService.registerUser(registerUserRequest);
		if(registerUserResp.getMessage().startsWith("S")) {
			USER_CONTROLLER_LOG.info("UserController :: exit registerUser()");
			return ResponseEntity.status(HttpStatus.OK).body(registerUserResp);
		}
		else
		{
			USER_CONTROLLER_LOG.info("UserController :: exit registerUser()");
			if (registerUserResp.getMessage().startsWith("F"))
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(registerUserResp);
			else
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registerUserResp);
		}
	}
	
}
