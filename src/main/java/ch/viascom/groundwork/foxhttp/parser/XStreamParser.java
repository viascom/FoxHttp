package ch.viascom.groundwork.foxhttp.parser;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import com.thoughtworks.xstream.XStream;
import java.io.Serializable;
import lombok.Getter;


/**
 * @author patrick.boesch@viascom.ch
 */
public class XStreamParser implements FoxHttpParser {

    @Getter
    private ContentType parserOutputContentType = ContentType.APPLICATION_XML;
    @Getter
    private ContentType parserInputContentType = ContentType.APPLICATION_XML;

    @Override
    public Serializable serializedToObject(String input, Class<Serializable> type, ContentType contentType) throws FoxHttpException {
        XStream xStream = new XStream();
        return (Serializable) xStream.fromXML(input);
    }

    @Override
    public String objectToSerialized(Serializable o, ContentType contentType) throws FoxHttpException {
        XStream xStream = new XStream();
        return xStream.toXML(o);
    }
}
