package ch.viascom.groundwork.foxhttp.lambda.interceptor;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestInterceptorContext;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author patrick.boesch@viascom.ch
 */
@AllArgsConstructor
public class LambdaRequestInterceptor implements FoxHttpRequestInterceptor {

    private LambdaRequestOnIntercept lambdaRequestOnIntercept;
    @Getter
    private int weight = 0;

    @Override
    public void onIntercept(FoxHttpRequestInterceptorContext context) throws FoxHttpException {
        lambdaRequestOnIntercept.accept(context);
    }

    public interface LambdaRequestOnIntercept extends Consumer<FoxHttpRequestInterceptorContext> {

    }
}
