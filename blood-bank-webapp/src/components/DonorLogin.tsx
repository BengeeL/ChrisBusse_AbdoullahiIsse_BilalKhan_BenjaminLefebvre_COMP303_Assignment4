import React, { useState } from 'react';
import axios from 'axios';

interface Credentials {
    phoneNumber: string; // because we use phone number, no email or anything
    password: string;
}

const DonorLogin: React.FC = () => {
    const [credentials, setCredentials] = useState<Credentials>({
        phoneNumber: '',
        password: ''
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        console.log("before: " + credentials)
        setCredentials({ ...credentials, [e.target.name]: e.target.value });
        console.log("after: " + credentials)
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/api/v1/blood-donor', credentials);
            alert('Login successful!\n' + response);
            // successful login:
            // set a cookie with user id?
        } catch (error) {
            console.error('Error logging in:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input type="text" name="phoneNumber" placeholder="Phone" onChange={handleChange} required />
            <input type="password" name="password" placeholder="Password" onChange={handleChange} required />
            <button type="submit" className={"submit"}>Login</button>
        </form>
    );
};

export default DonorLogin;
