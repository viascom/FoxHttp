package ch.viascom.groundwork.foxhttp.lambda.interceptor;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.response.FoxHttpResponseInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseInterceptorContext;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Consumer;

/**
 * @author patrick.boesch@viascom.ch
 */
@AllArgsConstructor
public class LambdaResponseInterceptor implements FoxHttpResponseInterceptor {
    private LambdaResponseOnIntercept lambdaResponseOnIntercept;
    @Getter
    private int weight = 0;


    @Override
    public void onIntercept(FoxHttpResponseInterceptorContext context) throws FoxHttpException {
        lambdaResponseOnIntercept.accept(context);
    }

    public interface LambdaResponseOnIntercept extends Consumer<FoxHttpResponseInterceptorContext> {

    }
}
