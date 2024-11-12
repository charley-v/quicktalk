package com.quicktalk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quicktalk.bean.AddUsersToGroupRoomRequestBean;
import com.quicktalk.bean.GroupRoomRequestBean;
import com.quicktalk.bean.PrivateRoomRequestBean;
import com.quicktalk.bean.RoomResponseBean;
import com.quicktalk.entity.Room;
import com.quicktalk.entity.RoomMembership;
import com.quicktalk.entity.RoomType;
import com.quicktalk.entity.Users;
import com.quicktalk.projection.RoomProjection;
import com.quicktalk.repository.RoomMembershipRepository;
import com.quicktalk.repository.RoomRepository;
import com.quicktalk.repository.UserRepository;
import com.quicktalk.utilities.Utility;

@Service("RoomServiceImpl")
public class RoomServiceImpl implements RoomService{
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoomRepository roomRepo;
	
	@Autowired
	RoomMembershipRepository roomMembershipRepo;
	
	private static final Logger ROOM_SERVICE_LOG = LoggerFactory.getLogger(RoomServiceImpl.class);

	//all database operations within the method are treated as a single transaction. If any operation (like saving the room or adding the user to room_membership) fails, the entire transaction is rolled back.
	@Transactional
	public RoomResponseBean getOrCreatePrivateRoom(PrivateRoomRequestBean privateRoomRequest) {

		ROOM_SERVICE_LOG.info("RoomServiceImpl :: in getOrCreatePrivateRoom() :: privateRoomRequest {}",privateRoomRequest.toString());
		RoomResponseBean roomIdResp = new RoomResponseBean();
		String errorMessage = validatePrivateRoomReq(privateRoomRequest);
		if (errorMessage.equals("")) {
			try {
				Integer userId1 = Integer.parseInt(privateRoomRequest.getFirstUserId());
				Integer userId2 = Integer.parseInt(privateRoomRequest.getSecondUserId());
				
				//Check whether users are present in DB or not
				//List<Users> users = userRepo.findAllById(List.of(userId1, userId2));
				List<Users> users = userRepo.findAllById(new ArrayList<>(Arrays.asList(userId1, userId2)));
		        if (users.size() != 2) {
		            throw new IllegalArgumentException("Failed Room Request. One or both users not found");
		        }
				
				//Check private room already exits
				Room room = roomRepo.findPrivateRoom(userId1,userId2).orElse(null);
				
				// If no room exists, create a new one
		        if (room == null) {
		        	ROOM_SERVICE_LOG.info("RoomServiceImpl :: in getOrCreatePrivateRoom() :: Creating Private Room as No Room Id exists");
		        	String roomName = "Chat with UserIds " +userId1+" "+userId2;
		            room = createRoom(false, roomName); // false for private room
//		            Room savedRoom = roomRepo.saveAndFlush(room);
//		            roomIdResp.setRoomId(savedRoom.getRoomId().toString());
		            roomRepo.saveAndFlush(room);
		            roomIdResp.setRoomId(room.getRoomId().toString());
		            ROOM_SERVICE_LOG.info("RoomServiceImpl :: in getOrCreatePrivateRoom() :: New Private Room Created :: roomIdResp :: {}", roomIdResp.toString());
		            addUsersToRoomMembership(users, room, false);		            
		        }	
		        else {
		        	roomIdResp.setRoomId(room.getRoomId().toString());
		        	ROOM_SERVICE_LOG.info("RoomServiceImpl :: in getOrCreatePrivateRoom() :: Room Id already exists :: roomIdResp :: {}"+roomIdResp.toString());
		        }		        	     
			} 
			catch (IllegalArgumentException e) {
				ROOM_SERVICE_LOG.info("RoomServiceImpl :: Failed Private Room Request :: One or both users does not exist :: exit getOrCreatePrivateRoom()");
//				roomIdResp.setRoomId("");
				roomIdResp.setMessage(e.getMessage());
            }		
			catch (Exception e) {
				ROOM_SERVICE_LOG.info("RoomServiceImpl :: Exception in getOrCreatePrivateRoom :: {}", e);
				roomIdResp.setRoomId(null);
				roomIdResp.setMessage("Failed to get or create private room");
			}
		} else {
			ROOM_SERVICE_LOG.info("RoomServiceImpl :: Request validation failed :: getOrCreatePrivateRoom()");
			roomIdResp.setMessage(errorMessage);
		}
		ROOM_SERVICE_LOG.info("RoomServiceImpl :: exit getOrCreatePrivateRoom() :: roomIdResp :: {}"+roomIdResp.toString());
		return roomIdResp;
	}

