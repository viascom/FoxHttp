package ch.viascom.groundwork.foxhttp.body.request;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import java.net.URLConnection;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
@AllArgsConstructor
public class FoxHttpRequestBodyContext {

    private URLConnection urlConnection;
    private FoxHttpRequest request;
    private FoxHttpClient client;
}
