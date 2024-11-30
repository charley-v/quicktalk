import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './NewMsg.css';
import { useUser } from '../../Context/UserContext';
import { GLOBAL_CONFIG } from '../../Constants/Config';

export const NewMsg = ({ isOpen, onClose, onSelectUser }) => {
    const [username, setUsername] = useState('');
    const [users, setUsers] = useState([]);
    const [filteredUsers, setFilteredUsers] = useState([]);
    const navigate = useNavigate();
    const { user } = useUser();

    useEffect(() => {
        if (isOpen) {
            // Fetch available users when the modal opens
            const fetchUsers = async () => {
                try {
                    const response = await fetch(`${GLOBAL_CONFIG.backendUrl}/users`, {
                        headers: {
                            Authorization: `Bearer ${user?.accessToken}`,
                        },
                    });

                    if (!response.ok) {
                        throw new Error("Failed to fetch users.");
                    }

                    const data = await response.json();
                    setUsers(data);
                    setFilteredUsers(data);
                } catch (error) {
                    console.error("Error fetching users:", error);
                }
            };

            fetchUsers();
        }
    }, [isOpen, user]);

    const handleSearch = (searchTerm) => {
        setUsername(searchTerm);
        const filtered = users.filter((u) =>
            u.username.toLowerCase().includes(searchTerm.toLowerCase())
        );
        setFilteredUsers(filtered);
    };

    const handleCreateChat = async (selectedUsername) => {
        const targetUsername = selectedUsername || username;
        if (!targetUsername.trim()) {
            console.error("Username cannot be empty.");
            return;
        }

        try {
            const userIdResponse = await fetch(`${GLOBAL_CONFIG.backendUrl}/users/id?username=${targetUsername}`, {
                headers: {
                    Authorization: `Bearer ${user?.accessToken}`,
                },
            });

            if (!userIdResponse.ok) {
                throw new Error("User not found. Please check the username.");
            }

            const targetUserData = await userIdResponse.json(); // Expecting { userId: "12" }
            const secondUserId = targetUserData.userId;

            if (!secondUserId) {
                throw new Error("Failed to retrieve target user ID.");
            }

            const requestBody = {
                firstUserId: user.userId, // Logged-in user's ID
                secondUserId: secondUserId, // Retrieved second user's ID
            };

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
                    onChange={(e) => handleSearch(e.target.value)}
                    placeholder="Search by Username"
                    className="username-input"
                />
                <div className="user-list">
                    {filteredUsers.length > 0 ? (
                        filteredUsers.map((u) => (
                            <div
                                key={u.userId}
                                className="user-item"
                                onClick={() => handleCreateChat(u.username)}
                            >
                                {u.username}
                            </div>
                        ))
                    ) : (
                        <p>No users found.</p>
                    )}
                </div>
                <div className="modal-buttons">
                    <button onClick={() => handleCreateChat()} className="create-button">Create</button>
                    <button onClick={onClose} className="close-button">Cancel</button>
                </div>
            </div>
        </div>
    );
};

export default NewMsg;