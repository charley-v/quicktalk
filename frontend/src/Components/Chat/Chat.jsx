import React, { useEffect, useRef, useState } from 'react'
import ChatBoxReciever, { ChatBoxSender } from './ChatBox';
import InputText from './InputText'
import './Chat.css'
import pic from '../../Assets/avatar.png'
import {useUser} from '../../Context/UserContext'
import List from '../../Components/List/List'
import SockJS from 'sockjs-client'
import {Stomp} from '@stomp/stompjs'


export const Chat = () => {
  const {user} = useUser();
  const [chats, setChats] = useState([])
  const [avatar, setAvatar] = useState(localStorage.getItem("avatar"))
  const endRef = useRef(null)
  const stompClientRef = useRef(null);

  const scrollToBottom = () =>
  {
    endRef.current?.scrollIntoView({behavior: "smooth"})
  }
  
  useEffect(() => {
    console.log("User", user)
    if (!user) {
        console.log("User is not logged in"); // Log if user is not logged in
    } else {
        console.log(`Logged in as: ${user.username}`); // Log the logged-in user
    }
}, [user]);
  
  useEffect(() => {
    const socket = new SockJS("http:localhost:8080/chat-websocket")
    stompClientRef.current = Stomp.over(socket)

    stompClientRef.current.connect({}, () => {
      console.log("connected to websocket")
      stompClientRef.current.subscribe('/topic/messages', (message) => {
        const messageBody = JSON.parse(message.body)
        console.log("Received message:", messageBody)
        setChats((prevChats) => [...prevChats, messageBody])
      })
    })
    return () => {
        if (stompClientRef.current) {
            stompClientRef.current.disconnect()
        }
    };
}, []);

  useEffect(() => {
    scrollToBottom();
  }, [chats]);

  const sendChatToSocket = (chat) => {
    if (stompClientRef.current && stompClientRef.current.connected) {
      stompClientRef.current.send("/app/chat", {}, JSON.stringify(chat));
    } else {
      console.log("WebSocket is not connected");
    }
  };


  const addMessage = (chat) =>{
    const newChat = {...chat, user: user?.user_name || "guest", avatar}
    sendChatToSocket(newChat)
  }
  function ChatsList(){
    console.log("current chats: ", chats)
    return chats.map((chat, index)=>{
      console.log(`Chat user: ${chat.username}, Logged-in user: ${user.username}`);
      if(chat.user === user.username)
      {
        return <ChatBoxSender key={index} message={chat.message} avatar={pic}/>
  
      }
      else{
        return <ChatBoxReciever key={index} message={chat.message} avatar={pic}/>
      }
        
    })
}

  return (
    <div className='container'>
      <div className="layout">
        <List/>
      <div className='chat'>
      <div className='top'>
        <div className='user'>
          <img src={pic} alt=''/>
          <div className='texts'>
            <span>{chats.user}</span>
            <p>Online</p>
          </div>
        </div>
      </div>
      <div className='center'>
          <ChatsList/>
        <div ref={endRef}></div>
      </div>
      <div className='input'>
        <InputText addMessage={addMessage}/>
        </div>
      </div>
      </div>
    </div>
  )
}

export default Chat;