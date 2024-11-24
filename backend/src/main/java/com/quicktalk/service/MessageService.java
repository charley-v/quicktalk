package com.quicktalk.service;

import java.util.List;

import com.quicktalk.entity.Message;

public interface MessageService {

	List<Message> getMessagesByRoomId(Long roomId);
}
