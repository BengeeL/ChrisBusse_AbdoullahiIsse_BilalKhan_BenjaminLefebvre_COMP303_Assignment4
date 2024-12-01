import { useState } from "react";
import UserRegistration from "../components/UserRegistration.tsx";
import UserLogin from "../components/UserLogin.tsx";

export default function Authentication() {
  const [display, setDisplay] = useState("login");

  return (
    <div className='auth-box'>
      {display === "register" ? (
        <div className="auth-box">
          <UserRegistration />
          <a
            onClick={() => setDisplay("login")}
            className='authentication-nav-link'
          >
            Already Registered?
          </a>
        </div>
      ) : (
        <div className="auth-box">
          <UserLogin />
          <a
            onClick={() => setDisplay("register")}
            className='authentication-nav-link'
          >
            Need to Register?
          </a>
        </div>
      )}
    </div>
  );
}
