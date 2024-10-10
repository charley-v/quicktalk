package com.quicktalk.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

@RestController
public class RoomController {

	private static final Logger ROOM_CONTROLLER_LOG = LoggerFactory.getLogger(RoomController.class);
	
	@Autowired
	RoomService roomService;
	
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
	
	@GetMapping(value = "/rooms/user/{userId}")
	public ResponseEntity<?> getAllRoomsByUserId(@PathVariable Integer userId) {	
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
