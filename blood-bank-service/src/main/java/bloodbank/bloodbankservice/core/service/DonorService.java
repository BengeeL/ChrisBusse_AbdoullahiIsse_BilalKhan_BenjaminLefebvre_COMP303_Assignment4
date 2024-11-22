package bloodbank.bloodbankservice.core.service;

import bloodbank.bloodbankservice.core.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DonorService {
    private final DonorRepository donorRepository;

    @Autowired
    public DonorService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    // TODO: Add service logic here.
}
