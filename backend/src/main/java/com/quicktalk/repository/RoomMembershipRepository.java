package com.quicktalk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.quicktalk.entity.RoomMembership;

@Repository
public interface RoomMembershipRepository extends JpaRepository<RoomMembership, Long> {

	@Query("SELECT rm FROM RoomMembership rm WHERE rm.user.userId = :userId AND rm.room.roomId = :roomId")
    RoomMembership findByUserAndRoom(@Param("userId") Integer userId, @Param("roomId") Long roomId);
	
}
