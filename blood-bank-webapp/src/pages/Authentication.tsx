import { useState } from "react";
import UserRegistration from "../components/UserRegistration.tsx";
import UserLogin from "../components/UserLogin.tsx";

export default function Authentication() {
  const [display, setDisplay] = useState("register");

  return (
    <div className='auth-box'>
      {display === "register" ? (
        <div>
          <UserRegistration />
          <a
            onClick={() => setDisplay("login")}
            className='authentication-nav-link'
          >
            Already Registered?
          </a>
        </div>
      ) : (
        <div>
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
