package com.quicktalk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.quicktalk.bean.ChatMessageRequestBean;
import com.quicktalk.entity.Message;
import com.quicktalk.repository.MessageRepository;

@Controller
public class ChatController {

	/*
	 * @MessageMapping("/chat")
	 * 
	 * @SendTo("/topic/messages") public MessageResponseBean
	 * sendMessage(MessageResponseBean message) { return message; // Message will be
	 * sent to all connected clients }
	 */
	
	@Autowired
	MessageRepository messageRepo;
	
	private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Endpoint for sending messages to a specific room
    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@DestinationVariable String roomId, ChatMessageRequestBean chatMessage) {
    	
    	Message message = new Message();
    	message.setUserId(chatMessage.getSenderUserId());
    	message.setRoomId(chatMessage.getRoomId());
    	message.setText(chatMessage.getMessage());
    	 
    	messageRepo.save(message);
    	
        messagingTemplate.convertAndSend("/topic/room/" + roomId, message);
    }
}