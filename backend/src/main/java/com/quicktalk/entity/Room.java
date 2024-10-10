package com.quicktalk.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "room")
public class Room {

	@Id
	@Column(name = "room_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roomId;

	@Column(name = "room_name")
    private String roomName;

	@Enumerated(EnumType.STRING)
	@Column(name = "room_type", columnDefinition = "enum('single','group')")
	private RoomType roomType;
    
	@OneToMany(mappedBy = "room") 
    private Set<RoomMembership> roomMemberships; // Set of memberships for the room
	
    public Room() {
    }

	public Room(Long roomId, String roomName, RoomType roomType) {
		this.roomId = roomId;
		this.roomName = roomName;
		this.roomType = roomType;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public Set<RoomMembership> getRoomMemberships() {
		return roomMemberships;
	}

	public void setRoomMemberships(Set<RoomMembership> roomMemberships) {
		this.roomMemberships = roomMemberships;
	}
 
}
