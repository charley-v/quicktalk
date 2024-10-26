import React from 'react'
import './ChatBox.css'

export const ChatBoxReciever =({avatar, message}) => {
  return (
    <div className="box receiver">
    <div className='receiver'>
        <div className='message'>
            <img src={avatar} alt =""/>
                <div className="texts">
                 <p>
                    {message}
                  </p>
            </div>
        </div>
    </div>
    </div>
  )
}
export const  ChatBoxSender = ({avatar,message}) => {
    return (
      <div className="box sender">
    <div className='sender'>
      <div className='message'>
        <img src={avatar} alt =""/>
            <div className="texts">
            <p>
                {message}
            </p>
            </div>
        </div>
     </div>
     </div>
    )
  }
  export default (ChatBoxReciever, ChatBoxSender)
