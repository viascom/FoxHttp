package ch.viascom.groundwork.foxhttp;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by stankovic on 23.05.2017.
 */
public class FoxHttpClientTest {

    FoxHttpClient client = new FoxHttpClient();

    @Test
    public void testSSLDebugLogIsActivated() {
        client.activateSSLDebugLog();
        assertThat(System.getProperty("javax.net.debug")).isEqualTo("all");
    }

    @Test
    public void testSSLDebugLogIsDisabled() {
        client.disableSSLDebugLog();
        assertThat(System.getProperty("javax.net.debug")).isNullOrEmpty();
    }

}
