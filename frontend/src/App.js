import "./App.css"
import Chat from './Components/Chat/Chat'
import { BrowserRouter, Routes, Route,Link } from 'react-router-dom';
import Login from "./Components/Login/Login";
import UserProvider, {userProvider, useUser} from '../src/Context/UserContext'

function App() {
  return (
    <BrowserRouter>
    <UserProvider>
      <Routes>
      <Route path='/' element={<Login/>}/>
      <Route path='/chat' element={<Chat/>}/>
      </Routes>
      </UserProvider>
    </BrowserRouter>
      
  );
}


export default App;