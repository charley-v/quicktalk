package com.quicktalk.bean;

import javax.xml.bind.annotation.XmlElement;

import io.swagger.annotations.ApiModelProperty;

public class RoomResponseBean {

	@ApiModelProperty(value = "Indicates room ID. Can be null if request fails", example="1")
	private String roomId;
	
	@ApiModelProperty(value = "Indicates message regarding chat room request.", example="Successfully added userIds [4, 5] to groupRoomId 1")
	private String message;

	public String getRoomId() {
		return roomId;
	}

	@XmlElement(name = "roomId")
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
	public String getMessage() {
		return message;
	}

	@XmlElement(name = "message")
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "RoomResponseBean [roomId=" + roomId + ", message=" + message + "]";
	}

}