	private String validatePrivateRoomReq(PrivateRoomRequestBean privateRoomRequest) {

		String error = "";
		ROOM_SERVICE_LOG.info("RoomServiceImpl :: in validatePrivateRoomReq()");
		if (Utility.isNull(privateRoomRequest.getFirstUserId()))
			error = "First UserId cannot be empty";
		else if (Utility.isNull(privateRoomRequest.getSecondUserId()))
			error = "Second UserId cannot be empty";

		ROOM_SERVICE_LOG.info("RoomServiceImpl :: exit validatePrivateRoomReq() :: error {}", error);
		return error;
	}
	
	private Room createRoom(boolean isGroupChat, String roomName) {
		ROOM_SERVICE_LOG.info("RoomServiceImpl :: in createRoom()");
        Room room = new Room();
        room.setRoomType(isGroupChat ? RoomType.group : RoomType.single);
        room.setRoomName(roomName);
        ROOM_SERVICE_LOG.info("RoomServiceImpl :: exit createRoom()");
        return room;
    }
	
//	private void addUsersToRoomMembership(List<Users> users, Room room) {
//		ROOM_SERVICE_LOG.info("RoomServiceImpl :: in addUsersToRoomMembership() :: Adding userIds {} in room membership",users.toString());
//
//		for (Users user : users) {
//            RoomMembership roomMembership = new RoomMembership();
//            roomMembership.setRoom(room);
//            roomMembership.setUser(user);
////          roomMembershipRepo.saveAndFlush(roomMembership);
//            roomMembershipRepo.save(roomMembership);
//            ROOM_SERVICE_LOG.info("RoomServiceImpl :: exit addUsersToRoomMembership() :: Added userId {} in room membership",user.getUserId());
//		}
//		ROOM_SERVICE_LOG.info("RoomServiceImpl :: exit addUsersToRoomMembership()");
//    }

	private void addUsersToRoomMembership(List<Users> users, Room room, boolean isExistingGroup) {
		ROOM_SERVICE_LOG.info("RoomServiceImpl :: in addUsersToRoomMembership() :: Adding userIds {} in room membership",users.toString());

		for (Users user : users) {
			//For create New group request
			if(!isExistingGroup) {
				RoomMembership roomMembership = new RoomMembership();
				roomMembership.setRoom(room);
				roomMembership.setUser(user);
				roomMembershipRepo.saveAndFlush(roomMembership);
//				roomMembershipRepo.save(roomMembership);
				ROOM_SERVICE_LOG.info("RoomServiceImpl :: in addUsersToRoomMembership() :: Added userId {} in room membership",user.getUserId());
			}
			//For existing group request
			else {
				// Check if the user is already a member of the group room
				RoomMembership existingMembership = roomMembershipRepo.findByUserAndRoom(user.getUserId(), room.getRoomId());
				
				if (existingMembership == null) {
					RoomMembership roomMembership = new RoomMembership();
					roomMembership.setUser(user);
					roomMembership.setRoom(room);
					roomMembershipRepo.saveAndFlush(roomMembership);
					ROOM_SERVICE_LOG.info("RoomServiceImpl :: in addUsersToRoomMembership() :: Added userId {} in room membership",user.getUserId());
				}
				else {
					ROOM_SERVICE_LOG.info("RoomServiceImpl :: in addUsersToRoomMembership() :: Already Existing userId {} in room membership",user.getUserId());
				}
			}
		}
		ROOM_SERVICE_LOG.info("RoomServiceImpl :: exit addUsersToRoomMembership()");
	}
	
