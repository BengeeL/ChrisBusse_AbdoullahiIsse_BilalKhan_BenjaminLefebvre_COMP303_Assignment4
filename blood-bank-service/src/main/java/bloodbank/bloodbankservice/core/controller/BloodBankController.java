package bloodbank.bloodbankservice.core.controller;

import bloodbank.bloodbankservice.core.entities.BloodBank;
import bloodbank.bloodbankservice.core.service.BloodBankService;
import bloodbank.bloodbankservice.core.utils.APIResponse;
import bloodbank.bloodbankservice.core.utils.APIResponseHandler;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Controller class for managing BloodBank entities.
 * @author Muhammad Bilal Khan
 * @author Benjamin Lefebvre
 * @version 1.0.0
 * @since 1.0.0
 * @see bloodbank.bloodbankservice.core.entities.BloodBank
 * @see bloodbank.bloodbankservice.core.service.BloodBankService
 * @see bloodbank.bloodbankservice.core.utils.APIResponse
 */
@RequestMapping("api/v1/blood-bank/")
@RestController
public class BloodBankController {
    @Autowired
    private final BloodBankService bloodBankService;

    public BloodBankController(BloodBankService bloodBankService) {
        this.bloodBankService = bloodBankService;
    }

    // @note: This is what it should be.
    @GetMapping("/find/{id}")
    public ResponseEntity<APIResponse<BloodBank>> findBloodBankByIdPathVariable(@PathVariable(value = "id") long id) {
        return wrappedBloodBankResponse(id);
    }

    // @note: This could be changed later on (or as an option if you want use RequestParam).
    @GetMapping("/find/id")
    public ResponseEntity<APIResponse<BloodBank>> findBloodBankByIdRequestParam(@RequestParam(value = "id") long id) {
        return wrappedBloodBankResponse(id);
    }

    @GetMapping("/find/{bloodBankName}")
    public ResponseEntity<APIResponse<BloodBank>> findBloodBankByName(@PathVariable(value = "bloodBankName") String bloodBankName) {
        var bloodBankEntity = bloodBankService.findBloodBankByBloodBankName(bloodBankName);

        if (bloodBankEntity == null) {
            return APIResponseHandler.error(
                    "Blood Bank with name: %s was not found.",
                    HttpStatus.NOT_FOUND,
                    bloodBankName);
        } else {
            return APIResponseHandler.payloadSuccess(
                    "Blood Bank with name: %s retrieved successfully.",
                    HttpStatus.OK,
                    bloodBankEntity,
                    bloodBankName);

        }
    }

    @GetMapping("/find/all")
    public ResponseEntity<APIResponse<List<BloodBank>>> findAllBloodBanks() {
        var bloodBankEntities = bloodBankService.findAllBloodBanks();

        if (bloodBankEntities.isEmpty()) {
            return APIResponseHandler.collection(
                    "No Blood Banks were found.",
                    HttpStatus.NO_CONTENT,
                    Collections.emptyList());

        } else {
            return APIResponseHandler.collection(
                    "All Blood Banks were successfully retrieved.",
                    HttpStatus.OK,
                    bloodBankEntities);
        }
    }


    @PostMapping("/add")
    public ResponseEntity<APIResponse<BloodBank>> addNewBloodBank(
            @RequestParam String BloodBankName,
            @RequestParam String Address,
            @RequestParam String City,
            @RequestParam String Phone,
            @RequestParam String Website,
            @RequestParam String Email
    ) {
        try {
            var bloodBank = BloodBank.builder()
                    .bloodBankName(BloodBankName)
                    .address(Address)
                    .city(City)
                    .phoneNumber(Phone)
                    .website(Website)
                    .email(Email)
                    .build();
            bloodBankService.saveBloodBank(bloodBank);

            return APIResponseHandler.payloadSuccess(
                    "Blood Bank with name: %s has been added successfully.",
                    HttpStatus.CREATED,
                    bloodBank,
                    BloodBankName
            );

        } catch (EntityExistsException e) {
            return APIResponseHandler.error(
                    "Blood Bank already exists. Error: %s",
                    HttpStatus.BAD_REQUEST,
                    e.getMessage());

        } catch (Exception e) {
            return APIResponseHandler.error(
                    "Failed to add provided Blood Bank. Error: %s",
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    @DeleteMapping("/find/delete")
    public ResponseEntity<APIResponse<Boolean>> deleteBloodBank(@RequestParam long id) {
       try {
            var deleted = bloodBankService.deleteBloodBank(id);

            if (deleted) {
                return APIResponseHandler.payloadSuccess(
                        "Blood Bank with id %s deleted successfully.",
                        HttpStatus.OK,
                        true,
                        id);

            } else {
                return APIResponseHandler.payloadError(
                        "Blood Bank with id %s was either not found or it's deletion failed.",
                        HttpStatus.OK,
                        false,
                        id);
            }

        } catch (Exception e) {
           return APIResponseHandler.error(
                   "Failed to delete Blood Bank, getting error: %s",
                   HttpStatus.BAD_REQUEST,
                   e.getMessage());
        }
    }

    @PutMapping("/find/update")
    public ResponseEntity<APIResponse<Boolean>> updateBloodBank(
            @RequestParam long id,
            @RequestBody BloodBank bloodBank
    ) {
        bloodBankService.updateBloodBank(bloodBank, id);

        return APIResponseHandler.payloadSuccess(
                        "Blood Bank with id %s updated successfully",
                        HttpStatus.OK,
                        true,
                        id);
    }


    //region Class Helpers
    private ResponseEntity<APIResponse<BloodBank>> wrappedBloodBankResponse(final Long id) {
        var bloodBankEntity = bloodBankService.findBloodBankById(id);

        if (bloodBankEntity == null) {
            return APIResponseHandler.error(
                    "Blood Bank with id %s was not found.",
                    HttpStatus.NOT_FOUND,
                    id);

        } else {
            return APIResponseHandler.payloadSuccess(
                    "Blood Bank with id %s retrieved successfully.",
                    HttpStatus.OK,
                    bloodBankEntity,
                    id);
        }
    }
    //endregion
}
