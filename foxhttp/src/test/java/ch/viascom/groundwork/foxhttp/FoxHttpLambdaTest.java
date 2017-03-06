package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.body.request.RequestObjectBody;
import ch.viascom.groundwork.foxhttp.builder.FoxHttpClientBuilder;
import ch.viascom.groundwork.foxhttp.builder.FoxHttpRequestBuilder;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestBodyInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestConnectionInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestHeaderInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseBodyInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseCodeInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseInterceptorContext;
import ch.viascom.groundwork.foxhttp.lambda.LambdaParser;
import ch.viascom.groundwork.foxhttp.lambda.interceptor.*;
import ch.viascom.groundwork.foxhttp.models.PostResponse;
import ch.viascom.groundwork.foxhttp.models.User;
import ch.viascom.groundwork.foxhttp.parser.GsonParser;
import ch.viascom.groundwork.foxhttp.type.RequestType;
import com.google.gson.Gson;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpLambdaTest {

    private String endpoint = "http://httpbin.org/";

    @Test
    public void lambdaParserTest() throws Exception {
        FoxHttpClient httpClient = new FoxHttpClientBuilder(
                new LambdaParser(
                        (s, serializableClass) -> new Gson().fromJson(s, serializableClass),
                        serializable -> new Gson().toJson(serializable)
                )
        ).build();

        PostResponse response = new FoxHttpRequestBuilder(endpoint + "post", RequestType.POST, httpClient)
                .setRequestBody(new RequestObjectBody(new User()))
                .build().execute().getParsedBody(PostResponse.class);

        assertThat(response.getUrl()).isEqualTo(endpoint + "post");
        assertThat(response.getData()).isEqualTo(new GsonParser().objectToSerialized(new User()));
    }

    @Test
    public void LambdaRequestBodyInterceptorTest() throws Exception {
        FoxHttpRequest request = new FoxHttpRequest();
        LambdaRequestBodyInterceptor interceptor = new LambdaRequestBodyInterceptor(
                context -> context.getRequest().setRequestType(RequestType.DELETE), 0
        );
        interceptor.onIntercept(new FoxHttpRequestBodyInterceptorContext(null, null, request, new FoxHttpClient()));
        assertThat(request.getRequestType()).isEqualTo(RequestType.DELETE);
    }

    @Test
    public void LambdaRequestConnectionInterceptorTest() throws Exception {
        FoxHttpRequest request = new FoxHttpRequest();
        LambdaRequestConnectionInterceptor interceptor = new LambdaRequestConnectionInterceptor(
                context -> context.getRequest().setRequestType(RequestType.DELETE), 0
        );
        interceptor.onIntercept(new FoxHttpRequestConnectionInterceptorContext(null, request, new FoxHttpClient()));
        assertThat(request.getRequestType()).isEqualTo(RequestType.DELETE);
    }

    @Test
    public void LambdaRequestHeaderInterceptorTest() throws Exception {
        FoxHttpRequest request = new FoxHttpRequest();
        LambdaRequestHeaderInterceptor interceptor = new LambdaRequestHeaderInterceptor(
                context -> context.getRequest().setRequestType(RequestType.DELETE), 0
        );
        interceptor.onIntercept(new FoxHttpRequestHeaderInterceptorContext(null, request, new FoxHttpClient()));
        assertThat(request.getRequestType()).isEqualTo(RequestType.DELETE);
    }

    @Test
    public void LambdaRequestInterceptorTest() throws Exception {
        FoxHttpRequest request = new FoxHttpRequest();
        LambdaRequestInterceptor interceptor = new LambdaRequestInterceptor(
                context -> context.getRequest().setRequestType(RequestType.DELETE), 0
        );
        interceptor.onIntercept(new FoxHttpRequestInterceptorContext(null, request, new FoxHttpClient()));
        assertThat(request.getRequestType()).isEqualTo(RequestType.DELETE);
    }

    @Test
    public void LambdaResponseBodyInterceptorTest() throws Exception {
        FoxHttpRequest request = new FoxHttpRequest();
        LambdaResponseBodyInterceptor interceptor = new LambdaResponseBodyInterceptor(
                context -> context.getRequest().setRequestType(RequestType.DELETE), 0
        );
        interceptor.onIntercept(new FoxHttpResponseBodyInterceptorContext(200, null, request, new FoxHttpClient()));
        assertThat(request.getRequestType()).isEqualTo(RequestType.DELETE);
    }

    @Test
    public void LambdaResponseCodeInterceptorTest() throws Exception {
        FoxHttpRequest request = new FoxHttpRequest();
        LambdaResponseCodeInterceptor interceptor = new LambdaResponseCodeInterceptor(
                context -> context.getRequest().setRequestType(RequestType.DELETE), 0
        );
        interceptor.onIntercept(new FoxHttpResponseCodeInterceptorContext(200, request, new FoxHttpClient()));
        assertThat(request.getRequestType()).isEqualTo(RequestType.DELETE);
    }

    @Test
    public void LambdaResponseInterceptorTest() throws Exception {
        FoxHttpRequest request = new FoxHttpRequest();
        LambdaResponseInterceptor interceptor = new LambdaResponseInterceptor(
                context -> context.getRequest().setRequestType(RequestType.DELETE), 0
        );
        interceptor.onIntercept(new FoxHttpResponseInterceptorContext(200, null, request, new FoxHttpClient()));
        assertThat(request.getRequestType()).isEqualTo(RequestType.DELETE);
    }

}
