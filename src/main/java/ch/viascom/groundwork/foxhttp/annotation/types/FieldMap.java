package ch.viascom.groundwork.foxhttp.annotation.types;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The @FieldMao annotation defines a map of key-value pairs in a form-url-encoded request body. Can only be used in form-url-encoded requests.
 *
 * @author patrick.boesch@viascom.ch
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldMap {

}
