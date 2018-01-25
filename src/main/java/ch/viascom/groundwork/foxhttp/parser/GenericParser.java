package ch.viascom.groundwork.foxhttp.parser;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;

public class GenericParser implements FoxHttpParser {

    HashMap<String, FoxHttpParser> foxHttpParsers = new HashMap<>();

    @Getter
    private ContentType parserOutputContentType = ContentType.WILDCARD;
    @Getter
    private ContentType parserInputContentType = ContentType.WILDCARD;

    public GenericParser() {
        foxHttpParsers.put(ContentType.APPLICATION_JSON.getMimeType(), new GsonParser());
        foxHttpParsers.put(ContentType.APPLICATION_XML.getMimeType(), new XStreamParser());
    }

    @Override
    public Serializable serializedToObject(String input, Class<Serializable> type, ContentType contentType) throws FoxHttpException {
        if (foxHttpParsers.containsKey(contentType.getMimeType())) {
            parserInputContentType = contentType;
            return foxHttpParsers.get(contentType.getMimeType()).serializedToObject(input, type, contentType);
        } else {
            throw new FoxHttpRequestException("No matching response parser for content type '" + contentType.toString() + "' found");
        }
    }

    @Override
    public String objectToSerialized(Serializable o, ContentType contentType) throws FoxHttpException {
        if (foxHttpParsers.containsKey(contentType.getMimeType())) {
            parserOutputContentType = contentType;
            return foxHttpParsers.get(contentType.getMimeType()).objectToSerialized(o, contentType);
        } else {
            throw new FoxHttpRequestException("No matching request parser for content type '" + contentType.toString() + "' found");
        }
    }
}
