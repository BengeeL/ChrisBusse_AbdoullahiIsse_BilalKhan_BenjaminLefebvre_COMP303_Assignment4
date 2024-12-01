import DonorProfile from "../components/DonorProfile";
import ScheduleDonation from "../components/ScheduleDonation";
import DonationHistory from "../components/DonationHistory"
import { useState } from "react";
import '../styles/dashboard.css';

export default function DonorDashboard() {

  const [storedDonations, SetDonations] = useState<[]>(() => {
    try {
      const donations = localStorage.getItem('donations');
      return donations ? JSON.parse(donations) : [];
    } catch (error) {
      console.error('Error parsing donations:', error);
      return [];
    }
  });

  const [bloodGroup, setBloodGroup] = useState('');

  const storeDonations = (newDonation: string) => {
    try {
      localStorage.setItem('donations', newDonation);
      SetDonations(JSON.parse(newDonation));
    } catch (error) {
      console.error('Error storing donations:', error);
    }
  };

  return (
    <div className="dashboard-container">
      <div className="dashboard">
        <div className="dashboard-left-panel">
          <h2 className="section-header">Your Profile</h2>
          <div className="dashboard-profile">
            <DonorProfile onBloodGroupChange={setBloodGroup} />
          </div>
        </div>

        <div className="dashboard-right-panel">
          <div className="dashboard-donation">
            <h2 className="section-header">Schedule Donation</h2>
            <ScheduleDonation addDonation={storeDonations} bloodGroup={bloodGroup} />
          </div>
          <div className="dashboard-history">
            <h2 className="section-header">Donation History</h2>
            <DonationHistory donations={storedDonations} />
          </div>
        </div>
      </div>
    </div>
  );
} 
