package ch.viascom.groundwork.foxhttp;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author nikola.stankovic@viascom.ch
 */
public class FoxHttpClientTest {

    private FoxHttpClient client = new FoxHttpClient();

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
