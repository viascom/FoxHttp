package ch.viascom.groundwork.foxhttp.authorization;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;

/**
 * FoxHttpAuthorization interface
 *
 * @author patrick.boesch@viascom.ch
 */
@FunctionalInterface
public interface FoxHttpAuthorization {

    void doAuthorization(FoxHttpAuthorizationContext authorizationContext, FoxHttpAuthorizationScope foxHttpAuthorizationScope) throws FoxHttpRequestException;
}
