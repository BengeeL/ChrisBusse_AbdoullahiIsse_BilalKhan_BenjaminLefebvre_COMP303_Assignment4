package bloodbank.bloodbankservice.core.service;

import bloodbank.bloodbankservice.core.entities.Donor;
import bloodbank.bloodbankservice.core.entities.enums.GenderType;
import bloodbank.bloodbankservice.core.repository.DonorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Service class for managing Donor entities.
 * @author Muhammad Bilal Khan
 * @version 1.0.0
 * @since 1.0.0
 * @see bloodbank.bloodbankservice.core.entities.Donor
 * @see bloodbank.bloodbankservice.core.repository.DonorRepository
 * @see bloodbank.bloodbankservice.core.dtos.DonorDto
 */
@Service
public class DonorService {
    //region DEFAULTS
    private static final String DONOR_NOT_FOUND = "Donor with provided %s is not found";
    //endregion

    private final DonorRepository donorRepository;

    @Autowired
    public DonorService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    public List<Donor> findAllDonors() {
        return donorRepository
                .findAll();
    }

    public Donor findDonorById(final Long id) throws EntityNotFoundException {
        return findDonorOrThrowException(id);
    }

    public List<Donor> findDonorsByAge(final Integer age) {
        return donorRepository
                .findDonorsByAge(age);
    }

    public List<Donor> findDonorsByCity(final String city) {
        return donorRepository
                .findDonorsByCity(city);
    }

    public List<Donor> findDonorsByGender(final GenderType genderType) {
        return donorRepository
                .findDonorsByGender(genderType);
    }

    public void saveDonor(final @Valid Donor Donor) {
        donorRepository
                .save(Donor);
    }

    public void saveDonors(final List<@Valid Donor> Donors) {
        donorRepository
                .saveAll(Donors);
    }

    public Boolean updateDonor(final @Valid Donor Donor, final Long id) throws EntityNotFoundException {
        // @note: throws EntityNotFoundException
        var DonorToUpdate = findDonorOrThrowException(id);

        updateDonorAttributes(DonorToUpdate, Donor);
        donorRepository
                .save(DonorToUpdate);
        return true;
    }

    public Boolean deleteDonor(final Long id) throws EntityNotFoundException {
        // @note: throws EntityNotFoundException
        var DonorToDelete = findDonorOrThrowException(id);

        donorRepository
                .delete(DonorToDelete);
        return true;
    }

    public Boolean deleteDonors(final List<Long> ids) throws EntityNotFoundException {
        for (var id : ids) {
            if (!donorRepository.existsById(id)) {
                throw new EntityNotFoundException(String.format(DONOR_NOT_FOUND, id));
            } else {
                donorRepository
                        .deleteById(id);
            }
        }
        return true;
    }

    //region Class Helpers
    private Donor findDonorOrThrowException(final Long id) throws EntityNotFoundException {
        return donorRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(DONOR_NOT_FOUND, id)));
    }

    private void updateDonorAttributes(
            final Donor target,
            final Donor source
    ) {
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setAge(source.getAge());
        target.setDateOfBirth(source.getDateOfBirth());
        target.setGender(source.getGender());
        target.setCity(source.getCity());
        target.setPhoneNumber(source.getPhoneNumber());
        target.setModifiedAt(new Timestamp(System.currentTimeMillis()));
    }
    //endregion
}
