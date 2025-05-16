import './App.css';
import {
  BrowserRouter as Router,
  Routes,
  Route,
} from 'react-router-dom';
import AdminLayout from './pages/component/admin-layout';
import Vacations from "./pages/component/vacations/vacations";

function App() {
  return (
      <Router>
        <AdminLayout>
          <Routes>
              <Route path="/vacations" element={<Vacations />} />
          </Routes>
        </AdminLayout>
      </Router>
  );
}

export default App;
