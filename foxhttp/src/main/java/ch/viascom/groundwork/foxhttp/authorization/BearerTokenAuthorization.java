package ch.viascom.groundwork.foxhttp.authorization;

import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import lombok.*;

/**
 * Bearer Token for FoxHttp
 * <p>
 * This FoxHttpAuthorization will create a header with data for a Bearer Token authentication.
 *
 * @author patrick.boesch@viascom.ch
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BearerTokenAuthorization implements FoxHttpAuthorization {

    private String token;
    private String headerPrefix = "Bearer";

    public BearerTokenAuthorization(String token){
        this.token = token;
    }

    @Override
    public void doAuthorization(FoxHttpAuthorizationContext authorizationContext, FoxHttpAuthorizationScope foxHttpAuthorizationScope) {
        authorizationContext.getUrlConnection().setRequestProperty(HeaderTypes.AUTHORIZATION.toString(), headerPrefix + " " + token);
    }
}
