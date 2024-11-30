import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import DonorRegistration from './components/DonorRegistration';
import DonorLogin from './components/DonorLogin';
import DonorProfile from './components/DonorProfile';
// import ScheduleDonation from './components/ScheduleDonation';
// import DonationHistory from './components/DonationHistory';

const App: React.FC = () => {
    return (
        <Router>
            <div>
                <h1>Blood Donation Site</h1>
                <nav>
                    <ul className={"navbar"}>
                        {/* need to figure out how to set active style*/}
                        <li><Link to="/">Home</Link></li>
                        <li><Link to="/register">Register</Link></li>
                        <li><Link to="/login">Login</Link></li>
                        <li><Link to="/profile">Profile</Link></li>
                        {/*<li><Link to="/schedule">Schedule Donation</Link></li>*/}
                        {/*<li><Link to="/history">Donation History</Link></li>*/}
                    </ul>
                </nav>
                <Routes>
                    <Route path="/" element={<h2>Welcome to the Blood Donation App</h2>} />
                    <Route path="/register" element={<DonorRegistration />} />
                    <Route path="/login" element={<DonorLogin />} />
                    <Route path="/profile" element={<DonorProfile />} />
                    {/*<Route path="/schedule" element={<ScheduleDonation />} />*/}
                    {/*<Route path="/history" element={<DonationHistory />} />*/}
                </Routes>
            </div>
        </Router>
    );
};

export default App;
