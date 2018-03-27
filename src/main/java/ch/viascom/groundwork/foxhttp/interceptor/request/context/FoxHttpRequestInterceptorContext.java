package ch.viascom.groundwork.foxhttp.interceptor.request.context;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import java.net.URL;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
@AllArgsConstructor
public class FoxHttpRequestInterceptorContext {

    private URL url;
    private FoxHttpRequest request;
    private FoxHttpClient client;
}
