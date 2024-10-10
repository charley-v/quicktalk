package com.quicktalk.bean;

import java.util.List;

public class AddUsersToGroupRoomRequestBean {

    private List<Integer> userIdList;
    private Long groupRoomId;
    
	public List<Integer> getUserIdList() {
		return userIdList;
	}
	public void setUserIdList(List<Integer> userIdList) {
		this.userIdList = userIdList;
	}
	public Long getGroupRoomId() {
		return groupRoomId;
	}
	public void setGroupRoomId(Long groupId) {
		this.groupRoomId = groupId;
	}
	
	@Override
	public String toString() {
		return "AddUsersToGroupRoomRequestBean [userIdList=" + userIdList + ", groupId=" + groupRoomId + "]";
	}        

}
