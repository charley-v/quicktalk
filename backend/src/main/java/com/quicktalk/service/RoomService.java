package com.quicktalk.service;

import java.util.List;

import com.quicktalk.bean.AddUsersToGroupRoomRequestBean;
import com.quicktalk.bean.GroupRoomRequestBean;
import com.quicktalk.bean.PrivateRoomRequestBean;
import com.quicktalk.bean.RoomResponseBean;
import com.quicktalk.projection.RoomProjection;

public interface RoomService {

	RoomResponseBean getOrCreatePrivateRoom(PrivateRoomRequestBean privateRoomRequest);
	RoomResponseBean getOrCreateGroupRoom(GroupRoomRequestBean groupRoomRequest);
	RoomResponseBean addUsersToGroupRoom(AddUsersToGroupRoomRequestBean addUserToGroupRequest);
	List<RoomProjection> getAllRoomsByUserId(Integer userId);

}
