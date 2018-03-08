package ch.viascom.groundwork.foxhttp.annotation.processor;

import static ch.viascom.groundwork.foxhttp.annotation.processor.FoxHttpAnnotationUtil.getParameterAnnotationTpe;

import ch.viascom.groundwork.foxhttp.annotation.types.Body;
import ch.viascom.groundwork.foxhttp.annotation.types.Field;
import ch.viascom.groundwork.foxhttp.annotation.types.FieldMap;
import ch.viascom.groundwork.foxhttp.annotation.types.FormUrlEncodedBody;
import ch.viascom.groundwork.foxhttp.annotation.types.HeaderField;
import ch.viascom.groundwork.foxhttp.annotation.types.HeaderFieldMap;
import ch.viascom.groundwork.foxhttp.annotation.types.MultipartBody;
import ch.viascom.groundwork.foxhttp.annotation.types.Part;
import ch.viascom.groundwork.foxhttp.annotation.types.PartMap;
import ch.viascom.groundwork.foxhttp.annotation.types.Path;
import ch.viascom.groundwork.foxhttp.annotation.types.Query;
import ch.viascom.groundwork.foxhttp.annotation.types.QueryMap;
import ch.viascom.groundwork.foxhttp.annotation.types.QueryObject;
import ch.viascom.groundwork.foxhttp.annotation.types.SerializeContentType;
import ch.viascom.groundwork.foxhttp.body.request.FoxHttpRequestBody;
import ch.viascom.groundwork.foxhttp.body.request.RequestMultipartBody;
import ch.viascom.groundwork.foxhttp.body.request.RequestObjectBody;
import ch.viascom.groundwork.foxhttp.body.request.RequestStringBody;
import ch.viascom.groundwork.foxhttp.body.request.RequestUrlEncodedFormBody;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.header.FoxHttpHeader;
import ch.viascom.groundwork.foxhttp.header.HeaderEntry;
import ch.viascom.groundwork.foxhttp.parser.FoxHttpParser;
import ch.viascom.groundwork.foxhttp.query.FoxHttpRequestQuery;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import ch.viascom.groundwork.foxhttp.util.NamedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author patrick.boesch@viascom.ch
 */
class FoxHttpAnnotationRequestBuilder {

