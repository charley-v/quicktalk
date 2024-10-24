package com.quicktalk.projection;

import com.quicktalk.entity.RoomType;

import io.swagger.annotations.ApiModelProperty;

public interface RoomProjection {

	@ApiModelProperty(value = "Unique identifier of the room", required=true, example = "1")
	Long getRoomId();
	
	@ApiModelProperty(value = "Indicates name of the room", required=true, example="John-Jane-Richard Group Chat")
	String getRoomName();
	
	@ApiModelProperty(value = "Indicates type of room. Possible values: single or group", required=true, example="group")
	RoomType getRoomType();
	
}
