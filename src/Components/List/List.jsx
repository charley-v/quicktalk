import React from 'react'
import './List.css'
import Userinfo from './Userinfo/Userinfo'
import Chatlist from './Chatlist/Chatlist'

export const List = () => {
  return (
    <div className='list'>
        <Userinfo/>
        <Chatlist/>
    </div>
  )
}

export default List