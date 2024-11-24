package com.quicktalk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quicktalk.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{
	List<Message> findByRoomId(Long roomId);
}
