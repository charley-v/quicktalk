package com.quicktalk.projection;

import com.quicktalk.entity.RoomType;

public interface RoomProjection {

	Long getRoomId();
	String getRoomName();
	RoomType getRoomType();
	
}
