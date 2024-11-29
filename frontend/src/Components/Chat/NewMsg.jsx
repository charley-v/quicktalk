import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './NewMsg.css';
import { useUser } from '../../Context/UserContext';
import { GLOBAL_CONFIG } from '../../Constants/Config';

export const NewMsg = ({isOpen, onClose, onSelectUser }) => {
    const [username, setUsername] = useState('');
    const navigate = useNavigate();
    const { user } = useUser();
    
      const handleCreateChat = async () => {
        if (!username.trim()) {
            console.error("Username cannot be empty.");
            return;
        }
    
        try {
            // Fetch secondUserId by username
            const userIdResponse = await fetch(`${GLOBAL_CONFIG.backendUrl}/users/id?username=${username}`,{
                headers: {
                    Authorization: `Bearer ${user?.accessToken}`,
                },
            });  
            console.log(userIdResponse);          
            if (!userIdResponse.ok) {
                throw new Error("User not found. Please check the username.");
            }
    
            const targetUserData = await userIdResponse.json(); // Expecting { userId: "12" }
            const secondUserId = targetUserData.userId;
            console.log(targetUserData)
            console.log(secondUserId)
            if (!secondUserId) {
                throw new Error("Failed to retrieve target user ID.");
            }
    
            const requestBody = {
                firstUserId: user.userId, // Logged-in user's ID
                secondUserId: secondUserId, // Retrieved second user's ID
            };
    
            console.log("Request Body:", requestBody);
    
            // Send the request to create a chat room
            const response = await fetch(`${GLOBAL_CONFIG.backendUrl}/rooms/private`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${user?.accessToken}`,
                },
                body: JSON.stringify(requestBody),
            });
    
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || "Failed to create chat room.");
            }
    
            const data = await response.json();
            console.log(`Chat created successfully with Room ID: ${data.roomId}`);
            onClose(); // Close the modal
            navigate(`/chat/${data.roomId}`); // Redirect to the new chat room
        } catch (error) {
            console.error("Error creating chat:", error);
        }
    };
    if (!isOpen) {
        return null; // Do not render if modal is not open
    }

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <h2>Start a New Message</h2>
                <input 
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    placeholder="Enter Username"
                    className="username-input"
                />
                <div className="modal-buttons">
                    <button onClick={handleCreateChat} className="create-button">Create</button>
                    <button onClick={onClose} className="close-button">Cancel</button>
                </div>
            </div>
        </div>
    );
};

export default NewMsg;