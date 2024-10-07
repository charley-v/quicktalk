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
                <p>Message</p>
            </div>
        </div>
        <div className='item'>
            <img src={avatar} alt=''/>
            <div className='texts'>
                <span>User #2</span>
                <p>Message</p>
            </div>
        </div>
        <div className='item'>
            <img src={avatar} alt=''/>
            <div className='texts'>
                <span>User #3</span>
                <p>Message</p>
            </div>
        </div>
        <div className='item'>
            <img src={avatar} alt=''/>
            <div className='texts'>
                <span>User #4</span>
                <p>Message</p>
            </div>
        </div>

        <button className='logout'>
            Logout
        </button>

        
    </div>
  )
}

export default Chatlist