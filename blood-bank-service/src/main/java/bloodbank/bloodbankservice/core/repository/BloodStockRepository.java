package bloodbank.bloodbankservice.core.repository;

import bloodbank.bloodbankservice.core.entities.BloodStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodStockRepository extends JpaRepository<BloodStock, Long> {
    List<BloodStock> findBloodStocksByBloodGroup(final String bloodGroup);
    BloodStock findBloodStockByBloodGroup(final String bloodGroup);
}
