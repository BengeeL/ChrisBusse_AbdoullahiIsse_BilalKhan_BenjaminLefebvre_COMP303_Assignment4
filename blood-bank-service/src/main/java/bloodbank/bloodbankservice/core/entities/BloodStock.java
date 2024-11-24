package bloodbank.bloodbankservice.core.entities;

import bloodbank.bloodbankservice.core.entities.enums.StatusType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "bb_blood_bank")
public class BloodStock implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "blood_group", nullable = false)
    @NotBlank(message = "Provided blood group is a required field and should not be blank.")
    @Size(min = 1, max = 100, message = "Blood Group provided must be between 1 and 100 characters.")
    private String bloodGroup;

    @Column(name = "quantity", nullable = false)
    @Range(min = 1, max = 100, message = "Quantity provided must be between 1 and 100.")
    private Integer quantity;

    @Column(name = "best_before", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Best Before Date is a required field and should not be null.")
    @Pattern(
            regexp = "\\d{1,2}\\/\\d{1,2}\\/\\d{2,4}",
            message = "Best Before data does not conform with the regex provided.")
    private Date bestBefore;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is a required field and should not be null.")
    private StatusType status;

    //region Timestamps
    @Column(name="created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreatedDate
    private Timestamp CreatedAt;

    @Column(name="modified_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @LastModifiedDate
    private Timestamp ModifiedAt;
    //endregion
}
