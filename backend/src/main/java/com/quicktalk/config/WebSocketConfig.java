package com.quicktalk.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.quicktalk.jwt.WebSocketJWTInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Value("${frontend.url}")
	String frontendURL;
	
	private final WebSocketJWTInterceptor webSocketJwtInterceptor;

    public WebSocketConfig(WebSocketJWTInterceptor webSocketJwtInterceptor) {
        this.webSocketJwtInterceptor = webSocketJwtInterceptor;
    }
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Messages sent to /topic will be broadcasted
        config.setApplicationDestinationPrefixes("/app"); // Prefix for messages sent from client
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/chat-websocket").setAllowedOrigins("http://localhost:3000").withSockJS();
    	registry.addEndpoint("/chat-websocket")
    		.setAllowedOrigins(frontendURL)
    		.addInterceptors(webSocketJwtInterceptor) // Attach JWT validation
    		.withSockJS();
    }
}
