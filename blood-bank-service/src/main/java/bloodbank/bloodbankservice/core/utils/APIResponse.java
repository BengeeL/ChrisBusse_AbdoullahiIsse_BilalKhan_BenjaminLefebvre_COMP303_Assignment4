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
    private T payload;

    //region Timestamp
    private Timestamp timestamp;
    //endregion
}
