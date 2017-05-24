package ch.viascom.groundwork.foxhttp.parser;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author patrick.boesch@viascom.ch
 */
@AllArgsConstructor
@NoArgsConstructor
public class GsonParser implements FoxHttpParser {

    Gson gson = new Gson();

    @Override
    public Serializable serializedToObject(String json, Class<Serializable> type) {
        return gson.fromJson(json, type);
    }

    @Override
    public String objectToSerialized(Serializable o) {
        return gson.toJson(o);
    }
}
