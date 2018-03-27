package ch.viascom.groundwork.foxhttp.annotation.types;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The @Path annotation defines the endpoint path and can't be empty. Can also be used as a parameter annotation and in this case the method annotation @Path have to contain a
 * place holder and this place holder will be replaced with the parameter value.
 *
 * @author patrick.boesch@viascom.ch
 */
@Documented
@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Path {

    String value() default "";

    boolean preProcessPlaceholders() default true;
}
