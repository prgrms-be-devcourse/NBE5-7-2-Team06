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
import {Navigate} from "react-router-dom";
import FirstApprovalList from './pages/component/approval/FirstApprovalList'
import SecondApprovalList from './pages/component/approval/SecondApprovalList'
import FirstApprovalDetail from './pages/component/approval/FirstApprovalDetail'
import SecondApprovalDetail from './pages/component/approval/SecondApprovalDetail'
import MemberApprovalList from './pages/component/admin/MemberApprovalList'

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
              <Route path="/admin/code" element={<CodeManagement />} />
              <Route path="/admin/vacation-detail/:id" element={<VacationDetail />} />

              <Route path="/approval/first" element={<FirstApprovalList />} />
              <Route path="/approval/second" element={<SecondApprovalList />} />
              <Route path="/approval/first/:approvalStepId" element={<FirstApprovalDetail />} />
              <Route path="/approval/second/:approvalStepId" element={<SecondApprovalDetail />} />
              <Route path="/admin/member-approvals" element={<MemberApprovalList />} />
          </Routes>
        </DefaultLayout>
      </Router>
  );
}

export default App;
