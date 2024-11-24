package bloodbank.bloodbankservice.core.controller;

import bloodbank.bloodbankservice.core.entities.BloodBank;
import bloodbank.bloodbankservice.core.service.BloodBankService;
import bloodbank.bloodbankservice.core.utils.APIResponse;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
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
@Controller
public class BloodBankController {
    @Autowired
    private final BloodBankService bloodBankService;

    public BloodBankController(BloodBankService bloodBankService) {
        this.bloodBankService = bloodBankService;
    }

    // @note: This is what it should be.
    @RequestMapping("/get/{id}")
    public ResponseEntity<APIResponse<BloodBank>> getBloodBankByIdPathVariable(@PathVariable(value = "id") long id) {
        return wrappedBloodBankResponse(id);
    }

    // @note: This could be changed later on (or as an option if you want use RequestParam).
    @RequestMapping("/get/id")
    public ResponseEntity<APIResponse<BloodBank>> getBloodBankByIdRequestParam(@RequestParam(value = "id") long id) {
        return wrappedBloodBankResponse(id);
    }

    @RequestMapping("/get/all")
    public ResponseEntity<APIResponse<List<BloodBank>>> getAllBloodBanks() {
        var bloodBankEntities = bloodBankService.getAllBloodBanks();

        if (bloodBankEntities.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(APIResponse.<List<BloodBank>>builder()
                            .message("No Blood Banks found.")
                            .status(HttpStatus.OK)
                            .payload(bloodBankEntities)
                            .timestamp(new Timestamp(System.currentTimeMillis()))
                            .build());
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(APIResponse.<List<BloodBank>>builder()
                            .message("Blood Banks retrieved successfully.")
                            .status(HttpStatus.OK)
                            .payload(bloodBankEntities)
                            .timestamp(new Timestamp(System.currentTimeMillis()))
                            .build());
        }
    }


    @RequestMapping("/add")
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

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(APIResponse.<BloodBank>builder()
                            .message("Blood Bank has been added successfully.")
                            .status(HttpStatus.CREATED)
                            .payload(bloodBank)
                            .timestamp(new Timestamp(System.currentTimeMillis()))
                            .build());
        } catch (EntityExistsException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(APIResponse.<BloodBank>builder()
                            .message("Blood Bank already exists.")
                            .status(HttpStatus.BAD_REQUEST)
                            .errorTrace(e.getMessage())
                            .timestamp(new Timestamp(System.currentTimeMillis()))
                            .build());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(APIResponse.<BloodBank>builder()
                            .message("Failed to add Blood Bank.")
                            .status(HttpStatus.BAD_REQUEST)
                            .errorTrace(e.getMessage())
                            .timestamp(new Timestamp(System.currentTimeMillis()))
                            .build());
        }
    }

    @RequestMapping("/get/{")

    @RequestMapping("/get/delete")
    public ResponseEntity<APIResponse<Boolean>> deleteBloodBank(@RequestParam long id) {
       try {
            var deleted = bloodBankService.deleteBloodBank(id);

            if (deleted) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(APIResponse.<Boolean>builder()
                        .message("Blood Bank deleted successfully.")
                        .status(HttpStatus.OK)
                        .payload(true)
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .build());
            } else {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(APIResponse.<Boolean>builder()
                                .message(
                                        String.format(
                                                "Blood Bank with id %s was either not found or it's deletion failed.",
                                                id))
                                .status(HttpStatus.OK)
                                .payload(false)
                                .timestamp(new Timestamp(System.currentTimeMillis()))
                                .build());
            }

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(APIResponse.<Boolean>builder()
                            .message("Failed to delete Blood Bank.")
                            .status(HttpStatus.BAD_REQUEST)
                            .errorTrace(e.getMessage())
                            .timestamp(new Timestamp(System.currentTimeMillis()))
                            .build());
        }
    }

    @RequestMapping("/get/update")
    public ResponseEntity<APIResponse<Boolean>> updateBloodBank(
            @RequestParam long id,
            @RequestBody BloodBank bloodBank
    ) {
        bloodBankService.updateBloodBank(bloodBank, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIResponse.<Boolean>builder()
                        .message(String.format("Blood Bank with id %s updated successfully.", id))
                        .status(HttpStatus.OK)
                        .payload(true)
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .build());
    }


    //region Class Helpers
    private ResponseEntity<APIResponse<BloodBank>> wrappedBloodBankResponse(final Long id) {
        var bloodBankEntity = bloodBankService.getBloodBankById(id);

        if (bloodBankEntity == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(APIResponse.<BloodBank>builder()
                                    .message(String.format("Blood Bank with id %s not found", id))
                                    .status(HttpStatus.NOT_FOUND)
                                    .payload(null)
                                    .timestamp(new Timestamp(System.currentTimeMillis()))
                                    .build());
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(APIResponse.<BloodBank>builder()
                            .message("Blood Bank retrieved successfully.")
                            .status(HttpStatus.OK)
                            .payload(bloodBankEntity)
                            .timestamp(new Timestamp(System.currentTimeMillis()))
                            .build());
        }
    }
    //endregion
}
