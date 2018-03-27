package ch.viascom.groundwork.foxhttp.response.serviceresult;

import ch.viascom.groundwork.serviceresult.ServiceResult;
import ch.viascom.groundwork.serviceresult.util.ObjectHasher;

/**
 * @author patrick.boesch@viascom.ch
 */
public class DefaultServiceResultHasher implements FoxHttpServiceResultHasher {

    @Override
    public String hash(Object result, String rawBody) {
        return ObjectHasher.hash(((ServiceResult) result).getContent());
    }
}
