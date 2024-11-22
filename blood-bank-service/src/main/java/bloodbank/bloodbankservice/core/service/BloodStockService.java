package bloodbank.bloodbankservice.core.service;

import bloodbank.bloodbankservice.core.repository.BloodStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BloodStockService {
    private final BloodStockRepository bloodStockRepository;

    @Autowired
    public BloodStockService(final BloodStockRepository bloodStockRepository) {
        this.bloodStockRepository = bloodStockRepository;
    }

    // TODO: Add service logic here.
}
