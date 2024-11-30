import DonorProfile from "../components/DonorProfile";
import ScheduleDonation from "../components/ScheduleDonation";
import DonationHistory from "../components/DonationHistory";

export default function DonorDashboard() {
  return (
    <div className='dashboard-container'>
      {/* <h1>Blood Donation Site</h1>
      <nav>
        <ul className={"navbar"}>

          <li>
            <Link to='/profile'>Profile</Link>
          </li> */}
      {/*<li><Link to="/schedule">Schedule Donation</Link></li>*/}
      {/*<li><Link to="/history">Donation History</Link></li>*/}
      {/* </ul>
      </nav> */}

      <div className='dashboard'>
        <div className='dashboard-left-panel'>
          <div className='dashboard-profile'>
            <DonorProfile />
          </div>
        </div>

        <div className='dashboard-right-panel'>
          <div className='dashboard-donation'>
            <ScheduleDonation />
          </div>
          <div className='dashboard-history'>
            <DonationHistory />
          </div>
        </div>
      </div>
    </div>
  );
}
