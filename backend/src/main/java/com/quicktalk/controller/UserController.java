package com.quicktalk.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.quicktalk.bean.MessageResponseBean;
import com.quicktalk.bean.RegisterUserRequestBean;
import com.quicktalk.projection.UserProjection;
import com.quicktalk.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "User Management", tags = {"Users"})
@RestController
public class UserController {

	private static final Logger USER_CONTROLLER_LOG = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;
	
	
	@ApiOperation(value = "Fetches all registered users", notes = "This API returns list of all registered users if found, or a 404 status code if no user is found.", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
		 @ApiResponse(code = 200, message = "Success", response = UserProjection.class),
	     @ApiResponse(code = 404, message = "Not Found")})
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
	
	@ApiOperation(value = "Registers a new user", notes = "This API creates a new user with provided details. The request body must include the user's name, email, and password. Returns userId along with 201 status code on success, 404 status code if registration fails and 400 status code if request validation fails along with message.", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "OK - {\"userId\": \"1\", \"message\": \"Successfully registered johnDoe\"}", response = MessageResponseBean.class),
		    @ApiResponse(code = 404, message = "Not Found - {\"userId\": null, \"message\": \"Failed to register user johnDoe\"}", response = MessageResponseBean.class),
		    @ApiResponse(code = 400, message = "Bad Request - {\"userId\": null, \"message\": \"Password cannot be empty\"}", response = MessageResponseBean.class)
		})
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
