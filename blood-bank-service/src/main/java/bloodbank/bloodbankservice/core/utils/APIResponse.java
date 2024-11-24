package bloodbank.bloodbankservice.core.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class APIResponse<T> {
    private String message;
    private HttpStatus status;

    // @note: This is the payload that will usually be returned.
    private T payload;

    // @note: If there is an error, this will be the new payload.
    private String errorTrace;

    //region Timestamp
    private Timestamp timestamp;
    //endregion
}
