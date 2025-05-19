import './App.css';
import {
  BrowserRouter as Router,
  Routes,
  Route,
} from 'react-router-dom';
import DefaultLayout from './pages/component/default-layout';
import Login from'./pages/component/auth/login';
import SignUp from'./pages/component/auth/signUp';
import Calendar from './pages/component/vacations/calendar';
import VacationManagerPage from './pages/component/vacations/statistics';
import {Navigate} from "react-router-dom";
import VacationList from "./pages/admin/vacation-list";
import CodeManagement from "./pages/admin/code-management";
import VacationDetail from "./pages/admin/vacation-detail";


function App() {
  return (
      <Router>
        <DefaultLayout>
          <Routes>
              <Route path="/" element={<Navigate to="/auth/login" />} />

              <Route path="/auth/login" element={<Login />} />
              <Route path="/auth/signup" element={<SignUp />} />

              <Route path="/vacations/calendar" element={<Calendar />} />
              
              <Route path="/admin/vacation-request" element={<VacationList />} />
              <Route path="/admin/statistics" element={<VacationManagerPage />} />
              <Route path="/admin/code" element={<CodeManagement />} />
              <Route path="/admin/vacation-detail/:id" element={<VacationDetail />} />

          </Routes>
        </DefaultLayout>
      </Router>
  );
}

export default App;