    static Map<String, String> getPathValues(Method method, Object[] args) {
        Map<String, String> pathValues = new HashMap<>();

        int parameterPos = 0;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Path) {
                    pathValues.put(((Path) annotation).value(), (args[parameterPos] != null ? args[parameterPos].toString() : null));
                }
            }
            parameterPos++;
        }

        return pathValues;
    }

    @SuppressWarnings("unchecked")
    static FoxHttpRequestQuery getFoxHttpRequestQuery(Method method, Object[] args) throws FoxHttpRequestException {
        FoxHttpRequestQuery foxHttpRequestQuery = new FoxHttpRequestQuery();

        int parameterPos = 0;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Query) {
                    if (!((Query) annotation).allowOptional() && args[parameterPos] == null) {
                        throw new FoxHttpRequestException("The query parameter " + ((Query) annotation).value() + " is not optional and can't be null because of this.");
                    }
                    if (!(((Query) annotation).allowOptional() && args[parameterPos] == null)) {
                        foxHttpRequestQuery.addQueryEntry(((Query) annotation).value(), args[parameterPos].toString());
                    }
                } else if (annotation instanceof QueryMap) {
                    foxHttpRequestQuery.addQueryMap((HashMap<String, String>) args[parameterPos]);
                } else if (annotation instanceof QueryObject) {
                    foxHttpRequestQuery.parseObjectAsQueryMap(Arrays.asList(((QueryObject) annotation).value()), args[parameterPos],
                        ((QueryObject) annotation).parseSerializedName(), ((QueryObject) annotation).allowOptional(), ((QueryObject) annotation).recursiveOptional());
                }
            }
            parameterPos++;
        }

        return foxHttpRequestQuery;
    }

    @SuppressWarnings("unchecked")
    static FoxHttpHeader setFoxHttpRequestHeader(FoxHttpHeader foxHttpRequestHeader, Method method, Object[] args) throws FoxHttpRequestException {

        int parameterPos = 0;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof HeaderField) {
                    if (!((HeaderField) annotation).allowOptional() && args[parameterPos] == null) {
                        throw new FoxHttpRequestException("The header field " + ((HeaderField) annotation).value() + " is not optional and can't be null because of this.");
                    }
                    if (!(((HeaderField) annotation).allowOptional() && args[parameterPos] == null)) {
                        foxHttpRequestHeader.addHeader(((HeaderField) annotation).value(), args[parameterPos].toString());
                    }
                } else if (annotation instanceof HeaderFieldMap) {
                    if (args[parameterPos] instanceof Map) {
                        foxHttpRequestHeader.addHeader((Map<String, String>) args[parameterPos]);
                    } else if (args[parameterPos] instanceof List) {
                        foxHttpRequestHeader.addHeader((ArrayList<HeaderEntry>) args[parameterPos]);
                    } else {
                        throw new FoxHttpRequestException("@HeaderFieldMap annotation does not support " + args[parameterPos].getClass());
                    }

                }
            }
            parameterPos++;
        }

        return foxHttpRequestHeader;
    }

    static FoxHttpRequestBody getFoxHttpRequestBody(Method method, Object[] args, FoxHttpParser parser) throws FileNotFoundException, FoxHttpRequestException {
        FoxHttpRequestBody foxHttpRequestBody = null;

        if (FoxHttpAnnotationUtil.hasMethodAnnotation(MultipartBody.class, method)) {
            //@MultipartBody
            foxHttpRequestBody = getRequestMultipartBody(method, args);
        } else if (FoxHttpAnnotationUtil.hasMethodAnnotation(FormUrlEncodedBody.class, method)) {
            //@FormUrlEncodedBody
            foxHttpRequestBody = getRequestUrlEncodedFormBody(method, args);
        } else if (FoxHttpAnnotationUtil.hasParameterAnnotation(Body.class, method)) {
            //@Body

            Class<?> bodyClass = getParameterAnnotationTpe(Body.class, method);
            Object bodyObject = getRequestBody(method, args);
            if (FoxHttpRequestBody.class.isAssignableFrom(bodyClass)) {
                foxHttpRequestBody = (FoxHttpRequestBody) bodyObject;
            } else if (String.class.isAssignableFrom(bodyClass)) {
                foxHttpRequestBody = new RequestStringBody((String) bodyObject);
            } else if (Serializable.class.isAssignableFrom(bodyClass)) {
                foxHttpRequestBody = new RequestObjectBody((Serializable) bodyObject, getRequestBodyContentType(method, bodyObject, parser));
            }
        }

        return foxHttpRequestBody;
    }

    private static Object getRequestBody(Method method, Object[] args) {
        int parameterPos = 0;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Body) {
                    return args[parameterPos];
                }
            }
            parameterPos++;
        }

        return null;
    }

    private static ContentType getRequestBodyContentType(Method method, Object bodyObject, FoxHttpParser parser) {
        Charset charset = parser.getParserOutputContentType().getCharset();
        String mimeType = parser.getParserOutputContentType().getMimeType();
        SerializeContentType serializeContentType = null;

        //Check Interface
        if (FoxHttpAnnotationUtil.hasTypeAnnotation(SerializeContentType.class, method)) {
            serializeContentType = method.getDeclaringClass().getAnnotation(SerializeContentType.class);
        }

        //Check Method
        if (FoxHttpAnnotationUtil.hasMethodAnnotation(SerializeContentType.class, method)) {
            serializeContentType = method.getAnnotation(SerializeContentType.class);
        }

        //Check Model
        if (bodyObject.getClass().isAnnotationPresent(SerializeContentType.class)) {
            serializeContentType = bodyObject.getClass().getAnnotation(SerializeContentType.class);
        }

        if (serializeContentType != null) {
            charset = Charset.forName(serializeContentType.charset());
            mimeType = serializeContentType.mimeType();
        }

        return ContentType.create(mimeType, charset);
    }

    @SuppressWarnings("unchecked")
    private static RequestMultipartBody getRequestMultipartBody(Method method, Object[] args) throws FileNotFoundException, FoxHttpRequestException {
        MultipartBody multipartBody = method.getAnnotation(MultipartBody.class);

        RequestMultipartBody requestMultipartBody = new RequestMultipartBody(Charset.forName(multipartBody.charset()), multipartBody.linefeed());

        int parameterPos = 0;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Part) {
                    Part part = (Part) annotation;

                    if (args[parameterPos] instanceof File) {
                        requestMultipartBody.addFilePart(part.value(), (File) args[parameterPos]);
                    } else if (args[parameterPos] instanceof String) {
                        requestMultipartBody.addFormField(part.value(), (args[parameterPos] != null ? args[parameterPos].toString() : null));
                    } else if (args[parameterPos] instanceof NamedInputStream) {
                        NamedInputStream namedInputStream = (NamedInputStream) args[parameterPos];
                        requestMultipartBody.addInputStreamPart(part.value(), namedInputStream.getName(), namedInputStream.getInputStream(),
                            namedInputStream.getContentTransferEncoding(), namedInputStream.getType());
                    } else {
                        throw new FoxHttpRequestException("@Part annotation does not support " + args[parameterPos].getClass());
                    }
                } else if (annotation instanceof PartMap) {
                    PartMap partMap = (PartMap) annotation;
                    if (partMap.isStreamMap()) {
                        requestMultipartBody.getStream().putAll((HashMap<String, NamedInputStream>) args[parameterPos]);
                    } else {
                        requestMultipartBody.getForms().putAll((HashMap<String, String>) args[parameterPos]);
                    }
                }
            }
            parameterPos++;
        }

        return requestMultipartBody;
    }

    @SuppressWarnings("unchecked")
    private static RequestUrlEncodedFormBody getRequestUrlEncodedFormBody(Method method, Object[] args) throws FoxHttpRequestException {
        RequestUrlEncodedFormBody requestUrlEncodedFormBody = new RequestUrlEncodedFormBody();

        int parameterPos = 0;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Field) {
                    Field field = (Field) annotation;
                    if (!field.allowOptional() && args[parameterPos] == null) {
                        throw new FoxHttpRequestException("The query parameter " + field.value() + " is not optional and can't be null because of this.");
                    }
                    if (!(((Field) annotation).allowOptional() && args[parameterPos] == null)) {
                        requestUrlEncodedFormBody.addFormEntry(field.value(), args[parameterPos].toString());
                    }
                } else if (annotation instanceof FieldMap) {
                    requestUrlEncodedFormBody.addFormMap((HashMap<String, String>) args[parameterPos]);
                }
            }
            parameterPos++;
        }

        return requestUrlEncodedFormBody;
    }
}
