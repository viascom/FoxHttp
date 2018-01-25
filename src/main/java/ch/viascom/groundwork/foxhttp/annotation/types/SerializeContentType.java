package ch.viascom.groundwork.foxhttp.annotation.types;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SerializeContentType {
    String charset();
    String mimeType();
}
