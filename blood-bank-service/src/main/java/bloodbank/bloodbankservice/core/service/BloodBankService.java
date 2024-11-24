package bloodbank.bloodbankservice.core.service;

import bloodbank.bloodbankservice.core.entities.BloodBank;
import bloodbank.bloodbankservice.core.repository.BloodBankRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Service class for managing BloodBank entities.
 * @author Muhammad Bilal Khan
 * @version 1.0.0
 * @since 1.0.0
 * @see bloodbank.bloodbankservice.core.entities.BloodBank
 * @see bloodbank.bloodbankservice.core.repository.BloodBankRepository
 * @see bloodbank.bloodbankservice.core.dtos.BloodBankDto
 */
@Service
public class BloodBankService {
    //region DEFAULTS
    private static final String BLOOD_BANK_NOT_FOUND = "BloodBank with provided %s is not found";
    //endregion

    private final BloodBankRepository bloodBankRepository;

    @Autowired
    public BloodBankService(final BloodBankRepository bloodBankRepository) {
        this.bloodBankRepository = bloodBankRepository;
    }

    public List<BloodBank> findAllBloodBanks() {
        return bloodBankRepository.findAll();
    }

    public BloodBank findBloodBankByBloodBankName(final String name) {
        return bloodBankRepository
                .findBloodBankByBloodBankName(name);
    }

    // @note: This method is not used yet.
    public List<BloodBank> findBloodBanksByBloodBankName(final String name) {
        return bloodBankRepository
                .findBloodBanksByBloodBankName(name);
    }

    public BloodBank findBloodBankById(final Long id) throws EntityNotFoundException {
        // @note: throws EntityNotFoundException
        return findBloodBankOrThrowException(id);
    }

    public BloodBank getBloodBankByName(final String name) {
        return bloodBankRepository
                .findBloodBankByBloodBankName(name);
    }

    public Boolean saveBloodBank(final @Valid BloodBank bloodBank) {
        bloodBankRepository.save(bloodBank);
        return true;
    }

    public Boolean saveBloodBanks(final List<@Valid BloodBank> bloodBanks) {
        bloodBankRepository
                .saveAll(bloodBanks);
        return true;
    }

    public Boolean updateBloodBank(final @Valid BloodBank bloodBank, final Long id) throws EntityNotFoundException  {
        // @note: throws EntityNotFoundException
        var bloodBankToUpdate = findBloodBankOrThrowException(id);

        updateBloodBankAttributes(bloodBankToUpdate, bloodBank);
        bloodBankRepository.save(bloodBankToUpdate);
        return true;
    }

    public Boolean deleteBloodBank(final Long id) throws EntityNotFoundException {
        // @note: throws EntityNotFoundException
        var bloodBankToDelete = findBloodBankOrThrowException(id);

        bloodBankRepository.delete(bloodBankToDelete);
        return true;
    }

    public Boolean deleteBloodBanks(final List<Long> ids) throws EntityNotFoundException {
        for (var id: ids) {
            if (!bloodBankRepository.existsById(id)) {
                throw new EntityNotFoundException(String.format(BLOOD_BANK_NOT_FOUND, id));
            } else {
                bloodBankRepository
                        .deleteById(id);
            }
        }
        return true;
    }

    //region Class Helpers
    private BloodBank findBloodBankOrThrowException(final Long id) throws EntityNotFoundException {
        return bloodBankRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(BLOOD_BANK_NOT_FOUND, id)));
    }

    private void updateBloodBankAttributes(
            final BloodBank target,
            final BloodBank source
    ) {
        target.setBloodBankName(source.getBloodBankName());
        target.setAddress(source.getAddress());
        target.setCity(source.getCity());
        target.setPhoneNumber(source.getPhoneNumber());
        target.setWebsite(source.getWebsite());
        target.setEmail(source.getEmail());
        target.setModifiedAt(new Timestamp(System.currentTimeMillis()));
    }
    //endregion
}
