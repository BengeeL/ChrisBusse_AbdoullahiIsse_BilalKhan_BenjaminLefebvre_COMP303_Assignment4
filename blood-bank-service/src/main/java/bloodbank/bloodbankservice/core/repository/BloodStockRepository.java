package bloodbank.bloodbankservice.core.repository;

import bloodbank.bloodbankservice.core.entities.BloodStock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodStockRepository extends CrudRepository<BloodStock, Long> {
    // TODO: Add anything else within here.
}
