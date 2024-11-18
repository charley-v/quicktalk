package com.quicktalk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "message")
public class Message {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "m_user_id", referencedColumnName = "user_id", nullable = false)
//	private Users user; // The user associated with this membership
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "rm_room_id", referencedColumnName = "room_id", nullable = false)
//	private Room room; // The room associated with this membership
	
	@Column(name = "m_user_id", nullable = false)
    private Integer userId;
	
    
	@Column(name = "m_room_id", nullable = false)
    private Long roomId;
	
	@Column(name = "m_text", nullable = false)
    private String text;
	
    public Message() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
 
}
