package com.quicktalk.bean;

import java.util.List;

public class GroupRoomRequestBean {

    private List<Integer> userIdList;
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
