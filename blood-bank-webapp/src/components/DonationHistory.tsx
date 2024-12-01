interface Donation {
    id: number;
    bloodBank: string;
    date: string;
    bloodGroup: string;
}
interface DonationHistoryProps {
    donations: []
}

const DonationHistory: React.FC<DonationHistoryProps> = ({donations}: DonationHistoryProps) => {

    return (
        <div>
            <h2>Donation History</h2>
            {donations.map((donation:Donation) => (
                <p key={donation.id} className={"donation"}>
                    Date: {donation.date},
                    Blood Group: {donation.bloodGroup},
                    Blood Bank: {donation.bloodBank}
                </p>
            ))}
        </div>
    );
};

export default DonationHistory;
