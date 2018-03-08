package ch.viascom.groundwork.foxhttp.parser;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import com.google.gson.Gson;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author patrick.boesch@viascom.ch
 */
@AllArgsConstructor
@NoArgsConstructor
public class GsonParser implements FoxHttpParser {

    @Getter
    private ContentType parserOutputContentType = ContentType.APPLICATION_JSON;
    @Getter
    private ContentType parserInputContentType = ContentType.APPLICATION_JSON;

    private Gson gson = new Gson();

    public GsonParser(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Serializable serializedToObject(String json, Class<Serializable> type, ContentType contentType) throws FoxHttpException {
        return gson.fromJson(json, type);
    }

    @Override
    public String objectToSerialized(Serializable o, ContentType contentType) throws FoxHttpException {
        return gson.toJson(o);
    }
}
