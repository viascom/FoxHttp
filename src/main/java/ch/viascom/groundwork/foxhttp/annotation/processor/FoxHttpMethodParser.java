package ch.viascom.groundwork.foxhttp.annotation.processor;

import static ch.viascom.groundwork.foxhttp.annotation.processor.FoxHttpAnnotationUtil.getParameterAnnotation;
import static ch.viascom.groundwork.foxhttp.annotation.processor.FoxHttpAnnotationUtil.getParameterAnnotationTpe;
import static ch.viascom.groundwork.foxhttp.annotation.processor.FoxHttpAnnotationUtil.hasMethodAnnotation;
import static ch.viascom.groundwork.foxhttp.annotation.processor.FoxHttpAnnotationUtil.hasParameterAnnotation;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import ch.viascom.groundwork.foxhttp.FoxHttpResponse;
import ch.viascom.groundwork.foxhttp.annotation.types.Body;
import ch.viascom.groundwork.foxhttp.annotation.types.DELETE;
import ch.viascom.groundwork.foxhttp.annotation.types.Field;
import ch.viascom.groundwork.foxhttp.annotation.types.FieldMap;
import ch.viascom.groundwork.foxhttp.annotation.types.FollowRedirect;
import ch.viascom.groundwork.foxhttp.annotation.types.FormUrlEncodedBody;
import ch.viascom.groundwork.foxhttp.annotation.types.GET;
import ch.viascom.groundwork.foxhttp.annotation.types.HEAD;
import ch.viascom.groundwork.foxhttp.annotation.types.Header;
import ch.viascom.groundwork.foxhttp.annotation.types.HeaderField;
import ch.viascom.groundwork.foxhttp.annotation.types.MultipartBody;
import ch.viascom.groundwork.foxhttp.annotation.types.OPTIONS;
import ch.viascom.groundwork.foxhttp.annotation.types.POST;
import ch.viascom.groundwork.foxhttp.annotation.types.PUT;
import ch.viascom.groundwork.foxhttp.annotation.types.Part;
import ch.viascom.groundwork.foxhttp.annotation.types.PartMap;
import ch.viascom.groundwork.foxhttp.annotation.types.Path;
import ch.viascom.groundwork.foxhttp.annotation.types.Query;
import ch.viascom.groundwork.foxhttp.annotation.types.QueryMap;
import ch.viascom.groundwork.foxhttp.annotation.types.QueryObject;
import ch.viascom.groundwork.foxhttp.annotation.types.SkipResponseBody;
import ch.viascom.groundwork.foxhttp.body.request.FoxHttpRequestBody;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.header.FoxHttpHeader;
import ch.viascom.groundwork.foxhttp.type.RequestType;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import lombok.Getter;

/**
 * @author patrick.boesch@viascom.ch
 */
@Getter
class FoxHttpMethodParser {

    private Method method;
    private String path;
    private boolean completePath;
    private URL url;
    private RequestType requestType;
    private boolean hasBody;
    private Class<?> responseType;
    private FoxHttpClient foxHttpClient;
    private FoxHttpHeader headerFields = new FoxHttpHeader();
    private boolean skipResponseBody = false;
    private boolean followRedirect = true;


    void parseMethod(Method method, FoxHttpClient foxHttpClient) throws FoxHttpRequestException {

        this.method = method;
        this.foxHttpClient = foxHttpClient;

        parseClassHeaders();

        parseReturnType(method.getReturnType());

        parseSkipResponseBodyAndFollowRedirect();

        for (Annotation annotation : method.getAnnotations()) {
            parsetMethodAnnotation(annotation);
        }

        parseURL();

    }

    private void parseClassHeaders() throws FoxHttpRequestException {

        for (Annotation annotation : method.getDeclaringClass().getAnnotations()) {
            if (annotation instanceof Header) {
                setHeader((Header) annotation);
            }
        }
    }

    private void parseSkipResponseBodyAndFollowRedirect() {
        //From class
        SkipResponseBody skipResponseBodyAnnotation = method.getDeclaringClass().getAnnotation(SkipResponseBody.class);
        if (skipResponseBodyAnnotation != null) {
            skipResponseBody = skipResponseBodyAnnotation.value();
        }

        FollowRedirect followRedirectAnnotation = method.getDeclaringClass().getAnnotation(FollowRedirect.class);
        if (followRedirectAnnotation != null) {
            followRedirect = followRedirectAnnotation.value();
        }

        //From method
        skipResponseBodyAnnotation = method.getAnnotation(SkipResponseBody.class);
        if (skipResponseBodyAnnotation != null) {
            skipResponseBody = skipResponseBodyAnnotation.value();
        }

        followRedirectAnnotation = method.getAnnotation(FollowRedirect.class);
        if (followRedirectAnnotation != null) {
            followRedirect = followRedirectAnnotation.value();
        }
    }

