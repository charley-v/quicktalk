import React, { useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useUser } from '../../Context/UserContext';
import { jwtDecode } from 'jwt-decode';
import { GLOBAL_CONFIG } from '../../Constants/Config';

const CognitoCallback = () => {
  const navigate = useNavigate();
  const { login } = useUser();

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get('code');

    // Prevent duplicate calls: check if the code has already been used
    if (!code || localStorage.getItem('code_handled') === code) {
      return;
    }
      axios.post(
        `https://${GLOBAL_CONFIG.cognitoDomain}/oauth2/token`,
        new URLSearchParams({
          grant_type: 'authorization_code',
          client_id: GLOBAL_CONFIG.clientId,
          redirect_uri: GLOBAL_CONFIG.redirectUri,
          code,
        }),
        { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }
      )
        .then((response) => {
          const { access_token, id_token } = response.data;

          // console.log("In then :: /oauth2/token ", response)
          // Store tokens locally
          localStorage.setItem('access_token', access_token);
          localStorage.setItem('id_token', id_token);
          localStorage.setItem('code_handled', code); // Mark code as handled

          // Decode the id_token to get user information
          const decodedToken = jwtDecode(id_token); // Decodes JWT to extract claims
          const username = decodedToken.name || decodedToken.email;
          const email = decodedToken.email;

          // login({ username, idToken: id_token, accessToken: access_token });
          
          // Determine action based on context
          if (localStorage.getItem('signupInProgress')) {

            localStorage.removeItem('signupInProgress');

            // Send id_token to backend for signup syncing
            axios.post(`${GLOBAL_CONFIG.backendUrl}/user`, { idToken: id_token })
              .then(() => {
                alert('Signup successful! Please log in.');
                navigate('/'); // Redirect to login page
              })
              .catch(() => {
                alert('Error syncing user with the backend.');
                navigate('/');
              });
          } else {
            const accessToken = localStorage.getItem('access_token'); // Retrieve the access token
            // Login successful call backend for userId
            axios.post(`${GLOBAL_CONFIG.backendUrl}/login`, { email: email },
              {
                headers: {
                  'Authorization': `Bearer ${accessToken}`,
                }
              })
              .then((userIdResponse) => {
                const userId = userIdResponse.data.userId;
                login({ username, idToken: id_token, accessToken: accessToken, userId });
                navigate('/chat'); // Redirect to chat page
              })
              .catch(() => {
                alert('Error fetching user from backend.');
                navigate('/');
              });
            // navigate('/chat'); // Redirect to chat page
          }
        })
        .catch((error) => {
          console.log("In catch ", error)
          alert('Authentication failed. Please try again.');
          navigate('/'); // Redirect to login page
        });
  }, [navigate, login]);

  return <div>Authenticating...</div>;
};

export default CognitoCallback;