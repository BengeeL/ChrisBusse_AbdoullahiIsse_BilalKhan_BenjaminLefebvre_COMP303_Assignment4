import axios from "axios";
import React, { useEffect, useState } from "react";

interface Donation {
  id: number;
  date: string;
  bloodGroup: string;
}

const DonationHistory: React.FC = () => {
  const [donations, setDonations] = useState<Donation[]>([]);

  useEffect(() => {
    const getAllDonations = async () => {
      try {
        // donations and donation history not done on the backend yet
        const response = await axios.get<Donation[]>(
          "http://localhost:8080/api/v1/blood-donor/donations"
        );
        setDonations(response.data);
      } catch (error) {
        console.error("Error fetching donation history:", error);
      }
    };

    getAllDonations();
  }, []);

  return (
    <div>
      <h2>Donation History</h2>
      <ul>
        {donations.map((donation) => (
          <li key={donation.id}>
            Date: {donation.date}, Blood Group: {donation.bloodGroup}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default DonationHistory;
