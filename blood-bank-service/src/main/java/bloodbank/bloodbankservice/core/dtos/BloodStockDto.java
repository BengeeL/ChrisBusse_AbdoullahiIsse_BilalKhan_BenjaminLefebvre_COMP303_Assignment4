package bloodbank.bloodbankservice.core.dtos;

import bloodbank.bloodbankservice.core.entities.BloodStock;
import bloodbank.bloodbankservice.core.entities.enums.StatusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for Blood Stock Entity class.
 *
 * @author Muhammad Bilal Khan
 * @since 1.0.0
 * @version 1.0.0
 * @see bloodbank.bloodbankservice.core.entities.BloodStock
 */
@Value
public class BloodStockDto implements Serializable {
    Long id;
    
    @NotBlank(message = "Provided blood group is a required field and should not be blank.")
    String bloodGroup;

    @Range(message = "Quantity provided must be between 1 and 100.", min = 1, max = 100)
    Integer quantity;

    @NotNull(message = "Best Before Date is a required field and should not be null.")
    Date bestBefore;

    @NotNull(message = "Status is a required field and should not be null.")
    StatusType status;
}