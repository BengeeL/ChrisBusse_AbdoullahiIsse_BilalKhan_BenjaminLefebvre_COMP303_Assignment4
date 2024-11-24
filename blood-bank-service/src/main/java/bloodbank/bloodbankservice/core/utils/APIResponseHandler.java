package bloodbank.bloodbankservice.core.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;

public class APIResponseHandler {
    private APIResponseHandler() {
        // Private constructor to prevent class instantiation.
    }

    public static <T> ResponseEntity<APIResponse<T>> success(final T payload) {
        return success("The Operation was completed successfully", HttpStatus.OK, payload);
    }

    public static <T> ResponseEntity<APIResponse<T>> success(
            final String message,
            final HttpStatus status,
            final T payload,
            final Object... args // @note: Optional args to pass to the message (e.g., id).
    ) {
        return ResponseEntity
                .status(status == null ? HttpStatus.OK : status)
                .body(APIResponse.<T>builder()
                        .message(String.format(message, args))
                        .status(status == null ? HttpStatus.OK : status)
                        .payload(payload)
                        .timestamp(Instant.now())
                        .build());
    }

    public static <T> ResponseEntity<APIResponse<T>> error(
            final String message,
            final HttpStatus status,
            final Object... args // @note: Optional args to pass to the message (e.g., id).
    ) {
        return ResponseEntity
                .status(status == null ? HttpStatus.INTERNAL_SERVER_ERROR : status)
                .body(APIResponse.<T>builder()
                        .status(status == null ? HttpStatus.INTERNAL_SERVER_ERROR : status)
                        .errorTrace(String.format(message, args))
                        .timestamp(Instant.now())
                        .build());

    }

    public static <T> ResponseEntity<APIResponse<T>> created(
            final String message,
            final HttpStatus status,
            final T payload,
            final Object... args // @note: Optional args to pass to the message (e.g., id).
    ) {
        if (status.is4xxClientError() || status.is5xxServerError()) {
            return error(message, status, args);
        }

        return success(message, status, payload, args);
    }

    public static <T> ResponseEntity<APIResponse<List<T>>> collection(
            final String message,
            final HttpStatus status,
            final List<T> payload,
            final Object... args
    ) {
        if (status.is4xxClientError() || status.is5xxServerError()) {
            return error(message, status, args);
        }

        return ResponseEntity
                .status(status)
                .body(APIResponse.<List<T>>builder()
                        .message(String.format(message, args))
                        .status(status)
                        .payload(payload)
                        .timestamp(Instant.now())
                        .build());
    }
}
