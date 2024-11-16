import React, { useEffect, useRef, useState } from 'react';
import {ChatBoxReciever,  ChatBoxSender } from './ChatBox';
import InputText from './InputText';
import './Chat.css';
import pic from '../../Assets/avatar.png';
import { useUser } from '../../Context/UserContext';
import List from '../../Components/List/List';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

export const Chat = () => {
    const { user } = useUser();
    const [chats, setChats] = useState([]);
    const [avatar] = useState(localStorage.getItem('avatar'));
    const endRef = useRef(null);
    const stompClientRef = useRef(null);
    const [chatter, setChatter] = useState(null); 

    // Scroll to the bottom of the chat
    const scrollToBottom = () => {
        endRef.current?.scrollIntoView({ behavior: 'smooth' });
    };

    // Log user information
    useEffect(() => {
        if (!user) {
            console.warn('User is not logged in');
        } else {
            console.info(`Logged in as: ${user.username}`);
        }
    }, [user]);

    // Setup WebSocket connection
    useEffect(() => {
      const socket = new SockJS('http://localhost:8080/chat-websocket');
      stompClientRef.current = Stomp.over(socket);
  
      stompClientRef.current.connect({}, () => {
          console.log('Connected to WebSocket');
  
          const subscription = stompClientRef.current.subscribe('/topic/messages', (message) => {
              const messageBody = JSON.parse(message.body);
              console.log('Received WebSocket message:', messageBody);
  
              // Check if username and message are correctly received
              if (!messageBody.username || !messageBody.message) {
                  console.error('Invalid message format:', messageBody);
              }
              if (messageBody.username !== user.username) {
                setChatter(messageBody.username);
            }
  
              setChats((prevChats) => {
                  if (!prevChats.some((chat) => chat.message === messageBody.message && chat.username === messageBody.username)) {
                      return [...prevChats, messageBody];
                  }
                  return prevChats; // Avoid duplicate messages
              });
          });
  
          return () => {
              subscription.unsubscribe();
              stompClientRef.current.disconnect();
              console.log('Disconnected from WebSocket');
          };
      });
  }, []);
  

    // Scroll to the bottom whenever chats update
    useEffect(() => {
        scrollToBottom();
    }, [chats]);

    // Send message to WebSocket
    const sendChatToSocket = (chat) => {
        const messageToSend = {
            username: user.username,
            message: chat.message,
        };
        console.log('Sending message:', messageToSend);

        if (stompClientRef.current && stompClientRef.current.connected) {
            stompClientRef.current.send('/app/chat', {}, JSON.stringify(messageToSend));
        } else {
            console.error('WebSocket is not connected');
        }

        //Save to database
        saveMessageToDatabase({
          roomId: 1,
          userId: user.userId,
          text: chat.message,
      });
    };

    // Add message to chats and send to WebSocket
    const addMessage = (chat) => {
        const newChat = { ...chat, username: user.username };
        sendChatToSocket(newChat);
    };

    // Render chat messages
    function ChatsList() {
      console.log('Rendering ChatsList');
      return chats.map((chat, index) => {
          console.log(`Chat message: "${chat.message}", Chat User: "${chat.username}", Logged-in User: "${user.username}"`);
  
          const isSender =
              chat.username.trim().toLowerCase() === user.username.trim().toLowerCase();
          console.log(`Is sender: ${isSender}`);
  
          if (isSender) {
              return <ChatBoxSender key={index} message={chat.message} avatar={pic} />;
          } else {
              return <ChatBoxReciever key={index} message={chat.message} avatar={pic} />;
          }
      });
  }

  //Calls Api
  const saveMessageToDatabase = async (messageData) => {
    try {
        const response = await fetch('http://localhost:8080/api/messages', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(messageData),
        });

        if (!response.ok) {
            throw new Error('Failed to save message');
        }

        console.log('Message saved:', await response.json());
    } catch (error) {
        console.error('Error saving message:', error);
    }
};
  
  

    return (
        <div className="container">
            <div className="layout">
                <List />
                <div className="chat">
                    <div className="top">
                        <div className="user">
                            <img src={pic} alt="User Avatar" />
                            <div className="texts">
                                <span>{chatter}</span>
                                <p>Online</p>
                            </div>
                        </div>
                    </div>
                    <div className="center">
                        <ChatsList />
                        <div ref={endRef}></div>
                    </div>
                    <div className="input">
                        <InputText addMessage={addMessage} />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Chat;
