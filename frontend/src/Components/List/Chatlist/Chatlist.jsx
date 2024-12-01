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

                    return { ...room, otherUsername };
                })
            );

            setRooms(enrichedRooms);
        } catch (err) {
            console.error("Error fetching rooms:", err);
        }
    };
    
    const addRoom = (newRoom) => {
        setRooms((prevRooms) => [...prevRooms, newRoom]);
    };

    useEffect(() => {
        if (user && user.userId) {
            fetchRooms();
        }
    }, [user]);

    // Expose refreshChatList to parent via ref
    useImperativeHandle(ref, () => ({
        refreshChatList: fetchRooms,
        addRoomToChatList: addRoom, // Expose addRoom method
    }));

    const handleRoomClick = (roomId, username) => {
        navigate(`/chat/${roomId}`, { state: { chatterUsername: username } });
    };

    const handleDeleteRoom = async (roomId) => {
        try {
            const response = await fetch(`${GLOBAL_CONFIG.backendUrl}/rooms/${roomId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${user.accessToken}`,
                },
            });

            if (!response.ok) {
                throw new Error(`Failed to delete room: ${response.statusText}`);
            }

            setRooms((prevRooms) => prevRooms.filter((room) => room.roomId !== roomId));
        } catch (err) {
            console.error("Error deleting room:", err);
        }
    };

    return (
        <div className="chatlist">
            {rooms.map((room) => (
                <div key={room.roomId} className="item" onClick={() => handleRoomClick(room.roomId, room.otherUsername)}>
                    <img src={avatar} alt="User Avatar" />
                    <div className="texts">
                        <span>{room.otherUsername}</span>
                        <p>{room.lastMessage}</p>
                    </div>
                    <button className="delete-button" onClick={(e) => { e.stopPropagation(); handleDeleteRoom(room.roomId); }}>
                        Delete
                    </button>
                </div>
            ))}
        </div>
    );
});

export default Chatlist;