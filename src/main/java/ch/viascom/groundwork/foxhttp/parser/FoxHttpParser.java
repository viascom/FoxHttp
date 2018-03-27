package ch.viascom.groundwork.foxhttp.parser;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import java.io.Serializable;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpParser {

    ContentType getParserOutputContentType();

    ContentType getParserInputContentType();

    Serializable serializedToObject(String input, Class<Serializable> type, ContentType contentType) throws FoxHttpException;

    String objectToSerialized(Serializable o, ContentType contentType) throws FoxHttpException;
}
