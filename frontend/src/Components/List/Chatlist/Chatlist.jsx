import './Chatlist.css';
import React, {useContext, useEffect, useState } from 'react';
import avatar from '../../../Assets/avatar.png';
import { GLOBAL_CONFIG } from '../../../Constants/Config';
import { UserContext } from '../../../Context/UserContext';
import { useNavigate } from 'react-router-dom';

export const Chatlist = () => {
    const [users, setUsers] = useState([]);
    const [error, setError] = useState(null);
    const [rooms, setRooms] = useState([]);
    const {user} = useContext(UserContext);
    const navigate = useNavigate();

    //Call users from api
    useEffect(() => {
        const fetchRooms = async () => {
            if (!user || !user.userId) {
                setError("User is not logged in or userId is missing");
                console.error("User context is invalid:", user);
                return;
            }
            try {
                console.log("Fetching rooms for user:", user.userId);
                const response = await fetch(`${GLOBAL_CONFIG.backendUrl}/rooms/user/${user.userId}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${user.accessToken}`, // Include the access token
                    },
                });
                if (!response.ok) {
                    throw new Error(`Failed to fetch rooms: ${response.statusText}`);
                }
                const data = await response.json();
                console.log("Rooms fetched successfully:", data);
                setRooms(data);
            } catch (err) {
                console.error("Error fetching rooms:", err);
                setError(err.message);
            }
        };
        fetchRooms();
    }, [user]);
    
    const handleRoomClick = (roomId) => {
        console.log(`Navigating to room: ${roomId}`); // Corrected string interpolation
        navigate(`/chat/${roomId}`);
    };
    return(
        <div className="chatlist">
            {error ? (<p className="error">{error}</p>) : (rooms.map((room) => (
                    <div key={room.roomId}
                        className="item"
                        onClick={() => handleRoomClick(room.roomId)}>
                        <img src={avatar} alt="User Avatar" />
                        <div className="texts">
                            <span>{room.roomName}</span>
                            <p>{room.lastMessage}</p>
                        </div>
                    </div>
                ))
            )}
        </div>
    );
};

export default Chatlist;


