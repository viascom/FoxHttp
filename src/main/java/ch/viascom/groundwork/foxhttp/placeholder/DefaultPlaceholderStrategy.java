package ch.viascom.groundwork.foxhttp.placeholder;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.log.FoxHttpLoggerLevel;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * @author patrick.boesch@viascom.ch
 */
public class DefaultPlaceholderStrategy implements FoxHttpPlaceholderStrategy {

    /**
     * Start of the placeholder
     */
    @Getter
    @Setter
    private String placeholderEscapeCharStart = "{";

    /**
     * End of the placeholder
     */
    @Getter
    @Setter
    private String placeholderEscapeCharEnd = "}";

    /**
     * Regex to check if placeholders are used
     */
    @Getter
    @Setter
    private String placeholderMatchRegex = "[\\{][ -z|]*[\\}]";

    @Getter
    @Setter
    private Map<String, String> placeholderMap = new HashMap<>();

    /**
     * Add a placeholder to the strategy
     *
     * @param placeholder name of the placholder
     * @param value value of the placeholder
     */
    @Override
    public void addPlaceholder(String placeholder, String value) {
        placeholderMap.put(placeholder, value);
    }


    public String processPlaceholders(final String processedURL, FoxHttpClient foxHttpClient) throws FoxHttpRequestException {
        String parsedString = processedURL;
        Pattern p = Pattern.compile(placeholderEscapeCharEnd);
        for (Map.Entry<String, String> entry : this.getPlaceholderMap().entrySet()) {
            if (p.matcher(parsedString).find()) {
                String searchPlaceholder = this.getPlaceholderEscapeCharStart() + entry.getKey() + this.getPlaceholderEscapeCharEnd();
                if (entry.getValue() == null) {
                    throw new FoxHttpRequestException("Placeholder " + searchPlaceholder + " cant be null.");
                }
                parsedString = parsedString.replace(searchPlaceholder, entry.getValue());
                foxHttpClient.getFoxHttpLogger()
                             .log(FoxHttpLoggerLevel.INFO, processedURL
                                                           + " -> ("
                                                           + this.getPlaceholderEscapeCharStart()
                                                           + entry.getKey()
                                                           + this.getPlaceholderEscapeCharEnd()
                                                           + " -> "
                                                           + entry.getValue()
                                                           + ") -> "
                                                           + parsedString);
            } else {
                break;
            }
        }
        return parsedString;
    }
}
