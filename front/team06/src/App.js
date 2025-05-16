import { BrowserRouter as Router, Navigate, Route, Routes } from "react-router-dom";
import VacationList from "./pages/admin/vacation-list";
import CodeManagement from "./pages/admin/code-management";
import VacationDetail from "./pages/admin/vacation-detail";
import DefaultLayout from "./pages/component/default-layout";

function App() {
  return (
    <Router>
      <DefaultLayout>
        <Routes>
          <Route path="/" element={<Navigate to="/admin/vacation-request" replace />} />
          <Route path="/admin/vacation-request" element={<VacationList />} />
          <Route path="/admin/code" element={<CodeManagement />} />
          <Route path="/admin/vacation-detail/:id" element={<VacationDetail />} />
        </Routes>
      </DefaultLayout>
    </Router>
  );
}

export default App;
