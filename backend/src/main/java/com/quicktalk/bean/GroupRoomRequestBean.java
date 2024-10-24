package com.quicktalk.bean;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Request model for creating/fetching group chat room")
public class GroupRoomRequestBean {

	@ApiModelProperty(value = "List of user IDs for the group room", required=true, example="[1,2,3,4]")
    private List<Integer> userIdList;
	
	@ApiModelProperty(value = "Indicates name of the group room", required=true, example="John-Jane-Richard Group Chat")
    private String groupRoomName;
    
	public List<Integer> getUserIdList() {
		return userIdList;
	}
	public void setUserIdList(List<Integer> userIdList) {
		this.userIdList = userIdList;
	}
	public String getGroupRoomName() {
		return groupRoomName;
	}
	public void setGroupRoomName(String groupRoomName) {
		this.groupRoomName = groupRoomName;
	}
	@Override
	public String toString() {
		return "GroupRoomRequestBean [userIdList=" + userIdList + ", groupRoomName=" + groupRoomName + "]";
	}
    

}
