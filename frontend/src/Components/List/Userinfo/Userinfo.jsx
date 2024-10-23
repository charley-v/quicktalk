import './Userinfo.css'
import React from 'react'
import avatar from '../../../Assets/avatar.png'
import more from '../../../Assets/more.png'
import video from '../../../Assets/video.png'
import edit from '../../../Assets/edit.png'

export const Userinfo = () => {
  return (
    <div className='userinfo'>
        <div className='user'>
            <img src={avatar} alt=''/>
            <h2>John Doe</h2>
        </div>
        <div className='icons'>
            <img src={more} alt=''/>
            <img src={video} alt=''/>
            <img src={edit} alt=''/>
        </div>
    </div>
  )
}

export default Userinfo