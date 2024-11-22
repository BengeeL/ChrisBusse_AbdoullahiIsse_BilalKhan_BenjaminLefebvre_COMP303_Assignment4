package bloodbank.bloodbankservice.core.repository;

import bloodbank.bloodbankservice.core.entities.Donor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonorRepository extends CrudRepository<Donor, Long> {
    // TODO: Add anything else within here.
}
