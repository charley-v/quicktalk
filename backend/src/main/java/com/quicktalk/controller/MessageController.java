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
import org.springframework.web.bind.annotation.RestController;

import com.quicktalk.entity.Message;
import com.quicktalk.service.MessageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Message Management", tags = {"Message"})
@RestController
public class MessageController {

	private static final Logger MESSAGE_CONTROLLER_LOG = LoggerFactory.getLogger(MessageController.class);
	
	@Autowired
	MessageService messageService;
	
	@ApiOperation(value = "Fetches chat history by room ID", notes = "This API returns list of all messages from the requested room ID if found, or a 404 status code if no messages of room exist.", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			 @ApiResponse(code = 200, message = "Success", response = Message.class),
		     @ApiResponse(code = 404, message = "Not Found")})
	@GetMapping(value = "/messages/room/{roomId}")
	public ResponseEntity<?> getMessagesByRoomId(
			@ApiParam(value="ID value for the room whose messages you need to retrieve", required=true)
			@PathVariable Long roomId) {	
		MESSAGE_CONTROLLER_LOG.info("MessageController :: in getMessagesByRoomId()");
		List<Message> roomsResp = messageService.getMessagesByRoomId(roomId);
		
		if(roomsResp.isEmpty()) {
			MESSAGE_CONTROLLER_LOG.info("MessageController :: exit getMessagesByRoomId()");
			return ResponseEntity.notFound().build();
		}
		else {
			MESSAGE_CONTROLLER_LOG.info("MessageController :: exit getMessagesByRoomId()");
			return ResponseEntity.status(HttpStatus.OK).body(roomsResp);
		}
	}	
	
}
