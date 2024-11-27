package bloodbank.bloodbankservice.core.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "API Response Wrapper Object")
public class APIResponse<T> {
    @Schema(description = "Message to be returned to the user .")
    private String message;

    @Schema(description = "Status of the response.")
    private HttpStatus status;

    // @note: This is the payload that will usually be returned.
    @Schema(description = "Payload that is returned from the API.")
    private T payload;

    // @note: If there is an error, this will be the new payload.
    @Schema(description = "Possible error trace when there is an error from the API.")
    private String errorTrace;

    //region Timestamp
    private Instant timestamp;
    //endregion
}
