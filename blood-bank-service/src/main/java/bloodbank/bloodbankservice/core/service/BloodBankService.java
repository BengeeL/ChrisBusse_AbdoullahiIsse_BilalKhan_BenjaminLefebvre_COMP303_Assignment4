package bloodbank.bloodbankservice.core.service;

import bloodbank.bloodbankservice.core.repository.BloodBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BloodBankService {
    private final BloodBankRepository bloodBankRepository;

    @Autowired
    public BloodBankService(final BloodBankRepository bloodBankRepository) {
        this.bloodBankRepository = bloodBankRepository;
    }

    // TODO: Add service logic here.
}
