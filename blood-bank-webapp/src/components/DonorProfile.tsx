import React, { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import api from "../utils/api";

interface Donor {
  userName: string;
  id?: number;
  firstName?: string;
  lastName?: string | null;
  age?: number | null;
  dateOfBirth: string;
  gender: string;
  bloodGroup?: string;
  city?: string | null;
  phoneNumber?: string | null;
  createdAt?: string;
  modifiedAt?: string;
}

interface DonorProfileProps {
  onBloodGroupChange?: (bloodGroup: string) => void;
}

const DonorProfile: React.FC<DonorProfileProps> = ({ onBloodGroupChange }) => {
  const [donor, setDonor] = useState<Donor | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const { user } = useAuth();

  const formatDateForInput = (dateString: string) => {
    if (!dateString) return '';
    // Handle ISO date string format
    if (dateString.includes('T')) {
      return dateString.split('T')[0];
    }
    // Handle other date formats
    const date = new Date(dateString);
    return date.toISOString().split('T')[0];
  };

  useEffect(() => {
    let isMounted = true;

    const getDonor = async () => {
      if (!user?.username || isLoading) return;
      
      try {
        setIsLoading(true);
        setError(null);
        
        console.log("Fetching all donors...");
        const response = await api.get("/api/v1/donor/find/all");
        console.log("All donors response:", response.data);

        console.log("Looking for donor with username:", user.username);
        const existingDonor = response.data.payload?.find(
          (d: Donor) => d.userName === user.username
        );

        if (existingDonor) {
          if (isMounted) {
            setDonor(existingDonor as Donor);
            onBloodGroupChange?.(existingDonor.bloodGroup || '');
          }
        } else {
          alert("No existing donor found with name" + user.username + "!!!");
        }
      } catch (error: any) {
        console.error("Error creating donor:", error);
        console.error("Error response:", error.response?.data);
        alert(error)
        if (isMounted) {
          setError(error.response?.data?.message || error.message || "An error occurred");
        }
      } finally {
        if (isMounted) setIsLoading(false);
      }
    };

    getDonor();

    return () => {
      isMounted = false;
    };
  }, [user?.username]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    if (donor) {
      const updatedDonor = { ...donor, [e.target.name]: e.target.value };
      setDonor(updatedDonor);
      if (e.target.name === 'bloodGroup') {
        onBloodGroupChange?.(e.target.value);
      }
    }
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!donor || !donor.id) {
      alert("Error: Donor ID is missing");
      return;
    }

    try {
      const updateResponse = await api.put(
        `/api/v1/donor/update`,
        donor
      );
      console.log("Update response:", updateResponse.data);
      if (updateResponse.data.payload) {
        setDonor(updateResponse.data.payload);
        alert("Profile updated successfully!");
      }
    } catch (error: any) {
      console.error("Error updating profile:", error);
      console.error("Error details:", {
        message: error.message,
        response: error.response?.data,
        status: error.response?.status
      });
      alert(error.response?.data?.message || "Failed to update profile. Please try again.");
    }
  };

  if (isLoading) {
    return <div>Loading... {error && <div className="error">{error}</div>}</div>;
  }

  if (error) {
    return <div className="error">{error}</div>;
  }

  return (
    <form onSubmit={handleSubmit} className='dashboard-form'>

      <div>
        <label htmlFor='firstName'>First Name</label>
        <input
          type='text'
          name='firstName'
          value={donor?.firstName || ''}
          onChange={handleChange}
          minLength={2}
          maxLength={50}
          required
        />
      </div>

      <div>
        <label htmlFor='lastName'>Last Name</label>
        <input
          type='text'
          name='lastName'
          value={donor?.lastName || ''}
          onChange={handleChange}
          minLength={2}
          maxLength={50}
          required
        />
      </div>

      <div>
        <label htmlFor='dob'>Date Of Birth</label>
        <input
          type='date'
          name='dateOfBirth'
          placeholder='DOB'
          value={formatDateForInput(donor?.dateOfBirth || '')}
          onChange={handleChange}
          required
        />
      </div>

      <div>
        <label htmlFor='gender'>Gender</label>
        <select
          name='gender'
          onChange={handleChange}
          value={donor?.gender || ''}
          required
        >
          <option value='PREFER_NOT_TO_SAY'>I prefer not to say.</option>
          <option value='MALE'>Male</option>
          <option value='FEMALE'>Female</option>
          <option value='OTHER'>Other</option>
        </select>
      </div>

      <div>
        <label htmlFor='bloodGroup'>Blood Group</label>
        <select
          name='bloodGroup'
          onChange={handleChange}
          value={donor?.bloodGroup || ''}
          required
        >
          <option value='A+'>A+</option>
          <option value='A-'>A-</option>
          <option value='B+'>B+</option>
          <option value='B-'>B-</option>
          <option value='AB+'>AB+</option>
          <option value='AB-'>AB-</option>
          <option value='O+'>O+</option>
          <option value='O-'>O-</option>
        </select>
      </div>

      <div>
        <label htmlFor='city'>City</label>
        <input
          type='text'
          name='city'
          value={donor?.city || ''}
          onChange={handleChange}
          required
        />
      </div>

      <div>
        <label htmlFor='phoneNumber'>Phone Number</label>
        <input
          type='tel'
          name='phoneNumber'
          value={donor?.phoneNumber || ''}
          onChange={handleChange}
          required
        />
      </div>

      <button type='submit' className='dashboard-button'>Update Profile</button>
    </form>
  );
};

export default DonorProfile;
