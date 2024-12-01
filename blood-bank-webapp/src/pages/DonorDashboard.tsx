import DonorProfile from "../components/DonorProfile";
import ScheduleDonation from "../components/ScheduleDonation";
import DonationHistory from "../components/DonationHistory";
import {useState} from "react";
//
// interface Donation {
//   id: number;
//   bloodBank: string;
//   date: string;
//   bloodGroup: string;
// }

export default function DonorDashboard() {

  const [storedDonations, SetDonations] = useState<[]>(JSON.parse(localStorage.getItem('donations') as string) || [])

  const storeDonations = (newDonation:string) => {
    localStorage.setItem('donations', newDonation)
    SetDonations(JSON.parse(localStorage.getItem('donations') as string) || [])
  }

  return (
    <div className='dashboard-container'>
      <div className='dashboard'>
        <div className='dashboard-left-panel'>
          <div className='dashboard-profile'>
            <DonorProfile />
          </div>
        </div>

        <div className='dashboard-right-panel'>
          <div className='dashboard-donation'>
            <ScheduleDonation  addDonation={storeDonations}/>
          </div>
          <div className='dashboard-history'>
            <DonationHistory donations={storedDonations} />
          </div>
        </div>
      </div>
    </div>
  );
}
