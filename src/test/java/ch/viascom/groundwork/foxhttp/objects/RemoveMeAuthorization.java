package ch.viascom.groundwork.foxhttp.objects;

import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorization;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationContext;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;

/**
 * @author patrick.boesch@viascom.ch
 */
public class RemoveMeAuthorization implements FoxHttpAuthorization {
    @Override
    public void doAuthorization(FoxHttpAuthorizationContext authorizationContext, FoxHttpAuthorizationScope foxHttpAuthorizationScope) {
        authorizationContext.getUrlConnection().setRequestProperty("Remove-Me", "-----");
    }
}
