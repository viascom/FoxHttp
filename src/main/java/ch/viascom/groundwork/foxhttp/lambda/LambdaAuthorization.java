package ch.viascom.groundwork.foxhttp.lambda;

import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorization;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationContext;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import java.util.function.BiConsumer;
import lombok.AllArgsConstructor;

/**
 * @author patrick.boesch@viascom.ch
 */
@AllArgsConstructor
public class LambdaAuthorization implements FoxHttpAuthorization {

    private LambdaAuthorizationDoAuthorization lambdaAuthorizationDoAuthorization;

    @Override
    public void doAuthorization(FoxHttpAuthorizationContext authorizationContext, FoxHttpAuthorizationScope foxHttpAuthorizationScope) throws FoxHttpRequestException {
        lambdaAuthorizationDoAuthorization.accept(authorizationContext, foxHttpAuthorizationScope);
    }

    public interface LambdaAuthorizationDoAuthorization extends BiConsumer<FoxHttpAuthorizationContext, FoxHttpAuthorizationScope> {

    }
}
