package ch.viascom.groundwork.foxhttp.lambda.interceptor;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestBodyInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestBodyInterceptorContext;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Consumer;

/**
 * @author patrick.boesch@viascom.ch
 */
@AllArgsConstructor
public class LambdaRequestBodyInterceptor implements FoxHttpRequestBodyInterceptor {
    private LambdaRequestBodyOnIntercept lambdaRequestBodyOnIntercept;
    @Getter
    private int weight = 0;

    @Override
    public void onIntercept(FoxHttpRequestBodyInterceptorContext context) throws FoxHttpException {
        lambdaRequestBodyOnIntercept.accept(context);
    }

    public interface LambdaRequestBodyOnIntercept extends Consumer<FoxHttpRequestBodyInterceptorContext> {

    }
}
