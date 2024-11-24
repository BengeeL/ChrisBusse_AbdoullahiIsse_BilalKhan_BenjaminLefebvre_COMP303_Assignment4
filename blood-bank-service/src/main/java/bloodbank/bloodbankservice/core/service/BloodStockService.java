package bloodbank.bloodbankservice.core.service;

import bloodbank.bloodbankservice.core.entities.BloodStock;
import bloodbank.bloodbankservice.core.repository.BloodStockRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing BloodStock entities.
 * @author Muhammad Bilal Khan
 * @version 1.0.0
 * @since 1.0.0
 * @see bloodbank.bloodbankservice.core.entities.BloodStock
 * @see bloodbank.bloodbankservice.core.repository.BloodStockRepository
 * @see bloodbank.bloodbankservice.core.dtos.BloodStockDto
 */
@Service
public class BloodStockService {
    //region DEFAULTS
    private static final String BLOOD_STOCK_NOT_FOUND = "BloodStock with id %s not found";
    private static final String BLOOD_STOCK_EXISTS = "BloodStock with id %s, blood type %s, and status %s already exists";
    //endregion

    private final BloodStockRepository bloodStockRepository;

    @Autowired
    public BloodStockService(final BloodStockRepository bloodStockRepository) {
        this.bloodStockRepository = bloodStockRepository;
    }

    public List<BloodStock> findAllBloodStocks() {
        return bloodStockRepository
                .findAll();
    }

    public BloodStock findBloodStockById(final Long id) throws EntityNotFoundException {
        return findBloodStockOrThrowException(id);
    }

    public BloodStock findBloodStockByBloodGroup(final String bloodGroup) {
        return bloodStockRepository
                .findBloodStockByBloodGroup(bloodGroup);
    }

    public List<BloodStock> findBloodStocksByBloodGroup(final String bloodGroup) {
        return bloodStockRepository
                .findBloodStocksByBloodGroup(bloodGroup);
    }

    public void saveBloodStock(final @Valid BloodStock bloodStock) {
        bloodStockRepository
                .save(bloodStock);
    }

    public void saveBloodStocks(final List<@Valid BloodStock> bloodStocks) {
        bloodStockRepository
                .saveAll(bloodStocks);
    }

    public void updateBloodStock(final @Valid BloodStock bloodStock, final Long id) throws EntityNotFoundException {
        // @note: throws EntityNotFoundException
        var bloodStockToUpdate = findBloodStockOrThrowException(id);

        updateBloodStockAttributes(bloodStockToUpdate, bloodStock);
        bloodStockRepository
                .save(bloodStockToUpdate);
    }

    public Boolean deleteBloodStock(final Long id) throws EntityNotFoundException {
        // @note: throws EntityNotFoundException
        var bloodStockToDelete = findBloodStockOrThrowException(id);

        bloodStockRepository
                .delete(bloodStockToDelete);
        return true;
    }

    public Boolean deleteBloodStocks(final List<Long> ids) throws EntityNotFoundException {
        for (var id: ids){
            if (!bloodStockRepository.existsById(id)) {
                throw new EntityNotFoundException(String.format(BLOOD_STOCK_NOT_FOUND, id));
            } else {
                bloodStockRepository
                        .deleteById(id);
            }
        }

        return true;
    }

    //region Class Helpers
    private BloodStock findBloodStockOrThrowException(final Long id) throws EntityNotFoundException {
        return bloodStockRepository
                    .findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(String.format(BLOOD_STOCK_NOT_FOUND, id)));
    }

    private void updateBloodStockAttributes(
            final BloodStock target,
            final BloodStock source
    ) {
        target.setBloodGroup(source.getBloodGroup());
        target.setQuantity(source.getQuantity());
        target.setBestBefore(source.getBestBefore());
        target.setStatus(source.getStatus());
        target.setModifiedAt(source.getModifiedAt());
    }
    //endregion
}
