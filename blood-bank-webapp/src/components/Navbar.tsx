import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../utils/api';

const Navbar = () => {
  const { isAuthenticated, user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      localStorage.removeItem('donations')
      await api.post('/api/auth/logout');
    } catch (error) {
      console.error('Error during logout:', error);
    } finally {
      logout();
      navigate('/authentication');
    }
  };

  if (!isAuthenticated) {
    return null;
  }

  return (
    <nav className="navbar">
      <Link to="/donor-dashboard" className="navbar-brand navbar-item">
        🩸 Blood Bank
      </Link>
      <div className="navbar-menu">
        <Link 
          to="/donor-dashboard" 
          className="navbar-item"
        >
          Dashboard
        </Link>
        <div className="navbar-end">
          <span className="user-name">Welcome, {user?.username}</span>
          <button onClick={handleLogout} className="logout-button">
            Logout
          </button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
