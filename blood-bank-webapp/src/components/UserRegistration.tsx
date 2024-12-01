import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../utils/api";

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
      navigate("/authentication", { state: { message: "Registration successful! Please login." } });
    } catch (error: any) {
      setError(error.response?.data?.message || "Error during registration");
      console.error("Error registering:", error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="login-container">
      <form onSubmit={handleSubmit} className="auth-form"> 
        <h2>Register</h2>

        {error && <div className="error-message">{error}</div>}

        <label htmlFor="username">Username</label>
        <input
          type="text"
          id="username"
          name="username"
          placeholder="Username"
          value={user.username}
          onChange={handleChange}
          disabled={isLoading}
          required
        />

        <label htmlFor="email">Email</label>
        <input
          type="email"
          id="email"
          name="email"
          placeholder="Email"
          value={user.email}
          onChange={handleChange}
          disabled={isLoading}
          required
        />

        <label htmlFor="password">Password</label>
        <input
          type="password"
          id="password"
          name="password"
          placeholder="Password"
          value={user.password}
          onChange={handleChange}
          disabled={isLoading}
          required
        />

        <button type="submit" disabled={isLoading} className="submit-button">
          {isLoading ? "Registering..." : "Register"}
        </button>
      </form>
    </div>
  );
};

export default UserRegistration;
