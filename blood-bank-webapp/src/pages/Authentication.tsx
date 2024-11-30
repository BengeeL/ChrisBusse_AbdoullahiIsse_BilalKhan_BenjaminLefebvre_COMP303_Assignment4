import { useState } from "react";
import DonorRegistration from "../components/DonorRegistration";
import DonorLogin from "../components/DonorLogin";

export default function Authentication() {
  const [display, setDisplay] = useState("register");

  return (
    <div className='auth-box'>
      {display === "register" ? (
        <div>
          <DonorRegistration />
          <a
            onClick={() => setDisplay("login")}
            className='authentication-nav-link'
          >
            Already Registered?
          </a>
        </div>
      ) : (
        <div>
          <DonorLogin />
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
