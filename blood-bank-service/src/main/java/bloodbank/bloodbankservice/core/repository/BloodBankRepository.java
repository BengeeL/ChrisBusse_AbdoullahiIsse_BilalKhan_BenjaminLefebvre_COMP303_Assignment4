package bloodbank.bloodbankservice.core.repository;

import bloodbank.bloodbankservice.core.entities.BloodBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodBankRepository extends JpaRepository<BloodBank, Long> {
    List<BloodBank> findBloodBanksByBloodBankName(final String name);
    BloodBank findBloodBankByBloodBankName(final String name);
}
