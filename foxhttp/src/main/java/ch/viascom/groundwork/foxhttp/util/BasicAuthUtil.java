package ch.viascom.groundwork.foxhttp.util;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

/**
 * @author patrick.boesch@viascom.ch
 */
public class BasicAuthUtil {

    public static String getBasicAuthenticationEncoding(String username, String password) throws FoxHttpRequestException {
        return getBasicAuthenticationEncoding(username, password, Charset.forName("UTF-8"));
    }

    /**
     * Create user:password string for authentication.
     *
     * @return user:password string
     */
    public static String getBasicAuthenticationEncoding(String username, String password, Charset charset) throws FoxHttpRequestException {
        String userPassword = username + ":" + password;

        Class<?> base64;
        try {
            base64 = Class.forName("java.util.Base64");
            Object objectToInvokeOn = base64.getEnclosingClass();
            Method encoderMethod = base64.getDeclaredMethod("getEncoder");
            Object encoder = encoderMethod.invoke(objectToInvokeOn);
            Method method = encoder.getClass().getDeclaredMethod("encodeToString", byte[].class);

            return (String) (method.invoke(encoder, (Object) userPassword.getBytes(charset)));
        } catch (ClassNotFoundException e) {
            try {
                base64 = Class.forName("android.util.Base64");

                Object objectToInvokeOn = base64.getEnclosingClass();
                Method encoderMethod = base64.getDeclaredMethod("encodeToString", byte[].class, int.class);

                return (String) (encoderMethod.invoke(objectToInvokeOn, userPassword.getBytes(charset), 2));
            } catch (Exception e1) {
                throw new FoxHttpRequestException(e1);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new FoxHttpRequestException(e);
        }

    }
}
