package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.builder.FoxHttpRequestBuilder;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpResponseException;
import ch.viascom.groundwork.foxhttp.models.GetResponse;
import ch.viascom.groundwork.foxhttp.type.RequestType;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.net.URL;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpResponseTest {

    private String endpoint = "http://httpbin.org/";

    @Test
    @Ignore
    public void responseParserException() throws Exception {

        FoxHttpClient foxHttpClient = new FoxHttpClient();

        FoxHttpRequest foxHttpRequest = new FoxHttpRequest(foxHttpClient);
        foxHttpRequest.setUrl(new URL(endpoint + "get"));
        foxHttpRequest.setRequestType(RequestType.GET);
        foxHttpRequest.setFollowRedirect(true);

        FoxHttpResponse foxHttpResponse = foxHttpRequest.execute();

        try {
            foxHttpResponse.getParsedBody(GetResponse.class);
            assertThat(false).isEqualTo(true);
        } catch (FoxHttpResponseException e) {
            assertThat(e.getMessage()).isEqualTo("getParsedBody needs a FoxHttpResponseParser to deserialize the body");
        }

    }

    @Test
    public void responseToString() throws Exception {

        FoxHttpClient foxHttpClient = new FoxHttpClient();

        FoxHttpRequest foxHttpRequest = new FoxHttpRequest(foxHttpClient);
        foxHttpRequest.setUrl(new URL(endpoint + "get"));
        foxHttpRequest.setRequestType(RequestType.GET);
        foxHttpRequest.setFollowRedirect(true);

        FoxHttpResponse foxHttpResponse = foxHttpRequest.execute();


        assertThat(foxHttpResponse.toString(true).length() > 0).isEqualTo(true);


    }

    /*
    @Test
    public void streamResponse() throws FoxHttpException, IOException {
        DataInputStream in = new DataInputStream(new FoxHttpRequestBuilder("http://httpbin.org/stream/200").buildAndExecute().getInputStreamBody());

        byte[] messageByte = new byte[1000];
        boolean end = false;
        String dataString = "";

        int bytesRead = 0;

        messageByte[0] = in.readByte();
        messageByte[1] = in.readByte();

        int bytesToRead = messageByte[1];

        while(!end)
        {
            bytesRead = in.read(messageByte);
            String messageString = new String(messageByte, 0, bytesRead);
            if (messageString.length() == bytesToRead )
            {
                end = true;
            }
            System.out.println("MESSAGE: " + messageString);
        }

    }
    */
}
