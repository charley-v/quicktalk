import './Chatlist.css'
import React from 'react'
import avatar from '../../../Assets/avatar.png'

export const Chatlist = ()=> {

  return (
    <div className='chatlist'>
        <div className='item'>
            <img src={avatar} alt=''/>
            <div className='texts'>
                <span>User #1</span>
                <p>How are you?</p>
            </div>
        </div>
        <div className='item'>
            <img src={avatar} alt=''/>
            <div className='texts'>
                <span>User #2</span>
                <p>I have to reschedule our lunch</p>
            </div>
        </div>
        <div className='item'>
            <img src={avatar} alt=''/>
            <div className='texts'>
                <span>User #3</span>
                <p>What are you up to?</p>
            </div>
        </div>
        <div className='item'>
            <img src={avatar} alt=''/>
            <div className='texts'>
                <span>User #4</span>
                <p>Are we still up for cofee?</p>
            </div>
        </div>
    </div>
  )
}

export default Chatlist