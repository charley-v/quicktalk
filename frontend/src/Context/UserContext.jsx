import React, { createContext, useState, useContext } from 'react'
import {useNavigate } from 'react-router-dom'

export const UserContext = createContext()

export const useUser =() => {
  return useContext(UserContext)
}

export const UserProvider = ({ children }) => {
  const navigate = useNavigate()
  const [user, setUser] = useState(null)

  const login = (userData) => {
    setUser(userData);
    // Optionally, you can store user data in localStorage if needed
    localStorage.setItem('user', JSON.stringify(userData));
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('user'); // Clear user data from localStorage
    localStorage.removeItem('access_token'); // Clear access token
    localStorage.removeItem('id_token'); // Clear id token
    navigate("/")
  };

  // Function to load user data from localStorage when the app initializes
  const initializeUser = () => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  };

  return (
    <UserContext.Provider value={{ user, login, logout, initializeUser}}>
      {children}
    </UserContext.Provider>
  )
}
export default UserProvider;