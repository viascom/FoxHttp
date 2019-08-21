package ch.viascom.groundwork.foxhttp.cache;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import ch.viascom.groundwork.foxhttp.FoxHttpResponse;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpCacheStrategy {

    boolean isCachingEnabled();

    void setCachingEnabled(boolean enabled);

    boolean isCachingAvailable(FoxHttpRequest request, FoxHttpClient client);

    FoxHttpResponse loadDataFromCache(FoxHttpRequest request, FoxHttpClient client);

    void saveDataToCache(FoxHttpResponse response, FoxHttpRequest request, FoxHttpClient client);

}
