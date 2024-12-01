import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './NewMsg.css';
import { useUser } from '../../Context/UserContext';
import { GLOBAL_CONFIG } from '../../Constants/Config';

export const NewMsg = ({ isOpen, onClose, refreshChatList, onCreateChat }) => {
    const [username, setUsername] = useState('');
    const [users, setUsers] = useState([]);
    const [filteredUsers, setFilteredUsers] = useState([]);
    const [selectedUsers, setSelectedUsers] = useState([]); // Track selected users for group chat
    const [isGroupChat, setIsGroupChat] = useState(false); // Toggle for group/private chat
    const [groupName, setGroupName] = useState('');
    const navigate = useNavigate();
    const { user } = useUser();


    useEffect(() => {
        if (isOpen) {
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
        const filtered = users.filter((u) => u.username.toLowerCase().includes(searchTerm.toLowerCase()));
        setFilteredUsers(filtered);
    };

    const toggleUserSelection = (userId) => {
        setSelectedUsers((prev) =>
            prev.includes(userId) ? prev.filter((id) => id !== userId) : [...prev, userId]
        );
    };

    const handleCreateChat = async () => {
        if (isGroupChat) {
            await handleCreateGroupChat();
        } else {
            await handleCreatePrivateChat();
        }
    };

    const handleCreatePrivateChat = async () => {
        if (!username.trim()) {
            console.error("Username cannot be empty.");
            return;
        }

        try {
            const userIdResponse = await fetch(`${GLOBAL_CONFIG.backendUrl}/users/id?username=${username}`, {
                headers: {
                    Authorization: `Bearer ${user?.accessToken}`,
                },
            });

            if (!userIdResponse.ok) {
                throw new Error("User not found. Please check the username.");
            }

            const targetUserData = await userIdResponse.json();
            const secondUserId = targetUserData.userId;

            const requestBody = {
                firstUserId: user.userId,
                secondUserId,
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
                throw new Error("Failed to create private chat.");
            }

            const data = await response.json();
            console.log(`Private chat created successfully with Room ID: ${data.roomId}`);

            const newRoom = {
                roomId: data.roomId,
                roomName: `Chat with UserIds ${user.userId} ${secondUserId}`,
                lastMessage: "",
                otherUsername: username,
            };

            if (typeof onCreateChat === "function") {
                onCreateChat(newRoom);
            }
            onClose();
            navigate(`/chat/${data.roomId}`);
        } catch (error) {
            console.error("Error creating private chat:", error);
        }
    };

    const handleCreateGroupChat = async () => {
        if (selectedUsers.length < 2) {
            console.error("A group chat requires at least two users.");
            return;
        }
    
        try {
            const requestBody = {
                userIdList: [user.userId, ...selectedUsers],
                groupRoomName: groupName.trim() || 'Unnamed Group', // Ensure groupName is used
            };
    
            const response = await fetch(`${GLOBAL_CONFIG.backendUrl}/rooms/group`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${user?.accessToken}`,
                },
                body: JSON.stringify(requestBody),
            });
    
            if (!response.ok) {
                throw new Error("Failed to create group chat.");
            }
    
            const data = await response.json();
            console.log(`Group chat created successfully with Room ID: ${data.roomId}`);
    
            const newRoom = {
                roomId: data.roomId,
                roomName: requestBody.groupRoomName, // Use the group name from the request
                lastMessage: "",
                roomType: 'group', // Explicitly set the room type
            };
    
            if (typeof onCreateChat === "function") {
                onCreateChat(newRoom);
            }
            onClose();
            navigate(`/chat/${data.roomId}`);
        } catch (error) {
            console.error("Error creating group chat:", error);
        }
    };

    if (!isOpen) {
        return null;
    }

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <h2>Start a New Message</h2>
                <div className="toggle-container">
                    <label>
                        <input
                            type="checkbox"
                            checked={isGroupChat}
                            onChange={(e) => setIsGroupChat(e.target.checked)}
                        />
                        Group Chat
                    </label>
                </div>
                {isGroupChat && (
                    <input
                        type="text"
                        value={groupName}
                        onChange={(e) => setGroupName(e.target.value)}
                        placeholder="Enter Group Name"
                        className="group-name-input"
                    />
                )}
                <input
                    type="text"
                    value={username}
                    onChange={(e) => handleSearch(e.target.value)}
                    placeholder="Search by Username"
                    className="username-input"
                    disabled={isGroupChat} // Disable input in group chat mode
                />
                <div className="user-list">
                    {filteredUsers.length > 0 ? (
                        filteredUsers.map((u) => (
                            <div
                                key={u.userId}
                                className={`user-item ${
                                    isGroupChat && selectedUsers.includes(u.userId) ? "selected" : ""
                                }`}
                                onClick={() =>
                                    isGroupChat ? toggleUserSelection(u.userId) : setUsername(u.username)
                                }
                            >
                                {u.username}
                            </div>
                        ))
                    ) : (
                        <p>No users found.</p>
                    )}
                </div>
                <div className="modal-buttons">
                    <button onClick={handleCreateChat} className="create-button">
                        Create
                    </button>
                    <button onClick={onClose} className="close-button">
                        Cancel
                    </button>
                </div>
            </div>
        </div>
    );
};

export default NewMsg;