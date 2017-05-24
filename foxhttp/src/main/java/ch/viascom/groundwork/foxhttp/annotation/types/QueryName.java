package ch.viascom.groundwork.foxhttp.annotation.types;

import java.lang.annotation.*;

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
}
