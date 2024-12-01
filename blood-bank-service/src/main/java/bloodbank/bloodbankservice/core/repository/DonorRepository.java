package bloodbank.bloodbankservice.core.repository;

import bloodbank.bloodbankservice.core.entities.Donor;
import bloodbank.bloodbankservice.core.entities.enums.GenderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {
    List<Donor> findDonorsByCity(final String city);
    List<Donor> findDonorsByGender(final GenderType gender);
    Donor findDonorByUserName(final String userName);
}
