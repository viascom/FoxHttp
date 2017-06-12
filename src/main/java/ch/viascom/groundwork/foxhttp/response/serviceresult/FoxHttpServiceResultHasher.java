package ch.viascom.groundwork.foxhttp.response.serviceresult;

import ch.viascom.groundwork.foxhttp.response.FoxHttpResultHasher;

/**
 * @author patrick.boesch@viascom.ch
 */
@FunctionalInterface
public interface FoxHttpServiceResultHasher extends FoxHttpResultHasher {
    String hash(Object result, String rawBody);
}
