package com.quicktalk.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class Users {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	@Column(name = "user_name", nullable = false, unique = true)
    private String username;

    @Column(name = "user_email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "user_password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")  // Indicates that this User can have multiple RoomMemberships
    private Set<RoomMembership> roomMemberships;
    
    public Users() {
    }
    
	public Users(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<RoomMembership> getRoomMemberships() {
		return roomMemberships;
	}

	public void setRoomMemberships(Set<RoomMembership> roomMemberships) {
		this.roomMemberships = roomMemberships;
	}

	@Override
	public String toString() {
		return "Users [userId=" + userId + ", username=" + username + ", email=" + email + "]";
	}
       
}
