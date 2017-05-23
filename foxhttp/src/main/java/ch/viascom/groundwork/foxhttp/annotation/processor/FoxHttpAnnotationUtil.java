package ch.viascom.groundwork.foxhttp.annotation.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author patrick.boesch@viascom.ch
 */
class FoxHttpAnnotationUtil {

    /**
     * Utility classes, which are a collection of static members, are not meant to be instantiated.
     */
    private FoxHttpAnnotationUtil() {
        throw new IllegalAccessError("Utility class");
    }

    static boolean hasMethodAnnotation(Class<? extends Annotation> annotationClass, Method method) {
        return method.getAnnotation(annotationClass) != null;
    }

    static boolean hasTypeAnnotation(Class<? extends Annotation> annotationClass, Method method) {
        return method.getDeclaringClass().getAnnotation(annotationClass) != null;
    }

    static boolean hasParameterAnnotation(Class<? extends Annotation> annotationClass, Method method) {
        return getParameterAnnotation(annotationClass, method) > 0;
    }

    static int getParameterAnnotation(Class<? extends Annotation> annotationClass, Method method) {
        int count = 0;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == annotationClass) {
                    count++;
                }
            }
        }
        return count;
    }

    static Class<?> getParameterAnnotationTpe(Class<? extends Annotation> annotationClass, Method method) {
        int parameterPos = 0;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == annotationClass) {
                    return method.getParameterTypes()[parameterPos];
                }
            }
            parameterPos++;
        }

        return void.class;
    }
}
