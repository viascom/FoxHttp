package ch.viascom.groundwork.foxhttp.lambda;

import ch.viascom.groundwork.foxhttp.parser.FoxHttpParser;
import lombok.AllArgsConstructor;

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


    @Override
    public Serializable serializedToObject(String input, Class<Serializable> type) {
        return serializedToObjectMethod.apply(input, type);
    }

    @Override
    public String objectToSerialized(Serializable o) {
        return objectToSerializedMethod.apply(o);
    }


    public interface SerializedToObjectMethod extends BiFunction<String, Class<Serializable>, Serializable> {
    }

    public interface ObjectToSerializedMethod extends Function<Serializable, String> {
    }
}
