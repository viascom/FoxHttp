package ch.viascom.groundwork.foxhttp.authorization;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.placeholder.FoxHttpPlaceholderStrategy;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import lombok.ToString;

/**
 * Default RegExAuthorizationStrategy for FoxHttp <p> Stores FoxHttpAuthorization with a FoxHttpAuthorizationScope as key.
 *
 * @author patrick.boesch@viascom.ch
 */
@ToString
public class RegExAuthorizationStrategy extends DefaultAuthorizationStrategy {

    /**
     * Returns a list of matching FoxHttpAuthorizations based on the given FoxHttpAuthorizationScope
     *
     * @param connection connection of the request
     * @param searchScope looking for scope
     */
    @Override
    public List<FoxHttpAuthorization> getAuthorization(URLConnection connection, FoxHttpAuthorizationScope searchScope, FoxHttpClient foxHttpClient,
        FoxHttpPlaceholderStrategy foxHttpPlaceholderStrategy) {
        ArrayList<FoxHttpAuthorization> foxHttpAuthorizationList = new ArrayList<>();

        getFoxHttpAuthorizations().entrySet()
                                  .stream()
                                  .filter(entry -> searchScope.toString().matches(entry.getKey()))
                                  .forEach(entry -> foxHttpAuthorizationList.addAll(entry.getValue().values()));

        if (getFoxHttpAuthorizations().containsKey(FoxHttpAuthorizationScope.ANY.toString()) && (foxHttpAuthorizationList.isEmpty())) {
            foxHttpAuthorizationList.addAll(getFoxHttpAuthorizations().get(FoxHttpAuthorizationScope.ANY.toString()).values());
        }

        return foxHttpAuthorizationList;
    }
}
