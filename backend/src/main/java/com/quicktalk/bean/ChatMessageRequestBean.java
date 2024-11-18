package com.quicktalk.bean;

public class ChatMessageRequestBean {
    private Integer senderUserId;
    private Long roomId;
    private String message;
    
	public Integer getSenderUserId() {
		return senderUserId;
	}
	public void setSenderUserId(Integer senderUserId) {
		this.senderUserId = senderUserId;
	}
	
	public Long getRoomId() {
		return roomId;
	}
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
