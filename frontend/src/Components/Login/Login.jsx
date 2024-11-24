import React from 'react';
import './Login.css';
import { useNavigate } from 'react-router-dom';
import { GLOBAL_CONFIG } from '../../Constants/Config';

export const Login = () => {
  const navigate = useNavigate();

  // Login function using Cognito Hosted UI
  const login = () => {
    const loginUrl = `https://${GLOBAL_CONFIG.cognitoDomain}/login?client_id=${GLOBAL_CONFIG.clientId}&response_type=code&scope=openid&redirect_uri=${encodeURIComponent(GLOBAL_CONFIG.redirectUri)}`;
    window.location.href = loginUrl;
  };

  // Signup function using Cognito Hosted UI
  const signup = () => {
    // Set signup flag in localStorage
    localStorage.setItem('signupInProgress', 'true');
    const signupUrl = `https://${GLOBAL_CONFIG.cognitoDomain}/signup?client_id=${GLOBAL_CONFIG.clientId}&response_type=code&scope=openid&redirect_uri=${encodeURIComponent(GLOBAL_CONFIG.redirectUri)}`;
    window.location.href = signupUrl;
  };

  return (
    <div className="container">
      <div className="logo">
        <h1>QuickTalk</h1>
      </div>
      <div className="login">
        <div className="item">
          <h2>Welcome</h2>
          <button onClick={login}>Log In</button>
          <a className="signup-link" href="#" onClick={signup}>
            Don't have an account? Sign up
          </a>
        </div>
      </div>
    </div>
  );
};

export default Login;