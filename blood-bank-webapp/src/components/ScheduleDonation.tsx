import React, {useState} from "react";

interface Donation {
  id: number;
  bloodBank: string;
  date: string;
  bloodGroup: string;
}

interface ScheduleDonationProps {
    addDonation: (newDonation: string) => void
}

const ScheduleDonation: React.FC<ScheduleDonationProps> = ({addDonation}) => {
    const [donation, setDonation] = useState<Donation>({
        id: 0,
        bloodBank: "Saint Obama Blood Centre",
        date: "",
        bloodGroup: "A+",
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        setDonation({...donation, [e.target.name]: e.target.value});
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
          const storedDonations = JSON.parse(localStorage.getItem('donations') as string) || []
          const updatedDonations = [...storedDonations, donation]
          addDonation(JSON.stringify(updatedDonations))
          console.log(updatedDonations.length)
          setDonation({id: updatedDonations.length, date: "", bloodBank: "Saint Obama Blood Centre", bloodGroup: "A+"})
        } catch (error) {
            console.error("Error scheduling donation:", error);
            alert(error)
        }
    };

    return (
        <div>
            <form onSubmit={handleSubmit} className='dashboard-form'>
                <h2>Schedule Donation</h2>

                <input
                    type='date'
                    name='date'
                    value={donation.date}
                    onChange={handleChange}
                    required
                    min={new Date().toISOString().split('T')[0]}
                />
                <select name='bloodBank' id='bloodBank' value={donation.bloodBank} onChange={handleChange}>
                    <option value='Saint Obama Blood Centre'>Saint Obama Blood Centre</option>
                    <option value='TD Blood Bank'>TD Blood Bank</option>
                    <option value='RBC Hospital'>RBC Hospital</option>
                </select>
                <select name='bloodGroup' id='bloodGroup' value={donation.bloodGroup} onChange={handleChange}>
                    <option value='A+'>A+</option>
                    <option value='B+'>B+</option>
                    <option value='AB+'>AB+</option>
                    <option value='O+'>O+</option>
                    <option value='A-'>A-</option>
                    <option value='B-'>B-</option>
                    <option value='AB-'>AB-</option>
                    <option value='O-'>O-</option>
                </select>
                <button type='submit' className={"submit"}>
                    Schedule Donation
                </button>
            </form>
        </div>
    );
};

export default ScheduleDonation;
