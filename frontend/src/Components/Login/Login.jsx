import React, { useState } from 'react'
import './Login.css'
import axios from 'axios'
import {useUser} from '../../Context/UserContext'
import { useNavigate } from 'react-router-dom'

export const Login = () => {
    const {login, user} = useUser()
    const navigate = useNavigate()

    const [email, setEmail] = useState("")
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")

    const[loginForm, setloginForm] = useState({
        email: "",
        password: ""
    })
    function loginHandler (event){
        event.preventDefault()

        const loginData = {
            email: loginForm.email,
            password: loginForm.password
        }
        axios.post("http://localhost:8080/login", loginData)
        .then((response) => {
            console.log(response.data)
            login(response.data)  
            navigate('/chat')
          })
          .catch((error) => {
            console.log('Login error:', error); // Log the entire error
            if (error.response) {
                if (error.response.status === 401) {
                    alert("Invalid Email or Password");
                } else {
                    alert("Internal Server Error. Please try again later.");
                }
            } else {
                alert("Network error. Please check your connection.");
            }
        });
          setloginForm({
            email: "",
            password: ""
          })
        }

    const signup =  () => 
    {
        axios.post('http://localhost:8080/user', {
            username: username,
            email: email, 
            password: password, 
        })
        .then(function(response)
        {
            console.log(response)
            window.location.href = '/' //where to navigate after signing up 
        })
        .catch(function(error){
            console.log(error, 'error')
            if (error.response) {
                if (error.response.status === 401) {
                    alert("Invalid Credentials");
                } else {
                    alert("Error: " + error.response.data.message || "An error occurred. Please try again.");
                }
            } else {
                // Handle network or other errors
                alert("Network error or server is not reachable. Please try again later.");
            }
        })

    }
    function handleChange(event)
    {
        const {value, name} = event.target 
        setloginForm(prevNote=> ({
            ...prevNote, [name]: value})
    )}

    return (

        <div className="container">
             <div className="logo">
             <h1>QuickTalk</h1>
           </div>
         <div className='login'>
         
             <div className='item'>
             <h2>Log in</h2>
             <form>
                 <input type="text" value={loginForm.email} onChange={handleChange} placeholder='Email' name='email'/>
             {
                 <input type='password' value={loginForm.password} onChange={handleChange}placeholder='Password'name='password'/>
             }
                 <button type='button' onClick={loginHandler}>Log In</button>
             </form>
             </div>
             <a className="signup-link" onClick={()=> signup()}>
                 Don't have an account? Sign up
             </a>
         </div>
         </div>
       )
     }
     
     export default Login