package com.quicktalk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "room_membership")
public class RoomMembership {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_membership_id")
    private Long roomMembershipId;

	@ManyToOne
	@JoinColumn(name = "rm_user_id", referencedColumnName = "user_id", nullable = false)
	private Users user; // The user associated with this membership

	@ManyToOne
	@JoinColumn(name = "rm_room_id", referencedColumnName = "room_id", nullable = false)
	private Room room; // The room associated with this membership
    
    public RoomMembership() {
    }

	public Long getRoomMembershipId() {
		return roomMembershipId;
	}

	public void setRoomMembershipId(Long roomMembershipId) {
		this.roomMembershipId = roomMembershipId;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
 
}
