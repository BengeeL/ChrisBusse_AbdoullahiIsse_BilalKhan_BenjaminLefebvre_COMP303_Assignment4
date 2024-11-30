import React, { useState } from 'react';
import axios from 'axios';

interface Donor {
    firstName: string;
    lastName: string;
    age: number;
    dateOfBirth: string;
    Gender: string;
    bloodGroup: string;
    city: string;
    phoneNumber: string;
}

const DonorRegistration: React.FC = () => {
    const [donor, setDonor] = useState<Donor>({
        firstName: '',
        lastName: '',
        age: 0,
        dateOfBirth: '',
        Gender: 'OTHER', // it's an enum with multiple options on the backend,
        // not sure if i need to provide an int instead
        bloodGroup: '',
        city: '',
        phoneNumber: ''
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        setDonor({ ...donor, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            // backend wont run for me, untested
            await axios.post('http://localhost:8080/api/v1/blood-donor/add', donor);
            alert('Donor registered successfully!');
        } catch (error) {
            console.error('Error registering donor:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <label htmlFor="firstName">First Name</label>
            <input type="text" name="firstName" placeholder="First Name" onChange={handleChange} required/>
            <label htmlFor="lastName">Last Name</label>
            <input type="text" name="lastName" placeholder="Last Name" onChange={handleChange} required/>
            <label htmlFor="age">Age</label>
            <input type="number" name="age" placeholder="0" onChange={handleChange} required/>
            <label htmlFor="dateOfBirth">Date Of Birth</label>
            <input type="date" name="dateOfBirth" placeholder="Date Of Birth" onChange={handleChange} required/>
            <label htmlFor="Gender">Gender</label>
            <select name="Gender" onChange={handleChange} required>
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
            <input type="text" name="city" placeholder="City" onChange={handleChange} required/>
            <label htmlFor="phoneNumber">Phone</label>
            <input type="text" name="phoneNumber" placeholder="Phone" onChange={handleChange} required/>
            <button type="submit" className={"submit"}>Register</button>
        </form>
    );
};

export default DonorRegistration;
