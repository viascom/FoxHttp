package ch.viascom.groundwork.foxhttp.lambda.interceptor;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestHeaderInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestHeaderInterceptorContext;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author patrick.boesch@viascom.ch
 */
@AllArgsConstructor
public class LambdaRequestHeaderInterceptor implements FoxHttpRequestHeaderInterceptor {

    private LambdaRequestHeaderOnIntercept lambdaRequestHeaderOnIntercept;
    @Getter
    private int weight = 0;


    @Override
    public void onIntercept(FoxHttpRequestHeaderInterceptorContext context) throws FoxHttpException {
        lambdaRequestHeaderOnIntercept.accept(context);
    }

    public interface LambdaRequestHeaderOnIntercept extends Consumer<FoxHttpRequestHeaderInterceptorContext> {

    }
}