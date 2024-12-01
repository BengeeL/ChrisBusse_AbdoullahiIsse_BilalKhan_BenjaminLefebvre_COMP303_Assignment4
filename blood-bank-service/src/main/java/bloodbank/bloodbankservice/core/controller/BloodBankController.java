package bloodbank.bloodbankservice.core.controller;

import bloodbank.bloodbankservice.core.entities.BloodBank;
import bloodbank.bloodbankservice.core.service.BloodBankService;
import bloodbank.bloodbankservice.core.utils.APIResponse;
import bloodbank.bloodbankservice.core.utils.APIResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
@Tag(name = "Blood Bank API Endpoints", description = "Endpoints for managing Blood Banks.")
public class BloodBankController {
    @Autowired
    private final BloodBankService bloodBankService;

    public BloodBankController(BloodBankService bloodBankService) {
        this.bloodBankService = bloodBankService;
    }

    // ***********************************************
    // ********************* GET *********************
    // ***********************************************

    @Operation(
            summary = "Find a blood bank by its given id.",
            description = "Returns an APIResponse with the blood bank if found by its id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "200 if the blood bank was found successfully by the id provided."),
            @ApiResponse(responseCode = "404", description = "404 if the blood bank was not found successfully by the id provided.")
    })
    @GetMapping("/find/{id}")
    public ResponseEntity<APIResponse<BloodBank>> findBloodBankByIdPathVariable(@PathVariable(value = "id") long id) {
        return wrappedBloodBankResponse(id);
    }

    @Operation(
            summary = "Find a blood bank by its given id (using RequestParam).",
            description = "Returns an APIResponse with the blood bank if found by its id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "200 if the blood bank was found successfully by the id provided."),
            @ApiResponse(responseCode = "404", description = "404 if the blood bank was not found successfully by the id provided.")
    })
    @GetMapping("/find/id")
    public ResponseEntity<APIResponse<BloodBank>> findBloodBankByIdRequestParam(@RequestParam(value = "id") long id) {
        return wrappedBloodBankResponse(id);
    }


    @Operation(
            summary = "Find a blood bank entity by its given blood bank name (using PathVariable).",
            description = "Returns an APIResponse with the blood bank if found by its blood bank name."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "200 if the blood bank was found successfully by the blood bank name provided."),
            @ApiResponse(responseCode = "404", description = "404 if the blood bank was not found successfully by the blood bank name provided.")
    })
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

    @Operation(
            summary = "Find all blood bank entities.",
            description = "Returns an APIResponse with all the blood bank entities within the database."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "200 if the blood banks were found successfully (empty list otherwise)."),
    })
    @GetMapping("/find/all")
    public ResponseEntity<APIResponse<List<BloodBank>>> findAllBloodBanks() {
        var bloodBankEntities = bloodBankService.findAllBloodBanks();

        if (bloodBankEntities.isEmpty()) {
            return APIResponseHandler.collection(
                    "No Blood Banks were found.",
                    HttpStatus.OK,
                    Collections.emptyList());

        } else {
            return APIResponseHandler.collection(
                    "All Blood Banks were successfully retrieved.",
                    HttpStatus.OK,
                    bloodBankEntities);
        }
    }

    // ***********************************************
    // ********************* POST ********************
    // ***********************************************

    @Operation(
            summary = "Add a new blood bank to the database.",
            description = "Returns an APIResponse with the newly added blood bank."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "201 if the blood bank was added successfully."),
            @ApiResponse(responseCode = "400", description = "400 if the blood bank was not added successfully.")
    })
    @PostMapping("/add")
    public ResponseEntity<APIResponse<BloodBank>> addNewBloodBank(
            @RequestParam String bloodBankName,
            @RequestParam String address,
            @RequestParam String city,
            @RequestParam String phoneNumber,
            @RequestParam String website,
            @RequestParam String email
    ) {
        try {
            var bloodBank = BloodBank.builder()
                    .bloodBankName(bloodBankName)
                    .address(address)
                    .city(city)
                    .phoneNumber(phoneNumber)
                    .website(website)
                    .email(email)
                    .build();
            bloodBankService.saveBloodBank(bloodBank);

            return APIResponseHandler.payloadSuccess(
                    "Blood Bank with name: %s has been added successfully.",
                    HttpStatus.CREATED,
                    bloodBank,
                    bloodBankName
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

    // ***********************************************
    // ******************** DELETE *******************
    // ***********************************************

    @Operation(
            summary = "Delete a blood bank from the database.",
            description = "Returns an APIResponse with the deletion status (either true or false) of the blood bank."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "200 if the blood bank was deleted successfully."),
            @ApiResponse(responseCode = "400", description = "400 if the blood bank was not deleted successfully.")
    })
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

    // ***********************************************
    // ********************* PUT *********************
    // ***********************************************

    @Operation(
            summary = "Update a blood bank within the database.",
            description = "Returns an APIResponse with the updated blood bank."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "200 if the blood bank was updated successfully."),
            @ApiResponse(responseCode = "400", description = "400 if the blood bank was not updated successfully.")
    })
    @PutMapping("/find/update")
    public ResponseEntity<APIResponse<Boolean>> updateBloodBank(
            @RequestParam long id,
            @RequestParam String bloodBankName,
            @RequestParam String address,
            @RequestParam String city,
            @RequestParam String phoneNumber,
            @RequestParam String website,
            @RequestParam String email
    ) {
        BloodBank bloodBank = BloodBank.builder()
                .id(id)
                .bloodBankName(bloodBankName)
                .address(address)
                .city(city)
                .phoneNumber(phoneNumber)
                .website(website)
                .email(email)
                .build();

        Boolean updated = bloodBankService.updateBloodBank(bloodBank, id);

        if (updated) {
            return APIResponseHandler.payloadSuccess(
                    "Blood Bank with id %s updated successfully.",
                    HttpStatus.OK,
                    true,
                    id);
        } else {
            return APIResponseHandler.error(
                    "Blood Bank with id %s was either not found or it's update failed.",
                    HttpStatus.BAD_REQUEST,
                    id);
        }
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
