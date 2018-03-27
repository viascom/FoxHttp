package ch.viascom.groundwork.foxhttp.annotation.types;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The @QueryName annotation defines the name of an attribute if a object gets parsed bei the @QueryObject annotation.
 *
 * @author patrick.boesch@viascom.ch
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryName {

    String value() default "";

    boolean allowOptional() default false;
}
