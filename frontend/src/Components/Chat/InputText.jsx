import React, {useState } from 'react'
import './InputText.css'
import mic from '../../Assets/mic.png'
import camera from '../../Assets/camera.png'
import img from '../../Assets/img.png'

export const InputText = ({addMessage})=> {
    const [message, setMessage] = useState("")

    function addAMessage()
    {
        addMessage({
            message
        })
        setMessage('')
    }
  return (
    <div className='bottom'>
    <div className='icons'>
      <img src={img} alt=''/>
      <img src={camera} alt=''/>
      <img src={mic} alt=''/>
    </div>
    <input type='text' value= {message} placeholder='Type a message...' onChange={(e)=>setMessage(e.target.value)}/>
    <button onClick={()=> addAMessage()}className='sendButton'> Send</button>
    </div>
  )
}

export default InputText
