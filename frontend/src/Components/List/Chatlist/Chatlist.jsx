import './Chatlist.css';
import React, { useEffect, useState } from 'react';
import avatar from '../../../Assets/avatar.png';
import { GLOBAL_CONFIG } from '../../../Constants/Config';

export const Chatlist = () => {
    const [users, setUsers] = useState([]);
    const [error, setError] = useState(null);

    //Call users from api
    useEffect(() => {
        const fetchUsers = async () => {
            const accessToken = localStorage.getItem('access_token'); // Retrieve the access token
            try {
                const response = await fetch(`${GLOBAL_CONFIG.backendUrl}/users`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${accessToken}`, // Include the access token
                    },
                });
                if (!response.ok) {
                    throw new Error('Failed to fetch users');
                }
                const data = await response.json();
                setUsers(data);
            } catch (err) {
                setError(err.message);
                console.error('Error fetching users:', err);
            }
        };

        fetchUsers();
    }, []);

    return (
        <div className="chatlist">
            {error ? (
                <p className="error">{error}</p> // Display error if fetch fails
            ) : (
                users.map((user, index) => (
                    <div key={index} className="item">
                        <img src={avatar} alt="" />
                        <div className="texts">
                            <span>{user.username}</span>
                            <p>{user.status || 'Available'}</p>
                        </div>
                    </div>
                ))
            )}
        </div>
    );
};

export default Chatlist;
