package com.quicktalk.controller;

import com.quicktalk.bean.MessageResponseBean;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public MessageResponseBean sendMessage(MessageResponseBean message) {
        return message; // Message will be sent to all connected clients
    }
}
