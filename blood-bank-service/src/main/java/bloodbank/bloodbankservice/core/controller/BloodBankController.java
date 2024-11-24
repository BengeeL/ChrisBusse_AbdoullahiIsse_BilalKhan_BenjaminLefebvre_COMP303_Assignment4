package bloodbank.bloodbankservice.core.controller;

import bloodbank.bloodbankservice.core.entities.BloodBank;
import bloodbank.bloodbankservice.core.service.BloodBankService;
import bloodbank.bloodbankservice.core.utils.APIResponse;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

// TODO: Test this controller and refactor to clean up rough code.

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
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(APIResponse.<BloodBank>builder()
                            .message("Blood Bank deleted successfully.")
                            .status(HttpStatus.OK)
                            .payload(null)
                            .errorTrace(String.format("Blood Bank with name %s not found", bloodBankName)) // @note: Is this necessary?
                            .timestamp(Instant.now())
                            .build());
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(APIResponse.<BloodBank>builder()
                            .message("Blood Bank retrieved successfully.")
                            .status(HttpStatus.OK)
                            .payload(bloodBankEntity)
                            .timestamp(Instant.now())
                            .build());
        }
    }

    @GetMapping("/find/all")
    public ResponseEntity<APIResponse<List<BloodBank>>> findAllBloodBanks() {
        var bloodBankEntities = bloodBankService.findAllBloodBanks();

        if (bloodBankEntities.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(APIResponse.<List<BloodBank>>builder()
                            .message("No Blood Banks found.")
                            .status(HttpStatus.NO_CONTENT)
                            .payload(bloodBankEntities)
                            .timestamp(Instant.now())
                            .build());
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(APIResponse.<List<BloodBank>>builder()
                            .message("Blood Banks retrieved successfully.")
                            .status(HttpStatus.OK)
                            .payload(bloodBankEntities)
                            .timestamp(Instant.now())
                            .build());
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

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(APIResponse.<BloodBank>builder()
                            .message("Blood Bank has been added successfully.")
                            .status(HttpStatus.CREATED)
                            .payload(bloodBank)
                            .timestamp(Instant.now())
                            .build());
        } catch (EntityExistsException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(APIResponse.<BloodBank>builder()
                            .message("Blood Bank already exists.")
                            .status(HttpStatus.BAD_REQUEST)
                            .errorTrace(e.getMessage())
                            .timestamp(Instant.now())
                            .build());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(APIResponse.<BloodBank>builder()
                            .message("Failed to add Blood Bank.")
                            .status(HttpStatus.BAD_REQUEST)
                            .errorTrace(e.getMessage())
                            .timestamp(Instant.now())
                            .build());
        }
    }

    @DeleteMapping("/find/delete")
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
                                .timestamp(Instant.now())
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
                                .timestamp(Instant.now())
                                .build());
            }

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(APIResponse.<Boolean>builder()
                            .message("Failed to delete Blood Bank.")
                            .status(HttpStatus.BAD_REQUEST)
                            .errorTrace(e.getMessage())
                            .timestamp(Instant.now())
                            .build());
        }
    }

    @PutMapping("/find/update")
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
                        .timestamp(Instant.now())
                        .build());
    }


    //region Class Helpers
    private ResponseEntity<APIResponse<BloodBank>> wrappedBloodBankResponse(final Long id) {
        var bloodBankEntity = bloodBankService.findBloodBankById(id);

        if (bloodBankEntity == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(APIResponse.<BloodBank>builder()
                                    .message(String.format("Blood Bank with id %s not found", id))
                                    .status(HttpStatus.NOT_FOUND)
                                    .payload(null)
                                    .timestamp(Instant.now())
                                    .build());
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(APIResponse.<BloodBank>builder()
                            .message("Blood Bank retrieved successfully.")
                            .status(HttpStatus.OK)
                            .payload(bloodBankEntity)
                            .timestamp(Instant.now())
                            .build());
        }
    }
    //endregion
}
