package ch.viascom.groundwork.foxhttp.component.oauth2;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;
import ch.viascom.groundwork.foxhttp.component.FoxHttpComponent;
import ch.viascom.groundwork.foxhttp.component.oauth2.authorization.OAuth2Authorization;
import ch.viascom.groundwork.foxhttp.component.oauth2.authorization.OAuth2BearerTokenAuthorization;
import ch.viascom.groundwork.foxhttp.component.oauth2.interceptor.OAuth2RequestInterceptor;
import ch.viascom.groundwork.foxhttp.component.oauth2.request.DefaultOAuth2AuthorizationCodeRequestGenerator;
import ch.viascom.groundwork.foxhttp.component.oauth2.request.DefaultOAuth2ClientCredentialRequestGenerator;
import ch.viascom.groundwork.foxhttp.component.oauth2.request.DefaultOAuth2PasswordRequestGenerator;
import ch.viascom.groundwork.foxhttp.component.oauth2.request.DefaultOAuth2RefreshTokenRequestGenerator;
import ch.viascom.groundwork.foxhttp.component.oauth2.request.DefaultOAuth2RequestExecutor;
import ch.viascom.groundwork.foxhttp.component.oauth2.request.OAuth2RequestExecutor;
import ch.viascom.groundwork.foxhttp.component.oauth2.request.OAuth2RequestGenerator;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptorType;
import ch.viascom.groundwork.foxhttp.log.FoxHttpLoggerLevel;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * @author patrick.boesch@viascom.ch
 */
@Getter
@Setter
public class OAuth2Component implements FoxHttpComponent {

    private FoxHttpClient foxHttpClient;
    private OAuth2Store oAuth2Store;

    private OAuth2Authorization oAuth2Authorization;
    private Map<GrantType, OAuth2RequestGenerator> oAuth2RequestGenerators = new HashMap<>();
    private OAuth2RequestExecutor oAuth2RequestExecutor;

    /**
     * Get a new OAuth2 component for foxhttp
     *
     * @param oAuth2Store instance of a oAuthStore
     */
    public OAuth2Component(OAuth2Store oAuth2Store) {
        this.oAuth2Store = oAuth2Store;

        //Add defaults
        oAuth2RequestGenerators.put(GrantType.CLIENT_CREDENTIALS, new DefaultOAuth2ClientCredentialRequestGenerator());
        oAuth2RequestGenerators.put(GrantType.AUTHORIZATION_CODE, new DefaultOAuth2AuthorizationCodeRequestGenerator());
        oAuth2RequestGenerators.put(GrantType.PASSWORD, new DefaultOAuth2PasswordRequestGenerator());
        oAuth2RequestGenerators.put(GrantType.REFRESH_TOKEN, new DefaultOAuth2RefreshTokenRequestGenerator());

        oAuth2RequestExecutor = new DefaultOAuth2RequestExecutor();
    }

    @Override
    public void initiation(FoxHttpClient foxHttpClient) throws FoxHttpException {
        this.foxHttpClient = foxHttpClient;
        foxHttpClient.getFoxHttpLogger().log(FoxHttpLoggerLevel.INFO, "========= Initiate  OAuth2Component =========");
        foxHttpClient.getFoxHttpLogger().log(FoxHttpLoggerLevel.DEBUG, "-> Register authorizations");
        //Register authorization
        oAuth2Authorization = new OAuth2BearerTokenAuthorization(oAuth2Store.getAccessToken());
        for (FoxHttpAuthorizationScope scope : oAuth2Store.getAuthScopes()) {
            foxHttpClient.getFoxHttpAuthorizationStrategy().addAuthorization(scope, oAuth2Authorization);
        }
        foxHttpClient.getFoxHttpLogger().log(FoxHttpLoggerLevel.DEBUG, "-> Register interceptor");
        //Register interceptor
        foxHttpClient.getFoxHttpInterceptorStrategy().addInterceptor(FoxHttpInterceptorType.REQUEST_CONNECTION, new OAuth2RequestInterceptor(this, 100));
        foxHttpClient.getFoxHttpLogger().log(FoxHttpLoggerLevel.DEBUG, "=============================================");
    }


    public FoxHttpRequest generateRequestForGrantType(GrantType grantType) throws MalformedURLException, FoxHttpRequestException, IllegalAccessException, InstantiationException {
        OAuth2RequestGenerator oAuth2RequestGenerator = oAuth2RequestGenerators.get(grantType);
        return oAuth2RequestGenerator.getRequest(this);
    }

    /**
     * Request a new token based on the configuration
     *
     * @param grantType grant type to use
     * @return access token from the response
     */
    public String getNewToken(GrantType grantType) throws FoxHttpException, MalformedURLException, InstantiationException, IllegalAccessException {
        FoxHttpRequest request = this.generateRequestForGrantType(grantType);
        this.getOAuth2RequestExecutor().executeOAuth2Request(request, this);
        return getOAuth2Store().getAccessToken();
    }

    /**
     * Request a new token based on the configuration
     *
     * @return access token from the response
     */
    public String getNewToken() throws FoxHttpException, MalformedURLException, InstantiationException, IllegalAccessException {
        FoxHttpRequest request = this.generateRequestForGrantType(this.getOAuth2Store().getGrantType());
        this.getOAuth2RequestExecutor().executeOAuth2Request(request, this);
        return getOAuth2Store().getAccessToken();
    }
}
