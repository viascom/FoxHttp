package ch.viascom.groundwork.foxhttp.response;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpResultHasher {

    String hash(Object result, String rawBody);
}
