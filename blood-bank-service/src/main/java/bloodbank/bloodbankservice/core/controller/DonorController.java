package bloodbank.bloodbankservice.core.controller;

import bloodbank.bloodbankservice.core.entities.Donor;
import bloodbank.bloodbankservice.core.entities.enums.GenderType;
import bloodbank.bloodbankservice.core.service.DonorService;
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

// TODO: Test this controller and refactor to clean up rough code.

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
@Controller
public class DonorController {
    @Autowired
    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    // @note: This is what it should be
    @GetMapping("/find/{id}")
    public ResponseEntity<APIResponse<Donor>> findDonorByIdPathVariable(@PathVariable(value = "id") Long id) {
        return wrappedDonorResponse(id);
    }

    // @note: This could be changed later on (or as an option if you want to use RequestParam).
    @GetMapping("/find/id")
    public ResponseEntity<APIResponse<Donor>> findDonorByIdRequestParam(@RequestParam(value = "id") Long id) {
        return wrappedDonorResponse(id);
    }

    @GetMapping("/find/all")
    public ResponseEntity<APIResponse<List<Donor>>> findAllDonors() {
        var donorEntities = donorService.findAllDonors();

        if (donorEntities.isEmpty()) {
            return APIResponseHandler.collection(
                    "No Donors found.",
                    HttpStatus.NO_CONTENT,
                    donorEntities
            );
        } else {
            return APIResponseHandler.collection(
                    "Donors retrieved successfully.",
                    HttpStatus.OK,
                    donorEntities
            );
        }
    }

    @GetMapping("/find/age")
    public ResponseEntity<APIResponse<List<Donor>>> findDonorsByAge(@RequestParam(value = "age") Integer age) {
        var donorEntities = donorService.findDonorsByAge(age);

        if (donorEntities.isEmpty()) {
            return APIResponseHandler.collection(
                    "No Donors found.",
                    HttpStatus.NO_CONTENT,
                    Collections.emptyList()
            );
        } else {
            return APIResponseHandler.collection(
                    "Donors with age: %d retrieved successfully.",
                    HttpStatus.OK,
                    donorEntities,
                    age
            );
        }
    }

    @GetMapping("/find/city")
    public ResponseEntity<APIResponse<List<Donor>>> findDonorsByCity(@RequestParam(value = "city") String city) {
        var donorEntities = donorService.findDonorsByCity(city);

        if (donorEntities.isEmpty()) {
            return APIResponseHandler.collection(
                    "No Donors found.",
                    HttpStatus.NO_CONTENT,
                    donorEntities
            );
        } else {
            return APIResponseHandler.collection(
                    "Donors with city: %s retrieved successfully.",
                    HttpStatus.OK,
                    donorEntities,
                    city
            );
        }
    }

    @GetMapping("/find/gender")
    public ResponseEntity<APIResponse<List<Donor>>> findDonorsByGender(@RequestParam(value = "gender") GenderType genderType) {
        var donorEntities = donorService.findDonorsByGender(genderType);

        if (donorEntities.isEmpty()) {
            return APIResponseHandler.collection(
                    "No Donors found.",
                    HttpStatus.NO_CONTENT,
                    donorEntities
            );
        } else {
            return APIResponseHandler.collection(
                    "Donors with gender: %s retrieved successfully.",
                    HttpStatus.OK,
                    donorEntities,
                    genderType
            );
        }
    }


    @PostMapping("/add")
    public ResponseEntity<APIResponse<Donor>> addNewDonor(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam Integer age,
            @RequestParam Date dateOfBirth,
            @RequestParam GenderType gender
    ) {
        try {
            var donor = Donor.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .age(age)
                    .dateOfBirth(dateOfBirth)
                    .gender(gender)
                    .build();
            donorService.saveDonor(donor);

            return APIResponseHandler.success(
                    "Donor has been added successfully.",
                    HttpStatus.CREATED,
                    donor
            );

        } catch (Exception e) {
            return APIResponseHandler.error(
                    "Failed to add Donor.",
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }


    @DeleteMapping("/find/delete/{id}")
    public ResponseEntity<APIResponse<Boolean>> deleteDonorById(@PathVariable(value = "id") Long id) {
        try {
            var deleted = donorService.deleteDonor(id);

            if (deleted) {
                return APIResponseHandler.success(
                        "Donor with id %s deleted successfully.",
                        HttpStatus.OK,
                        true,
                        id
                );
            } else {
                return APIResponseHandler.error(
                        "Donor with id %s was either not found or it's deletion failed.",
                        HttpStatus.OK,
                        id
                );
            }
        } catch (Exception e) {
            return APIResponseHandler.error(
                    "Failed to delete Donor, getting error: %s",
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    @PutMapping("/find/update")
    public ResponseEntity<APIResponse<Donor>> updateDonorById(
            @RequestParam Long id,
            @RequestBody Donor donor
    ) {
        donorService.updateDonor(donor, id);

        return APIResponseHandler.success(
                "Donor with id %s updated successfully.",
                HttpStatus.OK,
                donor,
                id
        );
    }

    @DeleteMapping("/find/delete/")
    public ResponseEntity<APIResponse<Boolean>> deleteDonorsById(@RequestParam List<Long> ids) {
        try {
            for (var id: ids) {
                if (donorService.deleteDonor(id)) {
                    return APIResponseHandler.success(
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

    //region Class Helpers
    private ResponseEntity<APIResponse<Donor>> wrappedDonorResponse(final Long id) {
        var donorEntity = donorService.findDonorById(id);

        if (donorEntity == null) {
            return APIResponseHandler.error(
                    "Donor with id %s was not found.",
                    HttpStatus.NOT_FOUND,
                    id
            );
        } else {
            return APIResponseHandler.success(
                    "Donor with id %s retrieved successfully.",
                    HttpStatus.OK,
                    donorEntity,
                    id
            );
        }
    }
    //endregion
}
