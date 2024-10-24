package com.quicktalk.bean;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Request model for adding users to a group chat room")
public class AddUsersToGroupRoomRequestBean {

	@ApiModelProperty(value = "List of one or more user IDs to be added in an existing group", required=true, example="[1,2,3]")
    private List<Integer> userIdList;
	
	@ApiModelProperty(value = "Room ID of an existing group", required=true, example="1")
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
