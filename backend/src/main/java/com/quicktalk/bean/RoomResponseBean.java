package com.quicktalk.bean;

import javax.xml.bind.annotation.XmlElement;

public class RoomResponseBean {

	private String roomId;
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
