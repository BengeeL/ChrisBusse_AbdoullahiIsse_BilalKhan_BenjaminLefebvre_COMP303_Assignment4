import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import api from "../utils/api";

interface ScheduleDonationProps {
  storeDonation: (donation: string) => void;
  bloodGroup: string;
}

interface BloodBank {
  id: number;
  bloodBankName: string;
  address: string;
  city: string;
  phoneNumber: string;
  website: string;
  email: string;
}

export default function ScheduleDonation({
  storeDonation,
  bloodGroup,
}: ScheduleDonationProps) {
  const [bloodBank, setBloodBank] = useState("");
  const [date, setDate] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [availableBloodBanks, setAvailableBloodBanks] = useState<BloodBank[]>(
    []
  );

  const auth = useAuth();

  useEffect(() => {
    getBloodBanks();
    console.log(localStorage.getItem("token"));
  }, []);

  const getBloodBanks = async () => {
    if (isLoading) return;

    try {
      setIsLoading(true);

      const response = await api.get("/api/v1/blood-bank/find/all");
      setAvailableBloodBanks(response.data?.payload);
    } catch (error) {
      console.error("Error fetching blood banks:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);

    try {
      const newDonation = {
        id: Date.now(),
        bloodBank,
        date,
        bloodGroup,
        status: "Scheduled",
      };

      const existingDonations = localStorage.getItem(
        auth.user?.username + "-donations"
      );
      const donations = existingDonations ? JSON.parse(existingDonations) : [];
      donations.push(newDonation);

      storeDonation(JSON.stringify(donations));
      setBloodBank("");
      setDate("");
    } catch (error) {
      console.error("Error scheduling donation:", error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className='dashboard-form'>
      <div>
        <label htmlFor='bloodBank'>Blood Bank Location</label>
        <select
          id='bloodBank'
          value={bloodBank}
          onChange={(e) => setBloodBank(e.target.value)}
          required
        >
          <option value=''>Select a blood bank</option>
          {availableBloodBanks.length > 0 &&
            availableBloodBanks.map((bank, index) => (
              <option key={index} value={bank.bloodBankName}>
                {bank.bloodBankName}
              </option>
            ))}
        </select>
      </div>

      <div>
        <label htmlFor='date'>Preferred Date</label>
        <input
          type='date'
          id='date'
          value={date}
          onChange={(e) => setDate(e.target.value)}
          min={new Date().toISOString().split("T")[0]}
          required
        />
      </div>

      <button
        type='submit'
        className='dashboard-button'
        disabled={isLoading || !bloodBank || !date}
      >
        {isLoading ? "Scheduling..." : "Schedule Donation"}
      </button>
    </form>
  );
}
