package ch.viascom.groundwork.foxhttp.annotation.types;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The @Part annotation defines a key-value pair in a multipart request body. The value of the key-value pair can be a {@link java.lang.String String}, {@link java.io.File File} or
 * {@link java.io.InputStream InputStream}. Can only be used in multipart requests.
 *
 * @author patrick.boesch@viascom.ch
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Part {

    String value();
}
