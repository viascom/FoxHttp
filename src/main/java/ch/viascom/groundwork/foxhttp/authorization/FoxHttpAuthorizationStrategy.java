package ch.viascom.groundwork.foxhttp.authorization;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.placeholder.FoxHttpPlaceholderStrategy;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * FoxHttpAuthorizationStrategy interface
 *
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpAuthorizationStrategy {

    HashMap<String, HashMap<String, FoxHttpAuthorization>> getFoxHttpAuthorizations();

    void setFoxHttpAuthorizations(HashMap<String, HashMap<String, FoxHttpAuthorization>> authorizations);

    List<FoxHttpAuthorization> getAuthorization(URLConnection connection, FoxHttpAuthorizationScope searchScope, FoxHttpClient foxHttpClient,
        FoxHttpPlaceholderStrategy foxHttpPlaceholderStrategy) throws FoxHttpRequestException;

    void addAuthorization(FoxHttpAuthorizationScope scope, FoxHttpAuthorization authorization);

    void addAuthorization(FoxHttpAuthorizationScope scope, FoxHttpAuthorization authorization, String key);

    void addAuthorization(List<FoxHttpAuthorizationScope> scopes, FoxHttpAuthorization authorization);

    void addAuthorization(List<FoxHttpAuthorizationScope> scopes, FoxHttpAuthorization authorization, String key);

    void removeAuthorizationByKey(FoxHttpAuthorizationScope scope, String key);

    void removeAuthorizationByClass(FoxHttpAuthorizationScope scope, Class<? extends FoxHttpAuthorization> clazz);

    void replaceAuthorization(FoxHttpAuthorizationScope scope, FoxHttpAuthorization newAuthorization, String key);

    FoxHttpAuthorization getAuthorizationByKey(FoxHttpAuthorizationScope scope, String key);

    ArrayList<FoxHttpAuthorization> getAuthorizationsByClass(FoxHttpAuthorizationScope scope, Class<? extends FoxHttpAuthorization> clazz);

    HashMap<String, FoxHttpAuthorization> getAllAuthorizationsFromScope(FoxHttpAuthorizationScope scope);

    ArrayList<FoxHttpAuthorization> getAllAuthorizationsFromScopeAsArray(FoxHttpAuthorizationScope scope);

    boolean doesScopeExist(FoxHttpAuthorizationScope scope);
}
