package ch.viascom.groundwork.foxhttp.lambda.interceptor;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestConnectionInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestConnectionInterceptorContext;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author patrick.boesch@viascom.ch
 */
@AllArgsConstructor
public class LambdaRequestConnectionInterceptor implements FoxHttpRequestConnectionInterceptor {

    private LambdaRequestConnectionOnIntercept lambdaRequestConnectionOnIntercept;
    @Getter
    private int weight = 0;

    @Override
    public void onIntercept(FoxHttpRequestConnectionInterceptorContext context) throws FoxHttpException {
        lambdaRequestConnectionOnIntercept.accept(context);
    }

    public interface LambdaRequestConnectionOnIntercept extends Consumer<FoxHttpRequestConnectionInterceptorContext> {

    }
}
