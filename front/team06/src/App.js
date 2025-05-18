import './App.css';
import {
  BrowserRouter as Router,
  Routes,
  Route,
} from 'react-router-dom';
import DefaultLayout from "./pages/component/default-layout";
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
