package ch.viascom.groundwork.foxhttp.ssl;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpSSLTrustStrategyException;
import ch.viascom.groundwork.foxhttp.log.FoxHttpLogger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/**
 * @author patrick.boesch@viascom.ch
 */
@FunctionalInterface
public interface FoxHttpSSLTrustStrategy {

    default void activateSSLDebugLog() {
        System.setProperty("javax.net.debug", "all");
    }

    default void disableSSLDebugLog() {
        System.clearProperty("javax.net.debug");
    }

    SSLSocketFactory getSSLSocketFactory(HttpsURLConnection httpsURLConnection, FoxHttpLogger logger) throws FoxHttpSSLTrustStrategyException;
}
