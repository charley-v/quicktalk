import React, { useContext, useEffect, useState, forwardRef, useImperativeHandle } from 'react';
import avatar from '../../../Assets/avatar.png';
import { GLOBAL_CONFIG } from '../../../Constants/Config';
import { UserContext } from '../../../Context/UserContext';
import { useNavigate } from 'react-router-dom';
import './Chatlist.css';

const Chatlist = forwardRef((props, ref) => {
    const [rooms, setRooms] = useState([]);
    const { user } = useContext(UserContext);
    const navigate = useNavigate();

    const fetchRooms = async () => {
        try {
            const response = await fetch(`${GLOBAL_CONFIG.backendUrl}/rooms/user/${user.userId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${user.accessToken}`,
                },
            });

            if (!response.ok) {
                throw new Error(`Failed to fetch rooms: ${response.statusText}`);
            }

            const roomsData = await response.json();
            const enrichedRooms = await Promise.all(
                roomsData.map(async (room) => {
                    if (room.roomType === 'group') {
                        return {
                            ...room,
                            displayName: room.roomName, // Use the group name for group chats
                        };
                    } else {
                        let otherUserId = null;
                        let otherUsername = "Unknown User";

                        if (room.roomName.startsWith("Chat with UserIds")) {
                            const userIds = room.roomName.match(/\d+/g);
                            const otherUserIds = userIds.filter((id) => parseInt(id) !== parseInt(user.userId));
                            otherUserId = otherUserIds.length > 0 ? otherUserIds[0] : null;
                        }

                        if (otherUserId) {
                            const userResponse = await fetch(`${GLOBAL_CONFIG.backendUrl}/${otherUserId}`, {
                                method: 'GET',
                                headers: {
                                    'Content-Type': 'application/json',
                                    'Authorization': `Bearer ${user.accessToken}`,
                                },
                            });

                            if (userResponse.ok) {
                                const userData = await userResponse.json();
                                otherUsername = userData.username;
                            }
                        }

                        return {
                            ...room,
                            displayName: otherUsername, // Display the other user's name for private chats
                        };
                    }
                })
            );

            setRooms(enrichedRooms);
        } catch (err) {
            console.error("Error fetching rooms:", err);
        }
    };

    const addRoom = async (newRoom) => {
        if (newRoom.roomType === 'group') {
            // Directly set the group name for group chats
            newRoom.displayName = newRoom.roomName || 'Unnamed Group';
        } else if (newRoom.roomName) {
            // For private chats, extract the other user's name
            const userIds = newRoom.roomName.match(/\d+/g); // Extract numeric IDs
            if (userIds) {
                const otherUserId = userIds.filter((id) => parseInt(id) !== parseInt(user.userId))[0]; // Exclude logged-in user ID
    
                if (otherUserId) {
                    try {
                        const userResponse = await fetch(`${GLOBAL_CONFIG.backendUrl}/${otherUserId}`, {
                            method: 'GET',
                            headers: {
                                'Content-Type': 'application/json',
                                'Authorization': `Bearer ${user.accessToken}`,
                            },
                        });
    
                        if (userResponse.ok) {
                            const userData = await userResponse.json();
                            newRoom.displayName = userData.username; // Assign fetched username
                        } else {
                            console.error(`Failed to fetch username for userId: ${otherUserId}`);
                            newRoom.displayName = 'Unknown User';
                        }
                    } catch (err) {
                        console.error("Error fetching username for new room:", err);
                        newRoom.displayName = 'Unknown User';
                    }
                } else {
                    newRoom.displayName = 'Unknown User';
                }
            } else {
                newRoom.displayName = 'Invalid Room';
            }
        } else {
            newRoom.displayName = 'Invalid Room'; // Fallback for invalid roomName
        }
    
        setRooms((prevRooms) => [...prevRooms, newRoom]);
    };

    useEffect(() => {
        if (user && user.userId) {
            fetchRooms();
        }
    }, [user]);

    // Expose methods to parent via ref
    useImperativeHandle(ref, () => ({
        refreshChatList: fetchRooms,
        addRoomToChatList: addRoom, // Expose addRoom method
    }));

    const handleRoomClick = (roomId, displayName) => {
        navigate(`/chat/${roomId}`, { state: { chatterUsername: displayName } });
    };


    return (
        <div className="chatlist">
            {rooms.map((room) => (
                <div
                    key={room.roomId}
                    className="item"
                    onClick={() => handleRoomClick(room.roomId, room.displayName)}
                >
                    <img src={avatar} alt="User Avatar" />
                    <div className="texts">
                        <span>{room.displayName}</span> {/* Display group or user name */}
                        <p>{room.lastMessage}</p>
                    </div>
                </div>
            ))}
        </div>
    );
});

export default Chatlist;