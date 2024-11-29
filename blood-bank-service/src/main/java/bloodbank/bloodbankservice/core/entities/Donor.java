package bloodbank.bloodbankservice.core.entities;

import bloodbank.bloodbankservice.core.entities.enums.GenderType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "bb_donor")
public class Donor implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname", nullable = false)
    @NotBlank(message = "Provided firstname is a required field and should not be blank.")
    @Size(min = 2, max = 50, message = "Firstname should be between 2 and 50 characters.")
    private @NotNull String firstName;

    @Column(name = "lastname", nullable = false)
    @NotBlank(message = "Provide lastname is a required field and should not be blank.")
    @Size(min = 2, max = 50, message = "Lastname should be between 2 and 50 characters.")
    private @NotNull String lastName;

    @Column(name = "age", nullable = false)
    @Range(min = 1, max = 99, message = "Age should be within the range of 1 - 99")
    private Integer age;

    @Column(name = "dob", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Best Before Date is a required field and should not be null.")
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    @NotNull (message = "Gender is a required field and should not be null.")
    private GenderType gender;

    @Column(name = "city", nullable = false)
    @NotBlank(message = "Provided city is a required field and should not be blank.")
    private @NotNull String city;

    @Column(name = "phone_number", unique = true, nullable = false)
    @NotBlank(message = "Provided phone number is a required field and should not be blank.")
    private @NotNull String phoneNumber;

    //region Timestamps
    @Column(name="created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreatedDate
    private Instant CreatedAt;

    @Column(name="modified_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @LastModifiedDate
    private Instant ModifiedAt;
    //endregion
}
