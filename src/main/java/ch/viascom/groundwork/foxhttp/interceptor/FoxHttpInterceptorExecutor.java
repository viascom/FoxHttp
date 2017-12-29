package ch.viascom.groundwork.foxhttp.interceptor;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestBodyInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestConnectionInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestHeaderInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestBodyInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestConnectionInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestHeaderInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.response.FoxHttpResponseBodyInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.FoxHttpResponseCodeInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.FoxHttpResponseInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseBodyInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseCodeInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseInterceptorContext;
import ch.viascom.groundwork.foxhttp.log.FoxHttpLoggerLevel;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpInterceptorExecutor {

    /**
     * Utility classes, which are a collection of static members, are not meant to be instantiated.
     */
    private FoxHttpInterceptorExecutor() {
        throw new IllegalAccessError("Utility class");
    }

    public static void executeRequestInterceptor(FoxHttpRequestInterceptorContext context) throws FoxHttpException {
        if (context.getClient().getFoxHttpInterceptorStrategy().doesTypeExist(FoxHttpInterceptorType.REQUEST)) {
            for (FoxHttpInterceptor interceptor : context.getClient().getFoxHttpInterceptorStrategy().getAllInterceptorsFromTypeAsArray(FoxHttpInterceptorType.REQUEST, true)) {
                context.getClient().getFoxHttpLogger().log(FoxHttpLoggerLevel.DEBUG, "-> [REQUEST] " + interceptor);
                ((FoxHttpRequestInterceptor) interceptor).onIntercept(context);
            }
        }
    }

    public static void executeRequestConnectionInterceptor(FoxHttpRequestConnectionInterceptorContext context) throws FoxHttpException {
        if (context.getClient().getFoxHttpInterceptorStrategy().doesTypeExist(FoxHttpInterceptorType.REQUEST_CONNECTION)) {
            for (FoxHttpInterceptor interceptor : context.getClient().getFoxHttpInterceptorStrategy().getAllInterceptorsFromTypeAsArray(FoxHttpInterceptorType.REQUEST_CONNECTION, true)) {
                context.getClient().getFoxHttpLogger().log(FoxHttpLoggerLevel.DEBUG, "-> [REQUEST_CONNECTION] " + interceptor);
                ((FoxHttpRequestConnectionInterceptor) interceptor).onIntercept(context);
            }
        }
    }

    public static void executeRequestHeaderInterceptor(FoxHttpRequestHeaderInterceptorContext context) throws FoxHttpException {
        if (context.getClient().getFoxHttpInterceptorStrategy().doesTypeExist(FoxHttpInterceptorType.REQUEST_HEADER)) {
            for (FoxHttpInterceptor interceptor : context.getClient().getFoxHttpInterceptorStrategy().getAllInterceptorsFromTypeAsArray(FoxHttpInterceptorType.REQUEST_HEADER, true)) {
                context.getClient().getFoxHttpLogger().log(FoxHttpLoggerLevel.DEBUG, "-> [REQUEST_HEADER] " + interceptor);
                ((FoxHttpRequestHeaderInterceptor) interceptor).onIntercept(context);
            }
        }
    }

    public static void executeRequestBodyInterceptor(FoxHttpRequestBodyInterceptorContext context) throws FoxHttpException {
        if (context.getClient().getFoxHttpInterceptorStrategy().doesTypeExist(FoxHttpInterceptorType.REQUEST_BODY)) {
            for (FoxHttpInterceptor interceptor : context.getClient().getFoxHttpInterceptorStrategy().getAllInterceptorsFromTypeAsArray(FoxHttpInterceptorType.REQUEST_BODY, true)) {
                context.getClient().getFoxHttpLogger().log(FoxHttpLoggerLevel.DEBUG, "-> [REQUEST_BODY] " + interceptor);
                ((FoxHttpRequestBodyInterceptor) interceptor).onIntercept(context);
            }
        }
    }

    public static void executeResponseInterceptor(FoxHttpResponseInterceptorContext context) throws FoxHttpException {
        if (context.getClient().getFoxHttpInterceptorStrategy().doesTypeExist(FoxHttpInterceptorType.RESPONSE)) {
            for (FoxHttpInterceptor interceptor : context.getClient().getFoxHttpInterceptorStrategy().getAllInterceptorsFromTypeAsArray(FoxHttpInterceptorType.RESPONSE, true)) {
                context.getClient().getFoxHttpLogger().log(FoxHttpLoggerLevel.DEBUG, "-> [RESPONSE] " + interceptor);
                ((FoxHttpResponseInterceptor) interceptor).onIntercept(context);
            }
        }
    }

    public static void executeResponseBodyInterceptor(FoxHttpResponseBodyInterceptorContext context) throws FoxHttpException {
        if (context.getClient().getFoxHttpInterceptorStrategy().doesTypeExist(FoxHttpInterceptorType.RESPONSE_BODY)) {
            for (FoxHttpInterceptor interceptor : context.getClient().getFoxHttpInterceptorStrategy().getAllInterceptorsFromTypeAsArray(FoxHttpInterceptorType.RESPONSE_BODY, true)) {
                context.getClient().getFoxHttpLogger().log(FoxHttpLoggerLevel.DEBUG, "-> [RESPONSE_BODY] " + interceptor);
                ((FoxHttpResponseBodyInterceptor) interceptor).onIntercept(context);
            }
        }
    }

    public static void executeResponseCodeInterceptor(FoxHttpResponseCodeInterceptorContext context) throws FoxHttpException {
        if (context.getClient().getFoxHttpInterceptorStrategy().doesTypeExist(FoxHttpInterceptorType.RESPONSE_CODE)) {
            for (FoxHttpInterceptor interceptor : context.getClient().getFoxHttpInterceptorStrategy().getAllInterceptorsFromTypeAsArray(FoxHttpInterceptorType.RESPONSE_CODE, true)) {
                context.getClient().getFoxHttpLogger().log(FoxHttpLoggerLevel.DEBUG, "-> [RESPONSE_CODE] " + interceptor);
                ((FoxHttpResponseCodeInterceptor) interceptor).onIntercept(context);
            }
        }
    }

}
