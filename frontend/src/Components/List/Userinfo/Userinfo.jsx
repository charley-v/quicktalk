import './Userinfo.css'
import React from 'react'
import avatar from '../../../Assets/avatar.png'
import {useUser} from '../../../Context/UserContext'

export const Userinfo = () => {
  const {user, logout} = useUser();
  return (
    <div className='userinfo'>
        <div className='user'>
            <img src={avatar} alt=''/>
            <h2>{user.username}</h2>
        </div>
        <div className='icons'>
        <button onClick={logout} className='logout'>
            Logout
        </button>
        </div>
    </div>
  )
}

export default Userinfo