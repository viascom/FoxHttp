package ch.viascom.groundwork.foxhttp.cache;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import ch.viascom.groundwork.foxhttp.FoxHttpResponse;
import java.io.InputStream;

public class NoCacheStrategy implements FoxHttpCacheStrategy {

    @Override
    public boolean isCachingEnabled() {
        return false;
    }

    @Override
    public void setCachingEnabled(boolean enabled) {

    }

    @Override
    public boolean isCachingAvailable(FoxHttpRequest request, FoxHttpClient client) {
        return false;
    }

    @Override
    public FoxHttpResponse loadDataFromCache(FoxHttpRequest request, FoxHttpClient client) {
        return null;
    }

    @Override
    public void saveDataToCache(FoxHttpResponse response, FoxHttpRequest request, FoxHttpClient client) {

    }
}
