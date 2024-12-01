package bloodbank.bloodbankservice.core.controller;

import bloodbank.bloodbankservice.core.entities.Donor;
import bloodbank.bloodbankservice.core.entities.enums.GenderType;
import bloodbank.bloodbankservice.core.service.DonorService;
import bloodbank.bloodbankservice.core.utils.APIResponse;
import bloodbank.bloodbankservice.core.utils.APIResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Controller class for managing Donor entities.
 * @author Muhammad Bilal Khan
 * @version 1.0.0
 * @since 1.0.0
 * @see bloodbank.bloodbankservice.core.entities.Donor
 * @see bloodbank.bloodbankservice.core.service.DonorService
 * @see bloodbank.bloodbankservice.core.utils.APIResponse
 * @see bloodbank.bloodbankservice.core.utils.APIResponseHandler
 */
@RequestMapping("api/v1/donor/")
@RestController
@Tag(name = "Donor API Endpoints", description = "Endpoints for managing Donors.")
public class DonorController {
    @Autowired
    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    // ***********************************************
    // ********************* GET *********************
    // ***********************************************

    @Operation(
            summary = "Find a donor by its given id.",
            description = "Returns an APIResponse with the donor if found by its id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "200 if the donor was found successfully by the id provided."),
            @ApiResponse(responseCode = "404", description = "404 if the donor was not found successfully by the id provided.")
    })
    @GetMapping(value = "/find/{id}")
    public ResponseEntity<APIResponse<Donor>> findDonorByIdPathVariable(@PathVariable(value = "id") Long id) {
        return wrappedDonorResponse(id);
    }


    @Operation(
            summary = "Find a donor by its given id (using RequestParam).",
            description = "Returns an APIResponse with the donor if found by its id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "200 if the donor was found successfully by the id provided."),
            @ApiResponse(responseCode = "404", description = "404 if the donor was not found successfully by the id provided.")
    })
    @GetMapping(value = "/find/id")
    public ResponseEntity<APIResponse<Donor>> findDonorByIdRequestParam(@RequestParam(value = "id") Long id) {
        return wrappedDonorResponse(id);
    }


    @Operation(
            summary = "Find all donors within the database.",
            description = "Returns an APIResponse with all donors within the database."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "200 if the donors were found successfully."),
            @ApiResponse(responseCode = "204", description = "204 if the donors were not found.")
    })
    @GetMapping(value = "/find/all")
    public ResponseEntity<APIResponse<List<Donor>>> findAllDonors() {
        var donorEntities = donorService.findAllDonors();

        return APIResponseHandler.collection(
                donorEntities.isEmpty() ? "No Donors found." : "Donors retrieved successfully.",
                HttpStatus.OK,
                donorEntities);
    }

    @Operation(
            summary = "Find all donors within the database by their city.",
            description = "Returns an APIResponse with all donors within the database by their city."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "200 if the donors were found successfully."),
            @ApiResponse(responseCode = "204", description = "204 if the donors were not found.")
    })
    @GetMapping(value = "/find/city")
    public ResponseEntity<APIResponse<List<Donor>>> findDonorsByCity(@RequestParam(value = "city") String city) {
        var donorEntities = donorService.findDonorsByCity(city);

        if (donorEntities.isEmpty()) {
            return APIResponseHandler.collection(
                    "No Donors found.",
                    HttpStatus.NO_CONTENT,
                    donorEntities);
        } else {
            return APIResponseHandler.collection(
                    "Donors with city: %s retrieved successfully.",
                    HttpStatus.OK,
                    donorEntities,
                    city);
        }
    }


    @Operation(
            summary = "Find all donors within the database that fit the given gender.",
            description = "Returns an APIResponse with all donors within the database that fit the given gender."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "200 if the donors were found successfully."),
            @ApiResponse(responseCode = "204", description = "204 if the donors were not found.")
    })
    @GetMapping("/find/gender")
    public ResponseEntity<APIResponse<List<Donor>>> findDonorsByGender(@RequestParam(value = "gender") GenderType genderType) {
        var donorEntities = donorService.findDonorsByGender(genderType);

        if (donorEntities.isEmpty()) {
            return APIResponseHandler.collection(
                    "No Donors found.",
                    HttpStatus.NO_CONTENT,
                    donorEntities);
        } else {
            return APIResponseHandler.collection(
                    "Donors with gender: %s retrieved successfully.",
                    HttpStatus.OK,
                    donorEntities,
                    genderType);
        }
    }

    // ***********************************************
    // ********************* POST ********************
    // ***********************************************

    @Operation(
            summary = "Add a new donor to the database.",
            description = "Returns an APIResponse with the newly added donor."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "201 if the donor was added successfully."),
            @ApiResponse(responseCode = "400", description = "400 if the donor was not added successfully.")
    })
    @PostMapping(value = "/add")
    public ResponseEntity<APIResponse<Donor>> addNewDonor(@RequestBody Donor donor) {
        try {
            // Set timestamps
            donor.setCreatedAt(Instant.now());
            donor.setModifiedAt(Instant.now());

            // Only validate firstName as it's the only required field
            if (donor.getFirstName() == null || donor.getFirstName().trim().isEmpty()) {
                return APIResponseHandler.error(
                        "Failed to add Donor. First name is required.",
                        HttpStatus.BAD_REQUEST,
                        "First name cannot be empty");
            }

            // Save donor with minimal information
            donorService.saveDonor(donor);

            return APIResponseHandler.payloadSuccess(
                    "Donor with firstname: %s has been added successfully.",
                    HttpStatus.CREATED,
                    donor,
                    donor.getFirstName());

        } catch (Exception e) {
            return APIResponseHandler.error(
                    "Failed to add Donor. Error: %s",
                    HttpStatus.BAD_REQUEST,
                    e.getMessage());
        }
    }

    // ***********************************************
    // ******************** DELETE *******************
    // ***********************************************

    @Operation(
            summary = "Delete a donor from the database.",
            description = "Returns an APIResponse with the deletion status of the donor."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "200 if the donor was deleted successfully."),
            @ApiResponse(responseCode = "400", description = "400 if the donor was not deleted successfully.")
    })
    @DeleteMapping(value = "/find/delete/{id}")
    public ResponseEntity<APIResponse<Boolean>> deleteDonorById(@PathVariable(value = "id") Long id) {
        try {
            var deleted = donorService.deleteDonor(id);

            if (deleted) {
                return APIResponseHandler.payloadSuccess(
                        "Donor with id %s deleted successfully.",
                        HttpStatus.OK,
                        true,
                        id);

            } else {
                return APIResponseHandler.error(
                        "Donor with id %s was either not found or it's deletion failed.",
                        HttpStatus.OK,
                        id);

            }
        } catch (Exception e) {
            return APIResponseHandler.error(
                    "Failed to delete Donor, getting error: %s",
                    HttpStatus.BAD_REQUEST,
                    e.getMessage());

        }
    }


    @Operation(
            summary = "Delete multiple donors from the database.",
            description = "Returns an APIResponse with the deletion status of the donors."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "200 if the donors were deleted successfully."),
            @ApiResponse(responseCode = "400", description = "400 if the donors were not deleted successfully.")
    })
    @DeleteMapping(value = "/find/delete/", produces = "application/json")
    public ResponseEntity<APIResponse<Boolean>> deleteDonorsById(@RequestParam List<Long> ids) {
        try {
            for (var id: ids) {
                if (donorService.deleteDonor(id)) {
                    return APIResponseHandler.payloadSuccess(
                            "Donor with id %s deleted successfully.",
                            HttpStatus.OK,
                            true,
                            id
                    );
                }
            }

            return APIResponseHandler.error(
                    "Donor with id %s was either not found or it's deletion failed.",
                    HttpStatus.OK,
                    ids
            );
        } catch (Exception e) {
            return APIResponseHandler.error(
                    "Failed to delete Donor, getting error: %s",
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }
    
    // ***********************************************
    // ********************* PUT *********************
    // ***********************************************

    @Operation(
            summary = "Update a donor in the database.",
            description = "Returns an APIResponse with the updated donor."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "200 if the donor was updated successfully."),
            @ApiResponse(responseCode = "400", description = "400 if the donor was not updated successfully.")
    })
    @PutMapping(value = "/find/update")
    public ResponseEntity<APIResponse<Donor>> updateDonorById(
            @RequestParam Long id,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String bloodGroup,
            @RequestParam Date dateOfBirth,
            @RequestParam GenderType gender,
            @RequestParam String city,
            @RequestParam String phoneNumber
    ) {
        Donor donor = Donor.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .dateOfBirth(dateOfBirth)
                .gender(gender)
                .city(city)
                .phoneNumber(phoneNumber)
                .bloodGroup(bloodGroup)
                .build();

        donorService.updateDonor(donor, id);

        return APIResponseHandler.payloadSuccess(
                "Donor with id %s updated successfully.",
                HttpStatus.OK,
                donor,
                id);
    }

    @Operation(
            summary = "Update a donor's information",
            description = "Updates donor information based on the provided donor object"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Donor updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Donor not found")
    })
    @PutMapping("/update")
    public ResponseEntity<APIResponse<Donor>> updateDonor(@RequestBody Donor donor) {
        try {
            if (donor.getId() == null) {
                return APIResponseHandler.error(
                        "Donor ID is required for update",
                        HttpStatus.BAD_REQUEST);
            }

            Donor updatedDonor = donorService.updateDonor(donor);
            if (updatedDonor != null) {
                return APIResponseHandler.payloadSuccess(
                        "Donor updated successfully",
                        HttpStatus.OK,
                        updatedDonor);
            } else {
                return APIResponseHandler.error(
                        "Donor not found with id: " + donor.getId(),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return APIResponseHandler.error(
                    "Failed to update donor: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    //region Class Helpers
    private ResponseEntity<APIResponse<Donor>> wrappedDonorResponse(final Long id) {
        var donorEntity = donorService.findDonorById(id);

        if (donorEntity == null) {
            return APIResponseHandler.error(
                    "Donor with id %s was not found.",
                    HttpStatus.NOT_FOUND,
                    id);
        } else {
            return APIResponseHandler.payloadSuccess(
                    "Donor with id %s retrieved successfully.",
                    HttpStatus.OK,
                    donorEntity,
                    id);
        }
    }
    //endregion
}
