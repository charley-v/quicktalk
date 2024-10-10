package com.quicktalk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.quicktalk.entity.Room;
import com.quicktalk.projection.RoomProjection;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

	// Query to find an existing private room between two users
	@Query("SELECT r FROM Room r WHERE r.roomType = 'single' AND EXISTS "
			+ "(SELECT rm FROM RoomMembership rm WHERE rm.room = r AND rm.user.userId = :userId1) AND "
			+ "EXISTS (SELECT rm FROM RoomMembership rm WHERE rm.room = r AND rm.user.userId = :userId2)")
	Optional<Room> findPrivateRoom(@Param("userId1") Integer userId1, @Param("userId2") Integer userId2);

	//Finding a group room with only those users in userIdsList
//	@Query("SELECT r FROM Room r JOIN RoomMembership rm ON rm.room = r " +
//		       "WHERE r.roomType = 'group' " +
//		       "AND rm.user.userId IN :userIdsList " +
//		       "GROUP BY rm.room.roomId " +
//		       "HAVING COUNT(DISTINCT rm.user.userId) = :userIdListSize " +
//		       "AND COUNT(DISTINCT CASE WHEN rm.user.userId IN :userIdsList THEN 1 END) = :userIdListSize " +
//		       "AND COUNT(DISTINCT CASE WHEN rm.user.userId NOT IN :userIdsList THEN 1 END) = 0")
	
	@Query("SELECT r FROM Room r JOIN RoomMembership rm ON rm.room = r " +
		       "GROUP BY rm.room.roomId " +
		       "HAVING COUNT(DISTINCT rm.user.userId) = :userIdListSize " +
		       "AND COUNT(CASE WHEN rm.user.userId IN :userIdsList THEN 1 END) = :userIdListSize " +
		       "AND COUNT(DISTINCT CASE WHEN rm.user.userId NOT IN :userIdsList THEN 1 END) = 0")
	Optional<Room> findGroupRoomByUserIds(@Param("userIdsList") List<Integer> userIdsList, @Param("userIdListSize") long userIdListSize);

	@Query("SELECT r FROM Room r WHERE r.roomType = 'group' and r.roomId = :groupRoomId")
	Optional<Room> findGroupRoomById(@Param("groupRoomId") Long groupRoomId);
	
	@Query("SELECT r FROM Room r JOIN r.roomMemberships rm WHERE rm.user.userId = :userId")
	List<RoomProjection> findAllRoomsByUserId(@Param("userId") Integer userId);
}
