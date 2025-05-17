import './App.css';
import {
  BrowserRouter as Router,
  Routes,
  Route,
} from 'react-router-dom';
import DefaultLayout from './pages/component/default-layout';
import Login from'./pages/component/auth/login';
import SignUp from'./pages/component/auth/signUp';
import {Navigate} from "react-router-dom";


function App() {
  return (
      <Router>
        <DefaultLayout>
          <Routes>
              <Route path="/" element={<Navigate to="/auth/login" />} />

              <Route path="/auth/login" element={<Login />} />
              <Route path="/auth/signup" element={<SignUp />} />
          </Routes>
        </DefaultLayout>
      </Router>
  );
}

export default App;
