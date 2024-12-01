import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import api from "../utils/api";
import "../styles/auth.css";

const UserLogin: React.FC = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError(null);
    setIsLoading(true);

    try {
      const response = await api.post("/api/auth/login", {
        username,
        password,
      });
      const { token } = response.data;
      login(token);
      navigate("/donor-dashboard");
    } catch (error: any) {
      setError(error.response?.data || "Failed to login. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className='auth-container'>
      <form onSubmit={handleLogin} className='auth-form'>
        <h2>Welcome Back</h2>
        {error && <div className='error-message'>{error}</div>}

        <label htmlFor='username'>Username</label>
        <input
          type='text'
          id='username'
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
          placeholder='Enter your username'
          autoComplete='username'
        />

        <label htmlFor='password'>Password</label>
        <input
          type='password'
          id='password'
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          placeholder='Enter your password'
          autoComplete='current-password'
        />

        <button type='submit' disabled={isLoading} className='login-button'>
          {isLoading ? <span>Logging in...</span> : <span>Login</span>}
        </button>
      </form>
    </div>
  );
};

export default UserLogin;
