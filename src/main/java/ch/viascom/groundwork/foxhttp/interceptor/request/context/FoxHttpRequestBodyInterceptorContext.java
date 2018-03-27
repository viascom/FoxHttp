package ch.viascom.groundwork.foxhttp.interceptor.request.context;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import ch.viascom.groundwork.foxhttp.body.request.FoxHttpRequestBody;
import java.net.URLConnection;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
@AllArgsConstructor
public class FoxHttpRequestBodyInterceptorContext {

    private URLConnection urlConnection;
    private FoxHttpRequestBody requestBody;
    private FoxHttpRequest request;
    private FoxHttpClient client;
}
