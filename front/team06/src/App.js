import './App.css';
import {
  BrowserRouter as Router,
  Routes,
  Route,
} from 'react-router-dom';
import DefaultLayout from './pages/component/default-layout';
import Vacations from "./pages/component/vacations/vacations";

function App() {
  return (
      <Router>
        <DefaultLayout>
          <Routes>
              <Route path="/vacations" element={<Vacations />} />
          </Routes>
        </DefaultLayout>
      </Router>
  );
}

export default App;
