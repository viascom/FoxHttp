package ch.viascom.groundwork.foxhttp.exception;

import ch.viascom.groundwork.foxhttp.FoxHttpResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpResponseException extends FoxHttpException {

    @Getter
    @Setter
    private FoxHttpResponse foxHttpResponse;

    public FoxHttpResponseException(Throwable cause) {
        super(cause);
    }

    public FoxHttpResponseException(String message) {
        super(message);
    }

    public FoxHttpResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public FoxHttpResponseException(String message, FoxHttpResponse foxHttpResponse) {
        super(message);
        this.foxHttpResponse = foxHttpResponse;
    }

    public FoxHttpResponseException(String message, FoxHttpResponse foxHttpResponse, Throwable cause) {
        super(message, cause);
        this.foxHttpResponse = foxHttpResponse;
    }
}