    private void parseURL() throws FoxHttpRequestException {
        Path basePath = method.getDeclaringClass().getAnnotation(Path.class);

        String url = path;
        if (!completePath && basePath != null) {
            url = basePath.value() + url;
        }

        if (basePath != null && basePath.preProcessPlaceholders()) {
            for (Map.Entry<String, String> entry : foxHttpClient.getFoxHttpPlaceholderStrategy().getPlaceholderMap().entrySet()) {
                url = url.replace(foxHttpClient.getFoxHttpPlaceholderStrategy().getPlaceholderEscapeCharStart() + entry.getKey() + foxHttpClient.getFoxHttpPlaceholderStrategy()
                                                                                                                                                .getPlaceholderEscapeCharEnd(),
                    entry.getValue());
            }
        }

        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            throw new FoxHttpRequestException(e);
        }
    }

    private void parseReturnType(Class<?> responseType) throws FoxHttpRequestException {
        if ((!responseType.isAssignableFrom(FoxHttpResponse.class)
             && !responseType.isAssignableFrom(String.class)
             && !responseType.isAssignableFrom(void.class)
             && !responseType.isAssignableFrom(FoxHttpRequest.class)) && foxHttpClient.getFoxHttpResponseParser() == null) {
            throwFoxHttpRequestException("The used return type needs a FoxHttpResponseParser to deserialize the body");
        } else {
            this.responseType = responseType;
        }
    }

    private void parsetMethodAnnotation(Annotation annotation) throws FoxHttpRequestException {
        if (annotation instanceof DELETE) {
            setRequestTypeAndUrl("DELETE", ((DELETE) annotation).value(), ((DELETE) annotation).completePath(), false);
        } else if (annotation instanceof GET) {
            setRequestTypeAndUrl("GET", ((GET) annotation).value(), ((GET) annotation).completePath(), false);
        } else if (annotation instanceof HEAD) {
            setRequestTypeAndUrl("HEAD", ((HEAD) annotation).value(), ((HEAD) annotation).completePath(), false);
        } else if (annotation instanceof POST) {
            setRequestTypeAndUrl("POST", ((POST) annotation).value(), ((POST) annotation).completePath(), true);
        } else if (annotation instanceof PUT) {
            setRequestTypeAndUrl("PUT", ((PUT) annotation).value(), ((PUT) annotation).completePath(), true);
        } else if (annotation instanceof OPTIONS) {
            setRequestTypeAndUrl("OPTIONS", ((OPTIONS) annotation).value(), ((OPTIONS) annotation).completePath(), false);
        } else if (annotation instanceof Header) {
            setHeader((Header) annotation);
        }

    }

    private void setHeader(Header annotation) throws FoxHttpRequestException {
        if (annotation.name().isEmpty() || annotation.value().isEmpty()) {
            throwFoxHttpRequestException("@Header annotation is empty.");
        }
        headerFields.addHeader(annotation.name(), annotation.value());
    }

    private void setRequestTypeAndUrl(String requestType, String value, boolean completePath, boolean hasBody) throws FoxHttpRequestException {
        if (this.requestType != null) {
            throwFoxHttpRequestException("Only one HTTP method is allowed. Found: " + this.requestType + " and " + requestType + ".");
        }

        if (!hasBody && hasBodyAnnotation()) {
            throwFoxHttpRequestException("Non-body HTTP method can not contain @Body, @Field, @FieldMap, @Part or @PartMap.");
        }

        //Parameters
        doParameterMatch(HeaderField.class, new Class[]{String.class, Enum.class, int.class, Integer.class, long.class, Long.class}, this.method);
        doParameterMatch(Query.class, new Class[]{String.class, Enum.class, int.class, Integer.class, long.class, Long.class}, this.method);
        doParameterMatch(QueryMap.class, new Class[]{Map.class}, this.method);
        doParameterMatch(QueryObject.class, new Class[]{Object.class}, this.method);
        doParameterMatch(Path.class, new Class[]{String.class, Enum.class, int.class, Integer.class, long.class, Long.class}, this.method);

        //Body
        if (hasBody) {

            int bodyAnnotationCount = getParameterAnnotation(Body.class, this.method);
            if (bodyAnnotationCount > 1) {
                throwFoxHttpRequestException("Only one @Body is allowed.");
            }

            if (bodyAnnotationCount == 1) {
                Class<?> parameterClass = getParameterAnnotationTpe(Body.class, this.method);
                if (!(FoxHttpRequestBody.class.isAssignableFrom(parameterClass) || String.class.isAssignableFrom(parameterClass) || Serializable.class.isAssignableFrom(
                    parameterClass))) {
                    throwFoxHttpRequestException("FoxHttpRequestBody, String, Serializable is not assignable from Parameter "
                                                 + parameterClass
                                                 + " ("
                                                 + parameterClass.getSimpleName()
                                                 + ") with annotation @Body");
                }
            }
        }

        //FormUrlEncodedBody
        if (hasMethodAnnotation(FormUrlEncodedBody.class, this.method)) {
            if (!(hasParameterAnnotation(Field.class, this.method) || hasParameterAnnotation(FieldMap.class, this.method))) {
                throwFoxHttpRequestException("Form-encoded method must contain at least one @Field or @FieldMap.");
            }
            if (hasParameterAnnotation(Part.class, this.method) || hasParameterAnnotation(PartMap.class, this.method)) {
                throwFoxHttpRequestException("Form-encoded method can not contain @Part or @PartMap.");
            }
            if (hasParameterAnnotation(Body.class, this.method)) {
                throwFoxHttpRequestException("Form-encoded method can not contain @Body.");
            }
            doParameterMatch(Field.class, new Class[]{String.class, Enum.class, int.class, Integer.class, long.class, Long.class}, this.method);
            doParameterMatch(FieldMap.class, new Class[]{Map.class}, this.method);
        }

        //Multipart
        if (hasMethodAnnotation(MultipartBody.class, this.method)) {
            if (!(hasParameterAnnotation(Part.class, this.method) || hasParameterAnnotation(PartMap.class, this.method))) {
                throwFoxHttpRequestException("Multipart method must contain at least one @Part or @PartMap.");
            }
            if (hasParameterAnnotation(Field.class, this.method) || hasParameterAnnotation(FieldMap.class, this.method)) {
                throwFoxHttpRequestException("Multipart method can not contain @Field or @FieldMap.");
            }
            if (hasParameterAnnotation(Body.class, this.method)) {
                throwFoxHttpRequestException("Multipart method can not contain @Body.");
            }
            doParameterMatch(PartMap.class, new Class[]{Map.class}, this.method);
        }

        this.requestType = RequestType.valueOf(requestType);
        this.hasBody = hasBody;
        this.path = value;
        this.completePath = completePath;
    }


    private boolean hasBodyAnnotation() {
        return hasParameterAnnotation(Body.class, this.method)
               || hasParameterAnnotation(Field.class, this.method)
               || hasParameterAnnotation(FieldMap.class, this.method)
               || hasParameterAnnotation(Part.class, this.method)
               || hasParameterAnnotation(PartMap.class, this.method);
    }


    private void doParameterMatch(Class<? extends Annotation> annotationClass, Class<?>[] allowedClasses, Method method) throws FoxHttpRequestException {
        int parameterPos = 0;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == annotationClass) {
                    boolean foundMatchingType = false;
                    Class<?> checkedClass = Object.class;
                    for (Class<?> aClass : allowedClasses) {
                        checkedClass = aClass;
                        if (aClass.isAssignableFrom(method.getParameterTypes()[parameterPos])) {
                            foundMatchingType = true;
                            break;
                        }
                    }
                    if (!foundMatchingType) {
                        throwFoxHttpRequestException(checkedClass.getSimpleName()
                                                     + " is not assignable from Parameter "
                                                     + method.getParameterTypes()[parameterPos]
                                                     + " ("
                                                     + method.getParameterTypes()[parameterPos].getSimpleName()
                                                     + ") with annotation @"
                                                     + annotationClass.getSimpleName());

                    }
                }
            }
            parameterPos++;
        }
    }

    private void throwFoxHttpRequestException(String message) throws FoxHttpRequestException {

        String outputMessage = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "\n-> " + message;

        throw new FoxHttpRequestException(outputMessage);
    }

}
