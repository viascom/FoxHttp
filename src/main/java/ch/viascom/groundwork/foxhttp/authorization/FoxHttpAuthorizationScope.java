package ch.viascom.groundwork.foxhttp.authorization;

import ch.viascom.groundwork.foxhttp.type.RequestType;
import lombok.Getter;

/**
 * FoxHttpAuthorizationScope <p> Stores information about the scope for an Authorization
 *
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpAuthorizationScope {

    public static final FoxHttpAuthorizationScope ANY = new FoxHttpAuthorizationScope("[ -~]*", null);

    @Getter
    private final String pattern;
    @Getter
    private final String requestType;


    /**
     * Given an pattern and requestType, constructs a FoxHttpAuthorizationScope.
     *
     * @param pattern The pattern to use for the FoxHttpAuthorizationScope.
     * @param requestType The requestType to use for the FoxHttpAuthorizationScope.
     */
    FoxHttpAuthorizationScope(final String pattern, final String requestType) {
        this.pattern = pattern;
        this.requestType = requestType;
    }

    /**
     * Creates a new instance of {@link FoxHttpAuthorizationScope}.
     *
     * @param pattern The pattern to use for the FoxHttpAuthorizationScope.
     * @param requestType The requestType to use for the FoxHttpAuthorizationScope.
     * @return FoxHttpAuthorizationScope
     */
    public static FoxHttpAuthorizationScope create(final String pattern, final RequestType requestType) {
        if (pattern == null) {
            throw new IllegalArgumentException("pattern can not be pattern");
        }
        if (requestType == null) {
            throw new IllegalArgumentException("requestType can not be null");
        }
        if (pattern.length() == 0) {
            throw new IllegalArgumentException("pattern can not be empty");
        }
        return new FoxHttpAuthorizationScope(pattern, requestType.toString());
    }

    /**
     * Creates a new instance of {@link FoxHttpAuthorizationScope} with requestType ANY.
     *
     * @param pattern The pattern to use for the FoxHttpAuthorizationScope.
     * @return FoxHttpAuthorizationScope
     */
    public static FoxHttpAuthorizationScope create(final String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("pattern can not be null");
        }
        if (pattern.length() == 0) {
            throw new IllegalArgumentException("pattern can not be empty");
        }
        return new FoxHttpAuthorizationScope(pattern, null);
    }

    /**
     * Converts a FoxHttpAuthorizationScope to a string.
     */
    @Override
    public String toString() {
        return ((this.getRequestType() == null) ? "[ -~]* " : this.getRequestType() + " ") + this.getPattern();
    }

}
