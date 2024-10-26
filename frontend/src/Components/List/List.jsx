import React from 'react'
import './List.css'
import Userinfo from './Userinfo/Userinfo'
import Chatlist from './Chatlist/Chatlist'

export const List = () => {
  return (
    <div className='list'>
      <Userinfo/>
      <div className='top-buttons'>
        <button className='list-button'>New Message</button>
        <button className='list-button'>New Group</button>
        </div>
        <Chatlist/>
    </div>
  )
}

export default List