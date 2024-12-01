interface Donation {
    id: number;
    bloodBank: string;
    date: string;
    bloodGroup: string;
}

interface DonationHistoryProps {
    donations: Donation[]
}

const DonationHistory: React.FC<DonationHistoryProps> = ({donations}: DonationHistoryProps) => {

    return (
        <div>
            {donations.map((donation: Donation, index: number) => (
                <p key={`${donation.id}-${index}`} className="donation">
                    Date: {donation.date},
                    Blood Group: {donation.bloodGroup},
                    Blood Bank: {donation.bloodBank}
                </p>
            ))}
        </div>
    );
};

export default DonationHistory;
