package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.builder.FoxHttpRequestBuilder;
import ch.viascom.groundwork.foxhttp.models.User;
import ch.viascom.groundwork.foxhttp.parser.GsonParser;
import ch.viascom.groundwork.foxhttp.parser.XStreamParser;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import org.junit.Test;

import java.io.Serializable;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpParserTest {

    @Test
    @SuppressWarnings("unchecked")
    public void gsonTest() throws Exception {
        User user = new User();

        String json = new GsonParser().objectToSerialized(user, ContentType.APPLICATION_JSON);
        User deUser = (User) new GsonParser().serializedToObject(json, (Class<Serializable>) Class.forName("ch.viascom.groundwork.foxhttp.models.User"), ContentType.APPLICATION_JSON);

        assertThat(user).isEqualTo(deUser);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void xstreamTest() throws Exception {
        User user = new User();

        String json = new XStreamParser().objectToSerialized(user, ContentType.APPLICATION_JSON);
        User deUser = (User) new XStreamParser().serializedToObject(json, (Class<Serializable>) Class.forName("ch.viascom.groundwork.foxhttp.models.User"), ContentType.APPLICATION_JSON);

        assertThat(user).isEqualTo(deUser);
    }
}
