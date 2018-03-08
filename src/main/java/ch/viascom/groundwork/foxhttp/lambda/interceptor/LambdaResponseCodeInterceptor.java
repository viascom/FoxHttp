package ch.viascom.groundwork.foxhttp.lambda.interceptor;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.response.FoxHttpResponseCodeInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseCodeInterceptorContext;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author patrick.boesch@viascom.ch
 */
@AllArgsConstructor
public class LambdaResponseCodeInterceptor implements FoxHttpResponseCodeInterceptor {

    private LambdaResponseCodeOnIntercept lambdaResponseCodeOnIntercept;
    @Getter
    private int weight = 0;

    @Override
    public void onIntercept(FoxHttpResponseCodeInterceptorContext context) throws FoxHttpException {
        lambdaResponseCodeOnIntercept.accept(context);
    }

    public interface LambdaResponseCodeOnIntercept extends Consumer<FoxHttpResponseCodeInterceptorContext> {

    }
}
