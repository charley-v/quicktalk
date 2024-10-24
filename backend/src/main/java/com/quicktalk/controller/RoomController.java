package com.quicktalk.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.quicktalk.bean.AddUsersToGroupRoomRequestBean;
import com.quicktalk.bean.GroupRoomRequestBean;
import com.quicktalk.bean.PrivateRoomRequestBean;
import com.quicktalk.bean.RoomResponseBean;
import com.quicktalk.projection.RoomProjection;
import com.quicktalk.service.RoomService;
import com.quicktalk.utilities.Utility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Room Management", tags = {"Room"})
@RestController
public class RoomController {

	private static final Logger ROOM_CONTROLLER_LOG = LoggerFactory.getLogger(RoomController.class);
	
	@Autowired
	RoomService roomService;
	
	
	@ApiOperation(value = "Create or fetch a private chat room by user IDs", notes = "This API creates a new private chat room for the specified user IDs if it does not already exist, or fetches the existing room if it does. Returns a private room ID and message along with 200 status code on success, 404 status code if room creation/fetching fails and 400 status code if request validation fails.", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "OK - {\"roomId\": \"1\", \"message\": \"null\"}", response = RoomResponseBean.class),
		    @ApiResponse(code = 404, message = "Not Found - {\"roomId\": null, \"message\": \"Failed to get or create private room\"}", response = RoomResponseBean.class),
		    @ApiResponse(code = 400, message = "Bad Request - {\"roomId\": null, \"message\": \"First UserId cannot be empty\"}", response = RoomResponseBean.class)
		})
	@PostMapping(value = "/rooms/private")
	public ResponseEntity<RoomResponseBean> getOrCreatePrivateRoom(@RequestBody PrivateRoomRequestBean privateRoomRequest) {	
		ROOM_CONTROLLER_LOG.info("RoomController :: in getOrCreatePrivateRoom()");
		RoomResponseBean privateRoomResp = roomService.getOrCreatePrivateRoom(privateRoomRequest);
		
		if(!Utility.isNull(privateRoomResp.getRoomId())) {
			ROOM_CONTROLLER_LOG.info("RoomController :: exit getOrCreatePrivateRoom()");
			return ResponseEntity.status(HttpStatus.OK).body(privateRoomResp);
		}
		else
		{
			ROOM_CONTROLLER_LOG.info("RoomController :: exit getOrCreatePrivateRoom()");
			if (privateRoomResp.getMessage().startsWith("Failed"))
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(privateRoomResp);
			else
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(privateRoomResp);
		}
		
//		if(privateRoomResp.getRoomId().equals("0")) {
//		ROOM_CONTROLLER_LOG.info("RoomController :: exit getOrCreatePrivateRoom()");
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//	}
//	else {
//		ROOM_CONTROLLER_LOG.info("RoomController :: exit getOrCreatePrivateRoom()");
//		return ResponseEntity.status(HttpStatus.OK).body(privateRoomResp);
//	}
	
	}
	
	
	@ApiOperation(value = "Create or fetch a group chat room by user IDs", notes = "This API creates a new group chat room for the specified user IDs if it does not already exist, or fetches the existing room if it does. Returns a group room ID and message along with 200 status code on success, 404 status code if room creation/fetching fails and 400 status code if request validation fails.", response = RoomResponseBean.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "OK - {\"roomId\": \"1\", \"message\": \"null\"}", response = RoomResponseBean.class),
		    @ApiResponse(code = 404, message = "Not Found - {\"roomId\": null, \"message\": \"Failed to get or create group room\"}", response = RoomResponseBean.class),
		    @ApiResponse(code = 400, message = "Bad Request - {\"roomId\": null, \"message\": \"User Id List cannot be empty\"}", response = RoomResponseBean.class)
		})
	@PostMapping(value = "/rooms/group")
	public ResponseEntity<RoomResponseBean> getOrCreateGroupRoom(@RequestBody GroupRoomRequestBean groupRoomRequest) {	
		ROOM_CONTROLLER_LOG.info("RoomController :: in getOrCreateGroupRoom()");
		RoomResponseBean privateRoomResp = roomService.getOrCreateGroupRoom(groupRoomRequest);
		
		if(!Utility.isNull(privateRoomResp.getRoomId())) {
			ROOM_CONTROLLER_LOG.info("RoomController :: exit getOrCreateGroupRoom()");
			return ResponseEntity.status(HttpStatus.OK).body(privateRoomResp);
		}
		else
		{
			ROOM_CONTROLLER_LOG.info("RoomController :: exit getOrCreateGroupRoom()");
			if (privateRoomResp.getMessage().startsWith("Failed"))
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(privateRoomResp);
			else
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(privateRoomResp);
		}
	}
	
	
	@ApiOperation(value = "Add users to a group chat room with the specified group room ID", notes = "This API is used to add a single or list of user IDs to an existing group room ID. Returns the group room ID and message along with 200 status code on success, 404 status code if user addition to group room fails and 400 status code if request validation fails.", response = RoomResponseBean.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "OK - {\"roomId\": \"1\", \"message\": \"Successfully added userIds [4, 5] to groupRoomId 1\"}", response = RoomResponseBean.class),
		    @ApiResponse(code = 404, message = "Not Found - {\"roomId\": null, \"message\": \"Failed to add users to group room Id\"}", response = RoomResponseBean.class),
		    @ApiResponse(code = 400, message = "Bad Request - {\"roomId\": null, \"message\": \"User Id List cannot be empty\"}", response = RoomResponseBean.class)
		})
	@PostMapping(value = "/group/users")
	public ResponseEntity<RoomResponseBean> addUsersToGroupRoom(@RequestBody AddUsersToGroupRoomRequestBean addUserToGroupRequest) {	
		ROOM_CONTROLLER_LOG.info("RoomController :: in addUsersToGroupRoom()");
		RoomResponseBean privateRoomResp = roomService.addUsersToGroupRoom(addUserToGroupRequest);
		
		if(!Utility.isNull(privateRoomResp.getRoomId())) {
			ROOM_CONTROLLER_LOG.info("RoomController :: exit addUsersToGroupRoom()");
			return ResponseEntity.status(HttpStatus.OK).body(privateRoomResp);
		}
		else
		{
			ROOM_CONTROLLER_LOG.info("RoomController :: exit addUsersToGroupRoom()");
			if (privateRoomResp.getMessage().startsWith("Failed"))
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(privateRoomResp);
			else
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(privateRoomResp);
		}
	}
	
	
	@ApiOperation(value = "Fetches all rooms by user ID", notes = "This API returns list of all rooms of the requested user ID if found, or a 404 status code if no rooms found.", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			 @ApiResponse(code = 200, message = "Success", response = RoomProjection.class),
		     @ApiResponse(code = 404, message = "Not Found")})
	@GetMapping(value = "/rooms/user/{userId}")
	public ResponseEntity<?> getAllRoomsByUserId(
			@ApiParam(value="ID value for the user whose rooms details you need to retrieve", required=true)
			@PathVariable Integer userId) {	
		ROOM_CONTROLLER_LOG.info("RoomController :: in getAllRoomsByUserId()");
		List<RoomProjection> roomsResp = roomService.getAllRoomsByUserId(userId);
		
		if(roomsResp.isEmpty()) {
			ROOM_CONTROLLER_LOG.info("RoomController :: exit getAllRoomsByUserId()");
			return ResponseEntity.notFound().build();
		}
		else {
			ROOM_CONTROLLER_LOG.info("RoomController :: exit getAllRoomsByUserId()");
			return ResponseEntity.status(HttpStatus.OK).body(roomsResp);
		}
	}	
	
}
