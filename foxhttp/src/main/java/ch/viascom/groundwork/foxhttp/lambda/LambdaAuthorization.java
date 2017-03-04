package ch.viascom.groundwork.foxhttp.lambda;

import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorization;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationContext;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import lombok.AllArgsConstructor;

import java.util.function.BiConsumer;

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
