import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Authentication from "./pages/authentication";
import DonorDashboard from "./pages/DonorDashboard";

const App: React.FC = () => {
  return (
    <Router>
      <div>
        {location.pathname === "/authentication" ? <></> : <></>}

        <Routes>
          <Route path='/' element={<DonorDashboard />} />
          <Route path='/authentication' element={<Authentication />} />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
