package bloodbank.bloodbankservice.core.controller;

import bloodbank.bloodbankservice.core.entities.BloodStock;
import bloodbank.bloodbankservice.core.entities.enums.StatusType;
import bloodbank.bloodbankservice.core.service.BloodStockService;
import bloodbank.bloodbankservice.core.utils.APIResponse;
import bloodbank.bloodbankservice.core.utils.APIResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Collections;
import java.util.List;


/**
 * Controller class for managing BloodBank entities.
 * @author Muhammad Bilal Khan
 * @version 1.0.0
 * @since 1.0.0
 * @see bloodbank.bloodbankservice.core.entities.BloodStock
 * @see bloodbank.bloodbankservice.core.service.BloodStockService
 * @see bloodbank.bloodbankservice.core.utils.APIResponse
 * @see bloodbank.bloodbankservice.core.utils.APIResponseHandler
 */
@RequestMapping("api/v1/")
@Controller
public class BloodStockController {
    @Autowired
    private final BloodStockService bloodStockService;

    public BloodStockController(BloodStockService bloodStockService) {
        this.bloodStockService = bloodStockService;
    }

    // @note: This is what is should be (again).
    @GetMapping("/find/{id}")
    public ResponseEntity<APIResponse<BloodStock>> findBloodBankByIdPathVariable(@PathVariable(value = "id") Long id) {
        return wrappedBloodStockResponse(id);
    }

    // @note: This could be changed later on (or as an option if you want to use RequestParam).
    @GetMapping("/find/id")
    public ResponseEntity<APIResponse<BloodStock>> findBloodBankByIdRequestParam(@RequestParam(value = "id") Long id) {
        return wrappedBloodStockResponse(id);
    }

    @GetMapping("/find/all")
    public ResponseEntity<APIResponse<List<BloodStock>>> findAllBloodStocks() {
        var bloodStockEntities = bloodStockService.findAllBloodStocks();

        if (bloodStockEntities.isEmpty()) {
            return APIResponseHandler.collection(
                    "No Blood Stocks were found.",
                    HttpStatus.NOT_FOUND,
                    Collections.emptyList());

        } else {
            return APIResponseHandler.collection(
                    "All Blood Stocks were successfully retrieved.",
                    HttpStatus.OK,
                    bloodStockEntities);
        }
    }


    @PostMapping("/add")
    public ResponseEntity<APIResponse<BloodStock>> addNewBloodStock(
            @RequestParam String bloodGroup,
            @RequestParam Integer quantity,
            @RequestParam Date bestBefore,
            @RequestParam StatusType status
    ) {
        try {
            var bloodStock = BloodStock.builder()
                    .bloodGroup(bloodGroup)
                    .quantity(quantity)
                    .bestBefore(bestBefore)
                    .status(status)
                    .build();
            bloodStockService.saveBloodStock(bloodStock);

            return APIResponseHandler.payloadSuccess(
                    "Blood Stock with name: %s and status: %s has been added successfully.",
                    HttpStatus.CREATED,
                    bloodStock,
                    bloodGroup, status.toString().toUpperCase());

        } catch (Exception e) {
            return APIResponseHandler.error(
                    "Failed to add provided Blood Stock. Error: %s",
                    HttpStatus.BAD_REQUEST,
                    e.getMessage());
        }
    }

    @DeleteMapping("/find/delete/{id}")
    public ResponseEntity<APIResponse<Boolean>> deleteBloodStockById(@PathVariable(value = "id") Long id) {
        try {
            var deleted = bloodStockService.deleteBloodStock(id);

            if (deleted) {
                return APIResponseHandler.payloadSuccess(
                        "Blood Stock with id %s was deleted successfully.",
                        HttpStatus.OK,
                        true,
                        id);

            } else {
                return APIResponseHandler.payloadError(
                        "Blood Stock with id %s was either not found or it's deletion failed.",
                        HttpStatus.OK,
                        false,
                        id);
            }

        } catch (Exception e) {
            return APIResponseHandler.error(
                    "Failed to delete Blood Stock. Error: %s",
                    HttpStatus.BAD_REQUEST,
                    e.getMessage());
        }
    }

    @GetMapping("/find/{bloodGroup}")
    public ResponseEntity<APIResponse<List<BloodStock>>> findBloodStocksByBloodGroup(@PathVariable(value = "bloodGroup") String bloodGroup) {
        var bloodStockEntities = bloodStockService.findBloodStocksByBloodGroup(bloodGroup);

        if (bloodStockEntities.isEmpty()) {
            return APIResponseHandler.collection(
                    "No Blood Stocks were found.",
                    HttpStatus.NO_CONTENT,
                    bloodStockEntities);

        } else {
            return APIResponseHandler.collection(
                    "All Blood Stocks were successfully retrieved.",
                    HttpStatus.OK,
                    bloodStockEntities);
        }
    }

    @PutMapping("/find/update")
    public ResponseEntity<APIResponse<Boolean>> updateBloodStock(
            @RequestParam long id,
            @RequestBody BloodStock bloodStock
    ) {
        bloodStockService.updateBloodStock(bloodStock, id);

        return APIResponseHandler.payloadSuccess(
                "Blood Stock with id %s updated successfully.",
                HttpStatus.OK,
                true,
                id);
    }

    @DeleteMapping("/find/delete")
    public ResponseEntity<APIResponse<Boolean>> deleteBloodStockById(@RequestParam long id) {
        try {
            var deleted = bloodStockService.deleteBloodStock(id);

            if (deleted) {
                return APIResponseHandler.payloadSuccess(
                        "Blood Stock with id %s deleted successfully.",
                        HttpStatus.OK,
                        true,
                        id);

            } else {
                return APIResponseHandler.payloadError(
                        "Blood Stock with id %s was either not found or it's deletion failed.",
                        HttpStatus.OK,
                        false,
                        id);
            }
        } catch (Exception e) {
            return APIResponseHandler.error(
                    "Failed to delete Blood Stock. Error: %s",
                    HttpStatus.BAD_REQUEST,
                    e.getMessage());
        }
    }

    //region Class Helpers
    private ResponseEntity<APIResponse<BloodStock>> wrappedBloodStockResponse(final Long id) {
        var bloodStockEntity = bloodStockService.findBloodStockById(id);

        if (bloodStockEntity == null) {
            return APIResponseHandler.error(
                    "Blood Stock with id %s was not found.",
                    HttpStatus.NOT_FOUND,
                    id);

        } else {
            return APIResponseHandler.payloadSuccess(
                    "Blood Stock with id %s successfully retrieved.",
                    HttpStatus.OK,
                    bloodStockEntity,
                    id);
        }
    }
    //endregion

}
