package bloodbank.bloodbankservice.core.controller;

import bloodbank.bloodbankservice.core.entities.BloodStock;
import bloodbank.bloodbankservice.core.entities.enums.StatusType;
import bloodbank.bloodbankservice.core.service.BloodStockService;
import bloodbank.bloodbankservice.core.utils.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

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
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(APIResponse.<List<BloodStock>>builder()
                            .message("No Blood Stocks found.")
                            .status(HttpStatus.NOT_FOUND)
                            .payload(bloodStockEntities)
                            .timestamp(new Timestamp(System.currentTimeMillis()))
                            .build());
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(APIResponse.<List<BloodStock>>builder()
                            .message("Blood Stocks retrieved successfully.")
                            .status(HttpStatus.OK)
                            .payload(bloodStockEntities)
                            .timestamp(new Timestamp(System.currentTimeMillis()))
                            .build());
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

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(APIResponse.<BloodStock>builder()
                            .message("Blood Stock has been added successfully.")
                            .status(HttpStatus.CREATED)
                            .payload(bloodStock)
                            .timestamp(new Timestamp(System.currentTimeMillis()))
                            .build());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(APIResponse.<BloodStock>builder()
                            .message("Failed to add Blood Stock.")
                            .status(HttpStatus.BAD_REQUEST)
                            .errorTrace(e.getMessage())
                            .timestamp(new Timestamp(System.currentTimeMillis()))
                            .build());
        }
    }

    @DeleteMapping("/find/delete/{id}")
    public ResponseEntity<APIResponse<Boolean>> deleteBloodStockById(@PathVariable(value = "id") Long id) {
        try {
            var deleted = bloodStockService.deleteBloodStock(id);

            if (deleted) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(APIResponse.<Boolean>builder()
                                .message("Blood Stock deleted successfully.")
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
                                                "Blood Stock with id %s was either not found or it's deletion failed.",
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
                            .message("Failed to delete Blood Stock.")
                            .status(HttpStatus.BAD_REQUEST)
                            .errorTrace(e.getMessage())
                            .timestamp(new Timestamp(System.currentTimeMillis()))
                            .build());
        }
    }

    @GetMapping("/find/{bloodGroup}")
    public ResponseEntity<APIResponse<List<BloodStock>>> findBloodStocksByBloodGroup(@PathVariable(value = "bloodGroup") String bloodGroup) {
        var bloodStockEntities = bloodStockService.findBloodStocksByBloodGroup(bloodGroup);

        // TODO: Duplicate code below, refactor.
        if (bloodStockEntities.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(APIResponse.<List<BloodStock>>builder()
                            .message("No Blood Stocks found.")
                            .status(HttpStatus.NOT_FOUND)
                            .payload(bloodStockEntities)
                            .timestamp(new Timestamp(System.currentTimeMillis()))
                            .build());
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(APIResponse.<List<BloodStock>>builder()
                            .message("Blood Stocks retrieved successfully.")
                            .status(HttpStatus.OK)
                            .payload(bloodStockEntities)
                            .timestamp(new Timestamp(System.currentTimeMillis()))
                            .build());
        }
    }

    @PutMapping("/find/update")
    public ResponseEntity<APIResponse<Boolean>> updateBloodStock(
            @RequestParam long id,
            @RequestBody BloodStock bloodStock
    ) {
        bloodStockService.updateBloodStock(bloodStock, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIResponse.<Boolean>builder()
                        .message(String.format("Blood Stock with id %s updated successfully.", id))
                        .status(HttpStatus.OK)
                        .payload(true)
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .build());
    }

    @DeleteMapping("/find/delete")
    public ResponseEntity<APIResponse<Boolean>> deleteBloodStockById(@RequestParam long id) {
        // TODO: Duplicate code below, refactor.
        try {
            var deleted = bloodStockService.deleteBloodStock(id);

            if (deleted) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(APIResponse.<Boolean>builder()
                                .message("Blood Stock deleted successfully.")
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
                                                "Blood Stock with id %s was either not found or it's deletion failed.",
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
                            .message("Failed to delete Blood Stock.")
                            .status(HttpStatus.BAD_REQUEST)
                            .errorTrace(e.getMessage())
                            .timestamp(new Timestamp(System.currentTimeMillis()))
                            .build());
        }
    }

    //region Class Helpers
    private ResponseEntity<APIResponse<BloodStock>> wrappedBloodStockResponse(final Long id) {
        var bloodStockEntity = bloodStockService.findBloodStockById(id);

        if (bloodStockEntity == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(APIResponse.<BloodStock>builder()
                            .message(String.format("Blood Stock with id %s not found", id))
                            .status(HttpStatus.NOT_FOUND)
                            .payload(null)
                            .timestamp(new Timestamp(System.currentTimeMillis()))
                            .build());
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(APIResponse.<BloodStock>builder()
                            .message("Blood Stock retrieved successfully.")
                            .status(HttpStatus.OK)
                            .payload(bloodStockEntity)
                            .timestamp(new Timestamp(System.currentTimeMillis()))
                            .build());
        }
    }
    //endregion

}
