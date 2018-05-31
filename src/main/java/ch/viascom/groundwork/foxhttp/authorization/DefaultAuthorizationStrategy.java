package ch.viascom.groundwork.foxhttp.authorization;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.placeholder.FoxHttpPlaceholderStrategy;
import ch.viascom.groundwork.foxhttp.util.RegexUtil;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

/**
 * Default AuthorizationStrategy for FoxHttp <p> Stores FoxHttpAuthorization with a FoxHttpAuthorizationScope as key.
 *
 * @author patrick.boesch@viascom.ch
 */
@ToString
public class DefaultAuthorizationStrategy implements FoxHttpAuthorizationStrategy {


    /**
     * AuthorizationStrategy store
     */
    @Getter
    @Setter
    private HashMap<String, HashMap<String, FoxHttpAuthorization>> foxHttpAuthorizations = new HashMap<>();

    /**
     * Add a new FoxHttpAuthorization to the AuthorizationStrategy
     *
     * @param scope scope in which the authorization is used
     * @param authorization authorization itself
     */
    @Override
    public void addAuthorization(FoxHttpAuthorizationScope scope, FoxHttpAuthorization authorization) {
        addAuthorization(scope, authorization, String.valueOf(UUID.randomUUID()));
    }


    public void addAuthorization(FoxHttpAuthorizationScope scope, FoxHttpAuthorization authorization, String key) {
        if (foxHttpAuthorizations.containsKey(scope.toString())) {
            foxHttpAuthorizations.get(scope.toString()).put(key, authorization);
        } else {
            HashMap<String, FoxHttpAuthorization> foxHttpAuthorizationsMap = new HashMap<>();
            foxHttpAuthorizationsMap.put(key, authorization);
            foxHttpAuthorizations.put(scope.toString(), foxHttpAuthorizationsMap);
        }
    }

    /**
     * Add a new FoxHttpAuthorization to the AuthorizationStrategy
     *
     * @param scopes scopes in which the authorization is used
     * @param authorization authorization itself
     */
    @Override
    public void addAuthorization(List<FoxHttpAuthorizationScope> scopes, FoxHttpAuthorization authorization) {
        addAuthorization(scopes, authorization, String.valueOf(UUID.randomUUID()));
    }

    public void addAuthorization(List<FoxHttpAuthorizationScope> scopes, FoxHttpAuthorization authorization, String key) {
        scopes.forEach(scope -> addAuthorization(scope, authorization, key));
    }


    /**
     * Returns a list of matching FoxHttpAuthorizations based on the given FoxHttpAuthorizationScope
     *
     * @param connection connection of the request
     * @param searchScope looking for scope
     */
    @Override
    public List<FoxHttpAuthorization> getAuthorization(URLConnection connection, FoxHttpAuthorizationScope searchScope, FoxHttpClient foxHttpClient,
        FoxHttpPlaceholderStrategy foxHttpPlaceholderStrategy) {

        ArrayList<FoxHttpAuthorization> foxHttpAuthorizationList = foxHttpAuthorizations.entrySet().stream().filter(entry -> {
            try {
                return RegexUtil.doesURLMatch(searchScope.toString(), foxHttpPlaceholderStrategy.processPlaceholders(entry.getKey(), foxHttpClient));
            } catch (FoxHttpRequestException e) {
                throw new RuntimeException(e.getMessage());
            }
        }).map(entry -> entry.getValue().values()).flatMap(Collection::stream).collect(Collectors.toCollection(ArrayList::new));

        if (doesScopeExist(FoxHttpAuthorizationScope.ANY) && (foxHttpAuthorizationList.isEmpty())) {
            foxHttpAuthorizationList.addAll(foxHttpAuthorizations.get(FoxHttpAuthorizationScope.ANY.toString()).values());
        }

        return foxHttpAuthorizationList;
    }


    /**
     * Remove a defined FoxHttpAuthorization from the AuthorizationStrategy
     *
     * @param scope scope in which the authorization is used
     * @param key key of the authorization
     */
    @Override
    public void removeAuthorizationByKey(FoxHttpAuthorizationScope scope, String key) {
        if (doesScopeExist(scope)) {
            foxHttpAuthorizations.put(scope.toString(), new HashMap<>(foxHttpAuthorizations.get(scope.toString())
                                                                                           .entrySet()
                                                                                           .stream()
                                                                                           .filter(entry -> !entry.getKey().equals(key))
                                                                                           .collect(Collectors.toMap(Entry::getKey, Entry::getValue))));
        }
    }

    /**
     * Remove a defined FoxHttpAuthorization from the AuthorizationStrategy
     *
     * @param scope scope in which the authorization is used
     * @param clazz class of the authorization
     */
    @Override
    public void removeAuthorizationByClass(FoxHttpAuthorizationScope scope, Class<? extends FoxHttpAuthorization> clazz) {
        if (doesScopeExist(scope)) {
            foxHttpAuthorizations.put(scope.toString(), new HashMap<>(foxHttpAuthorizations.get(scope.toString())
                                                                                           .entrySet()
                                                                                           .stream()
                                                                                           .filter(entry -> !entry.getValue().getClass().isAssignableFrom(clazz))
                                                                                           .collect(Collectors.toMap(Entry::getKey, Entry::getValue))));
        }
    }

    @Override
    public void replaceAuthorization(FoxHttpAuthorizationScope scope, FoxHttpAuthorization newAuthorization, String key) {
        removeAuthorizationByKey(scope, key);
        if (doesScopeExist(scope)) {
            foxHttpAuthorizations.get(scope.toString()).put(key, newAuthorization);
        }
    }

    @Override
    public FoxHttpAuthorization getAuthorizationByKey(FoxHttpAuthorizationScope scope, String key) {
        if (doesScopeExist(scope)) {
            return foxHttpAuthorizations.get(scope.toString()).get(key);
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<FoxHttpAuthorization> getAuthorizationsByClass(FoxHttpAuthorizationScope scope, Class<? extends FoxHttpAuthorization> clazz) {
        ArrayList<FoxHttpAuthorization> authorizationList = new ArrayList<>();
        if (doesScopeExist(scope)) {
            authorizationList.addAll(foxHttpAuthorizations.get(scope.toString())
                                 .entrySet()
                                 .stream()
                                 .filter((Map.Entry<String, FoxHttpAuthorization> authorization) -> authorization.getValue().getClass().isAssignableFrom(clazz))
                                 .map(Map.Entry::getValue)
                                 .collect(Collectors.toList()));
        }
        return authorizationList;
    }

    @Override
    public HashMap<String, FoxHttpAuthorization> getAllAuthorizationsFromScope(FoxHttpAuthorizationScope scope) {
        return foxHttpAuthorizations.get(scope.toString());
    }

    @Override
    public ArrayList<FoxHttpAuthorization> getAllAuthorizationsFromScopeAsArray(FoxHttpAuthorizationScope scope) {
        ArrayList<FoxHttpAuthorization> innerAuthorizationList = new ArrayList<>();
        if (doesScopeExist(scope)) {
            innerAuthorizationList.addAll(foxHttpAuthorizations.get(scope.toString())
                                 .entrySet()
                                 .stream()
                                 .map(Map.Entry::getValue)
                                 .collect(Collectors.toList()));
        }
        return innerAuthorizationList;
    }

    @Override
    public boolean doesScopeExist(FoxHttpAuthorizationScope scope) {
        return foxHttpAuthorizations.containsKey(scope.toString());
    }
}
