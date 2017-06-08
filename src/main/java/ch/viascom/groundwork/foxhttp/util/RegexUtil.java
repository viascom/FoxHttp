package ch.viascom.groundwork.foxhttp.util;

/**
 * @author patrick.boesch@viascom.ch
 */
public class RegexUtil {

    /**
     * Utility classes, which are a collection of static members, are not meant to be instantiated.
     */
    private RegexUtil() {
        throw new IllegalAccessError("Utility class");
    }

    public static boolean doesURLMatch(String input, String pattern) {
        if (input.equals(pattern)) {
            return true;
        }
        String matchRegex = pattern.replaceAll("(?<!(\\[ -~\\]))(\\*)", "[ -~]*").replaceAll("\\.", "\\\\.").replaceAll("\\/", "\\\\/");
        return input.matches(matchRegex);
    }
}