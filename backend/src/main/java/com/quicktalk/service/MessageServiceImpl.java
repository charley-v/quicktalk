package com.quicktalk.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quicktalk.entity.Message;
import com.quicktalk.repository.MessageRepository;


@Service("MessageServiceImpl")
public class MessageServiceImpl implements MessageService{
	
	@Autowired
	MessageRepository messageRepo;
	
	private static final Logger MESSAGE_SERVICE_LOG = LoggerFactory.getLogger(MessageServiceImpl.class);

	@Override
	public List<Message> getMessagesByRoomId(Long roomId) {

		List<Message> messages = new ArrayList<>();
		MESSAGE_SERVICE_LOG.info("MessageServiceImpl :: in getMessagesByRoomId()");
		try {
			messages = messageRepo.findByRoomId(roomId);
		} catch (Exception e) {
			MESSAGE_SERVICE_LOG.info("MessageServiceImpl :: Exception in getMessagesByRoomId :: {}",e);
		}
		MESSAGE_SERVICE_LOG.info("MessageServiceImpl :: exit getMessagesByRoomId()");
		return messages;
	}
}