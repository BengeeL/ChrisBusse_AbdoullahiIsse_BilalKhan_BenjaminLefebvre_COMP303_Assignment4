import React, { useState } from 'react';
// import axios from 'axios';

interface Donation {
    id: number;
    date: string;
    bloodGroup: string;
}

const user_id = 0; // replace with id of currently logged in user

const ScheduleDonation: React.FC = () => {
    const [donation, setDonation] = useState<Donation>({ id: user_id, date: '', bloodGroup:''});

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setDonation({ ...donation, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            // Can't run backend :( can't add it even if i wanted to
            // No endpoint for adding donations, needs to be changed in the model.
            // await axios.post('http://localhost:8080/api/v1/blood-donor/schedule', donation);
            alert('Donation scheduled successfully!');
        } catch (error) {
            console.error('Error scheduling donation:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input type="date" name="date" value={donation.date} onChange={handleChange} required />
            <select name="bloodGroup" id="bloodGroup" value={donation.date}>
                <option value="A+">A+</option>
                <option value="B+">B+</option>
                <option value="AB+">AB+</option>
                <option value="O+">O+</option>
                <option value="A-">A-</option>
                <option value="B-">B-</option>
                <option value="AB-">AB-</option>
                <option value="O-">O-</option>
            </select>
            <button type="submit" className={"submit"}>Schedule Donation</button>
        </form>
    );
};

export default ScheduleDonation;