	//Method for finding or creating a group room if it does not exists
	@Transactional
	public RoomResponseBean getOrCreateGroupRoom(GroupRoomRequestBean groomRoomRequest) {

		ROOM_SERVICE_LOG.info("RoomServiceImpl :: in getOrCreateGroupRoom() :: groomRoomRequest {}",groomRoomRequest.toString());
		RoomResponseBean roomIdResp = new RoomResponseBean();
		String errorMessage = validateGroupRoomReq(groomRoomRequest);
		if (errorMessage.equals("")) {
			try {
				
				List<Integer> userIdsList = groomRoomRequest.getUserIdList();
				//Check whether users are present in DB or not
				List<Users> users = userRepo.findAllById(userIdsList);
		        if (users.size() != userIdsList.size()) {
		            throw new IllegalArgumentException("Failed Room Request. One or multiple users not found");
		        }
				
				//Check group room already exits
				Room room = roomRepo.findGroupRoomByUserIds(userIdsList,userIdsList.size()).orElse(null);
				
				// If no room exists, create a new one
		        if (room == null) {
		        	ROOM_SERVICE_LOG.info("RoomServiceImpl :: in getOrCreateGroupRoom() :: Creating Group Room as No Room Id exists");
		        	String roomName = groomRoomRequest.getGroupRoomName();
		            room = createRoom(true, roomName); // true for group room
//		            Room savedRoom = roomRepo.saveAndFlush(room);
//		            roomIdResp.setRoomId(savedRoom.getRoomId().toString());
		            roomRepo.saveAndFlush(room);
		            roomIdResp.setRoomId(room.getRoomId().toString());
		            ROOM_SERVICE_LOG.info("RoomServiceImpl :: in getOrCreateGroupRoom() :: New Group Room Created :: roomIdResp :: {}", roomIdResp.toString());
		            addUsersToRoomMembership(users, room, false);		            
		        }	
		        else {
		        	roomIdResp.setRoomId(room.getRoomId().toString());
		        	ROOM_SERVICE_LOG.info("RoomServiceImpl :: in getOrCreateGroupRoom() :: Group Room Id already exists :: roomIdResp :: {}"+roomIdResp.toString());
		        }		        	     
			} 
			catch (IllegalArgumentException e) {
				ROOM_SERVICE_LOG.info("RoomServiceImpl :: Failed Group Room Request :: One or multiple users does not exist :: exit getOrCreateGroupRoom()");
//				roomIdResp.setRoomId("");
				roomIdResp.setMessage(e.getMessage());
            }		
			catch (Exception e) {
				ROOM_SERVICE_LOG.info("RoomServiceImpl :: Exception in getOrCreateGroupRoom :: {}", e);
				roomIdResp.setRoomId(null);
				roomIdResp.setMessage("Failed to get or create group room");
			}
		} else {
			ROOM_SERVICE_LOG.info("RoomServiceImpl :: Request validation failed :: getOrCreateGroupRoom()");
			roomIdResp.setMessage(errorMessage);
		}
		ROOM_SERVICE_LOG.info("RoomServiceImpl :: exit getOrCreateGroupRoom() :: roomIdResp :: {}"+roomIdResp.toString());
		return roomIdResp;
	}
	
	private String validateGroupRoomReq(GroupRoomRequestBean groomRoomRequest) {

		String error = "";
		ROOM_SERVICE_LOG.info("RoomServiceImpl :: in validateGroupRoomReq()");
		if (groomRoomRequest.getUserIdList().isEmpty())
			error = "User Id List cannot be empty";
		else if (Utility.isNull(groomRoomRequest.getGroupRoomName()))
			error = "Group Room Name cannot be empty";

		ROOM_SERVICE_LOG.info("RoomServiceImpl :: exit validateGroupRoomReq() :: error {}", error);
		return error;
	}
	
