package ch.viascom.groundwork.foxhttp.lambda.interceptor;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.response.FoxHttpResponseBodyInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseBodyInterceptorContext;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author patrick.boesch@viascom.ch
 */
@AllArgsConstructor
public class LambdaResponseBodyInterceptor implements FoxHttpResponseBodyInterceptor {

    private LambdaResponseBodyOnIntercept lambdaResponseBodyOnIntercept;
    @Getter
    private int weight = 0;

    @Override
    public void onIntercept(FoxHttpResponseBodyInterceptorContext context) throws FoxHttpException {
        lambdaResponseBodyOnIntercept.accept(context);
    }

    public interface LambdaResponseBodyOnIntercept extends Consumer<FoxHttpResponseBodyInterceptorContext> {

    }
}
