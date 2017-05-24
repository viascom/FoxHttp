package ch.viascom.groundwork.foxhttp.annotation.types;

import java.lang.annotation.*;

/**
 * The @QueryObject annotation defines an object which will be parsed and
 * used as part of the URL path.
 *
 * The value is either:
 * - an array of attribute names which are included in the result
 * - empty and in this case all attributes are included
 *
 * @author patrick.boesch@viascom.ch
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryObject {
    String[] value() default "";
}
