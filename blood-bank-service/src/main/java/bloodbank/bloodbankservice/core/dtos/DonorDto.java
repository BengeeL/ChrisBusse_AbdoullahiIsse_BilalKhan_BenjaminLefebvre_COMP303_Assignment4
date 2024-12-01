package bloodbank.bloodbankservice.core.dtos;

import bloodbank.bloodbankservice.core.entities.enums.GenderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for Donor Entity class.
 *
 * @author Muhammad Bilal Khan
 * @since 1.0.0
 * @version 1.0.0
 * @see bloodbank.bloodbankservice.core.entities.Donor
 */
@Value
public class DonorDto implements Serializable {
    Long id;

    @NotBlank(message = "Provided username is a required field and should not be blank.")
    String userName;

    @NotBlank(message = "Provided firstname is a required field and should not be blank.")
    String firstName;

    @NotBlank(message = "Provide lastname is a required field and should not be blank.")
    String lastName;

    @NotNull(message = "Date Of Birth is a required field and should not be null.")
    Date dateOfBirth;

    @NotNull(message = "Gender is a required field and should not be null.")
    GenderType Gender;

    @NotNull(message = "Blood Group is a required field and should not be null.")
    String bloodGroup;

    @NotBlank(message = "Provided city is a required field and should not be blank.")
    String city;

    @NotBlank(message = "Provided phone number is a required field and should not be blank.")
    String phoneNumber;
}