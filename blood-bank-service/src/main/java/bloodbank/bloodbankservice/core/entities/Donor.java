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

    @Column(name = "lastname")
    @Size(min = 2, max = 50, message = "If provided, lastname should be between 2 and 50 characters.")
    private String lastName;

    @Column(name = "dob")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderType gender;

    @Column(name = "city")
    private String city;

    @Column(name = "bloodGroup")
    private String bloodGroup;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    //region Timestamps
    @Column(name="created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreatedDate
    private Instant CreatedAt;

    @Column(name="modified_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @LastModifiedDate
    private Instant ModifiedAt;
    //endregion
}