	//Method for finding or adding user/users to an existing group room
	@Transactional
	public RoomResponseBean addUsersToGroupRoom(AddUsersToGroupRoomRequestBean addUserToGroupRequest) {

		ROOM_SERVICE_LOG.info("RoomServiceImpl :: in addUsersToGroupRoom() :: addUserToGroupRequest {}",
				addUserToGroupRequest.toString());
		RoomResponseBean roomIdResp = new RoomResponseBean();
		String errorMessage = validateAddUsersToGroupReq(addUserToGroupRequest);
		if (errorMessage.equals("")) {
			try {

				Long groupRoomId = addUserToGroupRequest.getGroupRoomId();
				// Check whether roomId exists
				Optional<Room> groupRoomOpt = roomRepo.findGroupRoomById(groupRoomId);
				if (!groupRoomOpt.isPresent()) {
					ROOM_SERVICE_LOG
							.info("RoomServiceImpl :: in addUsersToGroupRoom() :: Group Room Id does not exist");
					throw new IllegalArgumentException("Failed Add Users Request. Group room Id not found.");
				}

				List<Integer> userIdsList = addUserToGroupRequest.getUserIdList();
				// Check whether users are present in DB or not
				List<Users> users = userRepo.findAllById(userIdsList);
				if (users.size() != userIdsList.size()) {
					ROOM_SERVICE_LOG.info(
							"RoomServiceImpl :: in addUsersToGroupRoom() :: One or multiple users does not exist");
					throw new IllegalArgumentException("Failed Add Users Request. One or multiple users not found");
				}

				Room groupRoom = groupRoomOpt.get();
				roomIdResp.setRoomId(groupRoom.getRoomId().toString());
				roomIdResp.setMessage("Successfully added userIds "+userIdsList+" to groupRoomId "+groupRoomId);
				addUsersToRoomMembership(users, groupRoom, true);

			} catch (IllegalArgumentException e) {
				ROOM_SERVICE_LOG.info(
						"RoomServiceImpl :: IllegalArgumentException :: Failed Group Room Request :: exit addUsersToGroupRoom()");
//					roomIdResp.setRoomId("");
				roomIdResp.setMessage(e.getMessage());
			} catch (Exception e) {
				ROOM_SERVICE_LOG.info("RoomServiceImpl :: Exception in addUsersToGroupRoom :: {}", e);
				roomIdResp.setRoomId(null);
				roomIdResp.setMessage("Failed to add users to group room Id");
			}
		} else {
			ROOM_SERVICE_LOG.info("RoomServiceImpl :: Request validation failed :: addUsersToGroupRoom()");
			roomIdResp.setMessage(errorMessage);
		}
		ROOM_SERVICE_LOG
				.info("RoomServiceImpl :: exit addUsersToGroupRoom() :: roomIdResp :: {}" + roomIdResp.toString());
		return roomIdResp;
	}

	private String validateAddUsersToGroupReq(AddUsersToGroupRoomRequestBean addUserToGroupRequest) {

		String error = "";
		ROOM_SERVICE_LOG.info("RoomServiceImpl :: in validateAddUsersToGroupReq()");
		if (addUserToGroupRequest.getUserIdList().isEmpty())
			error = "User Id List cannot be empty";
		else if (Utility.isNull(addUserToGroupRequest.getGroupRoomId().toString()))
			error = "Group Room Id cannot be empty";

		ROOM_SERVICE_LOG.info("RoomServiceImpl :: exit validateAddUsersToGroupReq() :: error {}", error);
		return error;
	}
		
	public List<RoomProjection> getAllRoomsByUserId(Integer userId) {

		List<RoomProjection> roomsByUserId = new ArrayList<>();
		ROOM_SERVICE_LOG.info("RoomServiceImpl :: in getAllRoomsByUserId()");
		try {
			roomsByUserId = roomRepo.findAllRoomsByUserId(userId);
		} catch (Exception e) {
			ROOM_SERVICE_LOG.info("RoomServiceImpl :: Exception in getAllRoomsByUserId :: {}", e);
		}
		ROOM_SERVICE_LOG.info("RoomServiceImpl :: exit getAllRoomsByUserId()");
		return roomsByUserId;
	}

}
