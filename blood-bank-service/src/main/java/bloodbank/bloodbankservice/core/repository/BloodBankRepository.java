package bloodbank.bloodbankservice.core.repository;

import bloodbank.bloodbankservice.core.entities.BloodBank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodBankRepository extends CrudRepository<BloodBank, Long> {
    // TODO: Add anything else within here.
}
