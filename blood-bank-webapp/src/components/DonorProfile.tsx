import React, { useEffect, useState } from 'react';
import axios from 'axios';

interface Donor {
    id: number;
    firstName: string;
    lastName: string;
    age: string;
    dateOfBirth: string;
    Gender: string;
    bloodGroup: string;
    city: string;
    phoneNumber: string;
}

const DonorProfile: React.FC = () => {
    const [donor, setDonor] = useState<Donor | null>(null);

    useEffect(() => {
        const getDonor = async () => {
            try {
                // can't run backend, login not done so can't get currently logged in user.
                // needs to be edited later
                const response = await axios.get<Donor>('http://localhost:8080/api/v1/blood-donor/find/'+donor?.id);
                setDonor(response.data);
            } catch (error) {
                console.error('Error fetching donor profile:', error);
            }
        };

        getDonor();
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        if (donor) {
            setDonor({ ...donor, [e.target.name]: e.target.value });
        }
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (donor) {
            try {
                // can't connect to db though :(
                await axios.put(`http://localhost:8080/api/v1/blood-donor/find/update`, donor,  {params: {id: donor.id}});
                alert('Profile updated successfully!');
            } catch (error) {
                console.error('Error updating donor profile:', error);
            }
        }
    };

    if (!donor) return <div>Loading...</div>;

    return (
        <form onSubmit={handleSubmit}>
            <label htmlFor="firstName">First Name</label>
            <input type="text" name="firstName" value={donor.firstName} onChange={handleChange} required/>
            <label htmlFor="lastName">Last Name</label>
            <input type="text" name="lastName" value={donor.lastName} onChange={handleChange} required/>
            <label htmlFor="age">Age</label>
            <input type="date" name="age" value={donor.age} onChange={handleChange} required/>
            <label htmlFor="dob">Date Of Birth</label>
            <input type="date" name="dob" placeholder="DOB" value={donor.dateOfBirth} onChange={handleChange} required/>
            <label htmlFor="gender">Gender</label>
            <select name="gender" onChange={handleChange} value={donor.Gender} required>
                <option value="PREFER_NOT_TO_SAY">I prefer not to say.</option>
                <option value="MALE">Male</option>
                <option value="FEMALE">Female</option>
                <option value="OTHER">Other</option>
            </select>
            <label htmlFor="bloodGroup">Blood Group</label>
            <select name="bloodGroup" id="bloodGroup">
                <option value="A+">A+</option>
                <option value="B+">B+</option>
                <option value="AB+">AB+</option>
                <option value="O+">O+</option>
                <option value="A-">A-</option>
                <option value="B-">B-</option>
                <option value="AB-">AB-</option>
                <option value="O-">O-</option>
            </select>
            <label htmlFor="city">City</label>
            <input type="text" name="city" value={donor.city} onChange={handleChange} required/>
            <label htmlFor="phone">Phone</label>
            <input type="text" name="phone" value={donor.phoneNumber} onChange={handleChange} required/>
            <button type="submit" className={"submit"}>Update Profile</button>
        </form>
    );
};

export default DonorProfile;
