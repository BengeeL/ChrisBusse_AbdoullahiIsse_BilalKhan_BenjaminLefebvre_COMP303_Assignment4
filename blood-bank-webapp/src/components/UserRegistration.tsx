import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../utils/api";
import "../styles/auth.css";
import { useAuth } from "../context/AuthContext.tsx";

interface userRegisterRequest {
  username: string;
  email: string;
  password: string;
}

interface loginRequest {
  username: string;
  password: string;
}

interface donorRegisterRequest {
  userName: string;
  firstName: string;
  lastName: string;
  age: number;
  dateOfBirth: string;
  gender: string;
  bloodGroup: string;
  city: string;
  phoneNumber: string;
  createdAt?: string | "";
  modifiedAt?: string | "";
}

interface CombinedRegisterRequest {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  age: number;
  dateOfBirth: string;
  gender: string;
  bloodGroup: string;
  city: string;
  phoneNumber: string;
  createdAt?: string | "";
  modifiedAt?: string | "";
}

const UserRegistration: React.FC = () => {
  const navigate = useNavigate();
  const [error, setError] = useState<string>("");
  const [isLoading, setIsLoading] = useState(false);
  const [user, setUser] = useState<CombinedRegisterRequest>({
    username: "",
    email: "",
    password: "",
    firstName: "",
    lastName: "",
    age: 0,
    dateOfBirth: new Date().toISOString().split("T")[0],
    gender: "PREFER_NOT_TO_SAY",
    bloodGroup: "A+",
    city: "",
    phoneNumber: "",
    createdAt: "",
    modifiedAt: "",
  });

  const { login } = useAuth();

  const handleLogin = async (request: loginRequest) => {
    setError("");
    setIsLoading(true);

    try {
      const response = await api.post("/api/auth/login", request);
      const { token } = response.data;
      login(token);
    } catch (error: any) {
      setError(error.response?.data || "Failed to login. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  const formatDateForInput = (dateString: string) => {
    if (!dateString) return "";
    // Handle ISO date string format
    if (dateString.includes("T")) {
      return dateString.split("T")[0];
    }
    // Handle other date formats
    const date = new Date(dateString);
    return date.toISOString().split("T")[0];
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError("");
    setIsLoading(true);

    try {
      const loginInfo: loginRequest = {
        username: user.username,
        password: user.password,
      };
      const registerInfo: userRegisterRequest = {
        username: user.username,
        email: user.email,
        password: user.password,
      };
      const donorInfo: donorRegisterRequest = {
        userName: user.username,
        firstName: user.firstName,
        lastName: user.lastName,
        dateOfBirth: user.dateOfBirth,
        age: user.age,
        city: user.city,
        bloodGroup: user.bloodGroup,
        gender: user.gender,
        phoneNumber: user.phoneNumber,
        createdAt: user.createdAt,
        modifiedAt: user.modifiedAt,
      };

      const userResponse = await api.post("/api/auth/register", registerInfo);
      console.log(userResponse);
      if (userResponse.status !== 200) {
        setError("Failed to register user: " + userResponse.data.message);
        throw new Error("Failed to register user");
      }
      await handleLogin(loginInfo);
      await api.post("api/v1/donor/add", donorInfo);

      navigate("/donor-dashboard");
    } catch (error: any) {
      setError(error.response?.data || "Error during registration");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className='auth-container'>
      <form onSubmit={handleSubmit} className='auth-form'>
        <h2>Create Account</h2>
        {error && <div className='error-message'>{error}</div>}

        <label htmlFor='username'>Username</label>
        <input
          type='text'
          id='username'
          name='username'
          value={user.username}
          onChange={handleChange}
          required
          minLength={3}
          maxLength={50}
          placeholder='Choose a username'
          autoComplete='username'
        />

        <label htmlFor='email'>Email</label>
        <input
          type='email'
          id='email'
          name='email'
          value={user.email}
          onChange={handleChange}
          required
          maxLength={50}
          placeholder='Enter your email'
          autoComplete='email'
        />

        <label htmlFor='password'>Password</label>
        <input
          type='password'
          id='password'
          name='password'
          value={user.password}
          onChange={handleChange}
          required
          maxLength={120}
          placeholder='Create a password'
          autoComplete='new-password'
        />

        <label htmlFor='firstName'>First Name</label>
        <input
          type='text'
          name='firstName'
          placeholder={"First Name"}
          value={user.firstName}
          onChange={handleChange}
          minLength={2}
          maxLength={50}
          required
        />

        <label htmlFor='lastName'>Last Name</label>
        <input
          type='text'
          name='lastName'
          value={user.lastName}
          onChange={handleChange}
          placeholder={"Last Name"}
          minLength={2}
          maxLength={50}
          required
        />

        <label htmlFor='dob'>Date Of Birth</label>
        <input
          type='date'
          name='dateOfBirth'
          placeholder='DOB'
          value={formatDateForInput(user.dateOfBirth)}
          onChange={handleChange}
          required
        />

        <label htmlFor='gender'>Gender</label>
        <select
          name='gender'
          onChange={handleChange}
          value={user.gender}
          required
        >
          <option value='PREFER_NOT_TO_SAY'>I prefer not to say.</option>
          <option value='MALE'>Male</option>
          <option value='FEMALE'>Female</option>
          <option value='OTHER'>Other</option>
        </select>

        <label htmlFor='bloodGroup'>Blood Group</label>
        <select
          name='bloodGroup'
          onChange={handleChange}
          value={user.bloodGroup}
          required
        >
          <option value='A+'>A+</option>
          <option value='A-'>A-</option>
          <option value='B+'>B+</option>
          <option value='B-'>B-</option>
          <option value='AB+'>AB+</option>
          <option value='AB-'>AB-</option>
          <option value='O+'>O+</option>
          <option value='O-'>O-</option>
        </select>

        <label htmlFor='city'>City</label>
        <input
          type='text'
          name='city'
          placeholder={"City of Origin"}
          value={user.city}
          onChange={handleChange}
          required
        />

        <label htmlFor='phoneNumber'>Phone Number</label>
        <input
          type='tel'
          name='phoneNumber'
          placeholder={"Phone Number"}
          value={user.phoneNumber}
          onChange={handleChange}
          required
        />

        <button type='submit' disabled={isLoading} className='register-button'>
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
