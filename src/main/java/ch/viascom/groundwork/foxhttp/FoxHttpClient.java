package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.authorization.DefaultAuthorizationStrategy;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationStrategy;
import ch.viascom.groundwork.foxhttp.component.FoxHttpComponent;
import ch.viascom.groundwork.foxhttp.cookie.DefaultCookieStore;
import ch.viascom.groundwork.foxhttp.cookie.FoxHttpCookieStore;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.*;
import ch.viascom.groundwork.foxhttp.log.DefaultFoxHttpLogger;
import ch.viascom.groundwork.foxhttp.log.FoxHttpLogger;
import ch.viascom.groundwork.foxhttp.parser.FoxHttpParser;
import ch.viascom.groundwork.foxhttp.placeholder.DefaultPlaceholderStrategy;
import ch.viascom.groundwork.foxhttp.placeholder.FoxHttpPlaceholderStrategy;
import ch.viascom.groundwork.foxhttp.proxy.FoxHttpProxyStrategy;
import ch.viascom.groundwork.foxhttp.ssl.DefaultSSLTrustStrategy;
import ch.viascom.groundwork.foxhttp.ssl.FoxHttpHostTrustStrategy;
import ch.viascom.groundwork.foxhttp.ssl.FoxHttpSSLTrustStrategy;
import ch.viascom.groundwork.foxhttp.timeout.DefaultTimeoutStrategy;
import ch.viascom.groundwork.foxhttp.timeout.FoxHttpTimeoutStrategy;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpClient {

    @Getter
    @Setter
    //Response parser
    private FoxHttpParser foxHttpResponseParser;

    @Getter
    @Setter
    //Request parser
    private FoxHttpParser foxHttpRequestParser;

    @Getter
    @Setter
    //Interceptors
    private FoxHttpInterceptorStrategy foxHttpInterceptorStrategy = new DefaultInterceptorStrategy();

    //@Getter
    //Caching
    //private FoxHttpCacheStrategy foxHttpCacheStrategy;

    @Getter
    @Setter
    //Cookies
    private FoxHttpCookieStore foxHttpCookieStore = new DefaultCookieStore();

    @Getter
    @Setter
    //Authorization
    private FoxHttpAuthorizationStrategy foxHttpAuthorizationStrategy = new DefaultAuthorizationStrategy();

    @Getter
    @Setter
    //Timeouts
    private FoxHttpTimeoutStrategy foxHttpTimeoutStrategy = new DefaultTimeoutStrategy();

    @Getter
    @Setter
    //HostnameVerifier
    //NOT READY YET
    @Deprecated
    private FoxHttpHostTrustStrategy foxHttpHostTrustStrategy;// = new DefaultHostTrustStrategy();

    @Getter
    @Setter
    //SSL
    private FoxHttpSSLTrustStrategy foxHttpSSLTrustStrategy = new DefaultSSLTrustStrategy();

    @Getter
    @Setter
    //Proxy
    private FoxHttpProxyStrategy foxHttpProxyStrategy;

    @Getter
    @Setter
    //Placeholder
    private FoxHttpPlaceholderStrategy foxHttpPlaceholderStrategy = new DefaultPlaceholderStrategy();

    @Getter
    @Setter
    //Components
    private List<FoxHttpComponent> foxHttpComponents = new ArrayList<>();

    @Getter
    @Setter
    //Logger
    private FoxHttpLogger foxHttpLogger = new DefaultFoxHttpLogger(false);

    @Getter
    @Setter
    //UserAgent
    private String foxHttpUserAgent = "FoxHTTP v1.3";

    public void activateComponent(FoxHttpComponent foxHttpComponent) throws FoxHttpException {
        foxHttpComponents.add(foxHttpComponent);
        foxHttpComponent.initiation(this);
    }

    //Backwards compatibility

    @Deprecated
    public Map<FoxHttpInterceptorType, ArrayList<FoxHttpInterceptor>> getFoxHttpInterceptors() {
        Map<FoxHttpInterceptorType, ArrayList<FoxHttpInterceptor>> interceptorList = new EnumMap<>(FoxHttpInterceptorType.class);

        foxHttpInterceptorStrategy.getFoxHttpInterceptors().forEach((key, value) -> {
            ArrayList<FoxHttpInterceptor> innerInterceptorList = new ArrayList<>();
            value.forEach((key1, value1) -> innerInterceptorList.add(value1));
            innerInterceptorList.sort(new FoxHttpInterceptorComparator());
            interceptorList.put(key, innerInterceptorList);
        });

        return interceptorList;
    }

    @Deprecated
    public void setFoxHttpInterceptors(Map<FoxHttpInterceptorType, ArrayList<FoxHttpInterceptor>> interceptors) {
        foxHttpInterceptorStrategy.setFoxHttpInterceptors(new EnumMap<>(FoxHttpInterceptorType.class));
        interceptors.forEach((key, value) -> value.forEach(interceptor -> {
            try {
                foxHttpInterceptorStrategy.addInterceptor(key, interceptor, String.valueOf(UUID.randomUUID()));
            } catch (FoxHttpException e) {
                e.printStackTrace();
            }
        }));
    }

    /**
     * Register an interceptor
     *
     * @param interceptorType    Type of the interceptor
     * @param foxHttpInterceptor Interceptor instance
     *
     * @throws FoxHttpException Throws an exception if the interceptor does not match the type
     */
    @Deprecated
    public void register(FoxHttpInterceptorType interceptorType, FoxHttpInterceptor foxHttpInterceptor) throws FoxHttpException {
        FoxHttpInterceptorType.verifyInterceptor(interceptorType, foxHttpInterceptor);
        foxHttpInterceptorStrategy.addInterceptor(interceptorType, foxHttpInterceptor, String.valueOf(UUID.randomUUID()));
    }

}
