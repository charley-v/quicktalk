import "./App.css"
import Chat from './Components/Chat/Chat'
import { BrowserRouter, Routes, Route,Link } from 'react-router-dom';
import Login from "./Components/Login/Login";
import CognitoCallback from "./Components/Cognito/CognitoCallback";
import UserProvider, {userProvider, useUser} from '../src/Context/UserContext'

function App() {
  return (
    <BrowserRouter>
    <UserProvider>
      <Routes>
      <Route path='/' element={<Login/>}/>
      <Route path='/chat/:roomId' element={<Chat/>}/>
      <Route path="/callback" element={<CognitoCallback/>} />
      </Routes>
      </UserProvider>
    </BrowserRouter>
      
  );
}


export default App;
