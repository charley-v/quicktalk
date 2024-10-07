import React, { useEffect, useRef } from 'react'
import {useState} from 'react'
import './Chat.css'
import avatar from '../../Assets/avatar.png'
import video from '../../Assets/video.png'
import more from '../../Assets/more.png'
import mic from '../../Assets/mic.png'
import camera from '../../Assets/camera.png'
import img from '../../Assets/img.png'

export const Chat = () => {
  const[Text, setText] = useState("");

  const endRef = useRef(null);

  useEffect(()=>{
    endRef.current?.scrollIntoView({behavior:"smooth"})

  },[])


  return (
    <div className='chat'>
      <div className='top'>
        <div className='user'>
          <img src={avatar} alt=''/>
          <div className='texts'>
            <span>User #1</span>
            <p>Status..</p>
          </div>
        </div>
        <div className='icons'>
          <img src={video} alt=''/>
          <img src={more} alt=''/>
        </div>
      </div>
      <div className='center'>
        <div className="message">
          <img src={avatar} alt=''/>
          <div className="text">
            <p>Random Text</p>
            <span>1 min ago</span>
          </div>
        </div>
        <div className="ownmessage">
          <div className="text">
            <p>Random Text</p>
            <span>1 min ago</span>
          </div>
        </div>
        <div className="message">
          <img src={avatar} alt=''/>
          <div className="text">
            <p>Random Text</p>
            <span>1 min ago</span>
          </div>
        </div>
        <div className="ownmessage">
          <div className="text">
            <p>Random Text</p>
            <span>1 min ago</span>
          </div>
        </div>
        <div className="message">
          <img src={avatar} alt=''/>
          <div className="text">
            <p>Random Text</p>
            <span>1 min ago</span>
          </div>
        </div>
        <div className="ownmessage">
          <div className="text">
            <p>Random Text</p>
            <span>1 min ago</span>
          </div>
        </div>
        <div ref={endRef}></div>
      </div>
      <div className='bottom'>
      <div className='icons'>
        <img src={img} alt=''/>
        <img src={camera} alt=''/>
        <img src={mic} alt=''/>
      </div>
      <input type='text' placeholder='Type a message...' onChange={(e)=>setText(e.target.value)}/>
      <button className='sendButton'> Send</button>
      </div>
    </div>
  )
}

export default Chat