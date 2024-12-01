import React, {useState, useRef} from 'react'
import './List.css'
import Userinfo from './Userinfo/Userinfo'
import Chatlist from './Chatlist/Chatlist'
import NewMsg from '../Chat/NewMsg'

export const List = () => {
  const [newMsg, setNewMsgOpen] = useState(false)
  const chatlistRef = useRef(null); // Reference to Chatlist for refreshing

  const handleNewMsg = () => {
    setNewMsgOpen(true);
  }
  const handleCloseNewMsg = () =>{
    setNewMsgOpen(false)
  }
  const handleCreateChat = (newRoom) => {
    console.log('New chat created:', newRoom);
    setNewMsgOpen(false);

    // Add the new room directly to the Chatlist
    if (chatlistRef.current) {
        chatlistRef.current.addRoomToChatList(newRoom);
    }
};
  return (
    <div className='list'>
      <Userinfo/>
      <div className='searchbar-container'>
      <input type='text' className='searchbar' placeholder='Search...'/>
      </div>
      <div className='top-buttons'>
        <button className='list-button' onClick={handleNewMsg}>New Message</button>
        </div>
        <Chatlist ref={chatlistRef}/>
        <NewMsg isOpen={newMsg} onClose={handleCloseNewMsg} onCreateChat={handleCreateChat}/>

    </div>
  )
}

export default List