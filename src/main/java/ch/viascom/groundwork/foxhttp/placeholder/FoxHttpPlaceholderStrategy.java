package ch.viascom.groundwork.foxhttp.placeholder;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;

import java.util.Map;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpPlaceholderStrategy {
    String getPlaceholderEscapeCharStart();

    void setPlaceholderEscapeCharStart(String placeholderEscapeChar);

    String getPlaceholderEscapeCharEnd();

    void setPlaceholderEscapeCharEnd(String placeholderEscapeChar);

    String getPlaceholderMatchRegex();

    void addPlaceholder(String placeholder, String value);

    Map<String, String> getPlaceholderMap();

    String processPlaceholders(String processedURL, FoxHttpClient foxHttpClient) throws FoxHttpRequestException;
}
