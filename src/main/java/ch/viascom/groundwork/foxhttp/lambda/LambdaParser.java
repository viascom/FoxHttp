package ch.viascom.groundwork.foxhttp.lambda;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.parser.FoxHttpParser;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author patrick.boesch@viascom.ch
 */
@AllArgsConstructor
public class LambdaParser implements FoxHttpParser {

    private SerializedToObjectMethod serializedToObjectMethod;
    private ObjectToSerializedMethod objectToSerializedMethod;
    @Getter
    private ContentType parserOutputContentType;
    @Getter
    private ContentType parserInputContentType;


    @Override
    public Serializable serializedToObject(String input, Class<Serializable> type, ContentType contentType) throws FoxHttpException {
        return serializedToObjectMethod.apply(input, type);
    }

    @Override
    public String objectToSerialized(Serializable o, ContentType contentType) throws FoxHttpException {
        return objectToSerializedMethod.apply(o);
    }


    public interface SerializedToObjectMethod extends BiFunction<String, Class<Serializable>, Serializable> {
    }

    public interface ObjectToSerializedMethod extends Function<Serializable, String> {
    }
}
