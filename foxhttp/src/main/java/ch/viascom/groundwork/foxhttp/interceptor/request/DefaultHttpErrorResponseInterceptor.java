package ch.viascom.groundwork.foxhttp.interceptor.request;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpResponseException;
import ch.viascom.groundwork.foxhttp.interceptor.response.FoxHttpResponseBodyInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.FoxHttpResponseCodeInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.FoxHttpResponseInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseBodyInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseCodeInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseInterceptorContext;

/**
 * @author patrick.boesch@viascom.ch
 */
public class DefaultHttpErrorResponseInterceptor implements FoxHttpResponseInterceptor, FoxHttpResponseCodeInterceptor, FoxHttpResponseBodyInterceptor {

    /**
     * Sets the weight to 1'000 of the DefaultHttpErrorResponseInterceptor
     * <p>
     * Interceptors with a smaller weight number will be executed before this one
     * and interceptors with a higher weight number will be executed after this interceptor.
     * <p>
     * Override the weight if you wanna change the execution order of this interceptor.
     *
     * @return 1000
     */
    @Override
    public int getWeight() {
        return 1_000;
    }

    @Override
    public void onIntercept(FoxHttpResponseInterceptorContext context) throws FoxHttpException {
        if (!isValidResponseCode(context.getResponseCode())) {
            throw new FoxHttpResponseException(String.valueOf("Response Error-Code: " + context.getResponseCode()), context.getFoxHttpResponse());
        }
    }

    @Override
    public void onIntercept(FoxHttpResponseCodeInterceptorContext context) throws FoxHttpException {
        if (!isValidResponseCode(context.getResponseCode())) {
            throw new FoxHttpResponseException(String.valueOf("Response Error-Code: " + context.getResponseCode()));
        }
    }

    @Override
    public void onIntercept(FoxHttpResponseBodyInterceptorContext context) throws FoxHttpException {
        if (!isValidResponseCode(context.getResponseCode())) {
            throw new FoxHttpResponseException(String.valueOf("Response Error-Code: " + context.getResponseCode()), context.getFoxHttpResponse());
        }
    }

    /**
     * Check if the status code is valid
     *
     * @param statuscode status code of the response
     *
     * @return true if the status code is valid.
     */
    public boolean isValidResponseCode(int statuscode) {
        return (statuscode >= 200 && statuscode < 300);
    }

}
