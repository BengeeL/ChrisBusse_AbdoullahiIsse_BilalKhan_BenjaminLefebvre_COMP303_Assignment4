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

    public List<BloodBank> getAllBloodBanks() {
        return bloodBankRepository.findAll();
    }

    public List<BloodBank> getBloodBanksByName(final String name) {
        return bloodBankRepository
                .findBloodBanksByBloodBankName(name);
    }

    public BloodBank getBloodBankById(final Long id) throws EntityNotFoundException {
        // @note: throws EntityNotFoundException
        return findBloodBankOrThrowException(id);
    }

    public BloodBank getBloodBankByName(final String name) {
        return bloodBankRepository
                .findBloodBankByBloodBankName(name);
    }

    public BloodBank saveBloodBank(final @Valid BloodBank bloodBank) {
        return bloodBankRepository
                .save(bloodBank);
    }

    public void saveBloodBanks(final List<@Valid BloodBank> bloodBanks) {
        bloodBankRepository
                .saveAll(bloodBanks);
    }

    public void updateBloodBank(final @Valid BloodBank bloodBank, final Long id) throws EntityNotFoundException  {
        // @note: throws EntityNotFoundException
        var bloodBankToUpdate = findBloodBankOrThrowException(id);

        updateBloodBankAttributes(bloodBankToUpdate, bloodBank);
        bloodBankRepository.save(bloodBankToUpdate);
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
