package bloodbank.bloodbankservice.core.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "bb_blood_bank")
public class BloodBank implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "blood_bank_name", nullable = false)
    @NotBlank(message = "Blood Bank Name is a required field and should not be blank.")
    @Size(min = 1, max = 100, message = "Blood Bank Name should be between 1 and 100 characters.")
    private String bloodBankName;

    @Column(name = "address", nullable = false)
    @NotBlank(message = "Address is a required field and should not be blank.")
    private String address;

    @Column(name = "city", nullable = false)
    @NotBlank(message = "City is a required field and should not be blank.")
    private String city;

    @Column(name = "phone_number", nullable = false)
    @NotBlank(message = "Phone Number is a required field and should not be blank.")
    @Pattern(
            regexp = "^(\\+\\d{1,2}\\s?)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$",
            message = "Phone number does not conform with the regex provided."
    )
    private String phoneNumber;

    @Column(name = "website")
    @NotBlank(message = "Website is a required field and should nto be blank.")
    @Pattern(
            regexp = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)",
            message = "Website does not conform with the regex provided."
    )
    private String website;

    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "Email is a required field and should not be blank.")
    @Email(message = "Email that was provided is not of valid format.")
    private String email;

    //region Timestamps
    @Column(name="created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreatedDate
    private Timestamp CreatedAt;

    @Column(name="modified_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @LastModifiedDate
    private Timestamp ModifiedAt;
    //endregion
}
