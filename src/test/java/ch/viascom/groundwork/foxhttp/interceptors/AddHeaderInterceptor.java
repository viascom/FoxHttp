package ch.viascom.groundwork.foxhttp.interceptors;

import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestConnectionInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestConnectionInterceptorContext;

public class AddHeaderInterceptor  implements FoxHttpRequestConnectionInterceptor {

    @Override
    public int getWeight() {
        return 1000;
    }

    @Override
    public void onIntercept(FoxHttpRequestConnectionInterceptorContext context) {
        context.getRequest().getRequestHeader().addHeader("X-Test-Header", "test");
    }
}
