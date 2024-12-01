import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../utils/api";
import '../styles/auth.css';

interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

const UserRegistration: React.FC = () => {
  const navigate = useNavigate();
  const [error, setError] = useState<string>("");
  const [isLoading, setIsLoading] = useState(false);
  const [user, setUser] = useState<RegisterRequest>({
    username: "",
    email: "",
    password: "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError("");
    setIsLoading(true);

    try {
      await api.post("/api/auth/register", user);
      navigate("/donor-dashboard");
    } catch (error: any) {
      setError(error.response?.data?.message || "Error during registration");
      console.error("Error registering:", error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <form onSubmit={handleSubmit} className="auth-form">
        <h2>Create Account</h2>
        {error && <div className="error-message">{error}</div>}

        <label htmlFor="username">Username</label>
        <input
          type="text"
          id="username"
          name="username"
          value={user.username}
          onChange={handleChange}
          required
          placeholder="Choose a username"
          autoComplete="username"
        />

        <label htmlFor="email">Email</label>
        <input
          type="email"
          id="email"
          name="email"
          value={user.email}
          onChange={handleChange}
          required
          placeholder="Enter your email"
          autoComplete="email"
        />

        <label htmlFor="password">Password</label>
        <input
          type="password"
          id="password"
          name="password"
          value={user.password}
          onChange={handleChange}
          required
          placeholder="Create a password"
          autoComplete="new-password"
        />

        <button type="submit" disabled={isLoading} className="register-button">
          {isLoading ? (
            <span>Creating Account...</span>
          ) : (
            <span>Create Account</span>
          )}
        </button>
      </form>
    </div>
  );
};

export default UserRegistration;
