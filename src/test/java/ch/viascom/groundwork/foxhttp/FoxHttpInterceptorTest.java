package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.body.request.RequestStringBody;
import ch.viascom.groundwork.foxhttp.builder.FoxHttpClientBuilder;
import ch.viascom.groundwork.foxhttp.builder.FoxHttpRequestBuilder;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpResponseException;
import ch.viascom.groundwork.foxhttp.interceptor.DefaultInterceptorStrategy;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptorType;
import ch.viascom.groundwork.foxhttp.interceptor.response.HttpErrorResponseInterceptor;
import ch.viascom.groundwork.foxhttp.interceptors.*;
import ch.viascom.groundwork.foxhttp.lambda.LambdaLogger;
import ch.viascom.groundwork.foxhttp.lambda.interceptor.LambdaResponseInterceptor;
import ch.viascom.groundwork.foxhttp.models.GetResponse;
import ch.viascom.groundwork.foxhttp.models.PostResponse;
import ch.viascom.groundwork.foxhttp.parser.GsonParser;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import ch.viascom.groundwork.foxhttp.type.RequestType;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpInterceptorTest {

    private String endpoint = "http://httpbin.org/";

    @Test
    public void GZipTest() throws Exception {
        FoxHttpClientBuilder clientBuilder = new FoxHttpClientBuilder();
        clientBuilder.activateGZipResponseInterceptor();
        clientBuilder.setFoxHttpResponseParser(new GsonParser());

        FoxHttpRequestBuilder requestBuilder = new FoxHttpRequestBuilder(endpoint + "gzip", RequestType.GET, clientBuilder.build());
        FoxHttpRequest request = requestBuilder.build();

        GetResponse response = request.execute().getParsedBody(GetResponse.class);

        assertThat(response.getMethod()).isEqualTo("GET");
    }

    @Test
    public void DeflateTest() throws Exception {
        FoxHttpClientBuilder clientBuilder = new FoxHttpClientBuilder();
        clientBuilder.activateDeflateResponseInterceptor(false);
        clientBuilder.setFoxHttpResponseParser(new GsonParser());

        FoxHttpRequestBuilder requestBuilder = new FoxHttpRequestBuilder(endpoint + "deflate", RequestType.GET, clientBuilder.build());
        FoxHttpRequest request = requestBuilder.build();

        GetResponse response = request.execute().getParsedBody(GetResponse.class);

        assertThat(response.getMethod()).isEqualTo("GET");
    }

    @Test
    public void requestInterceptorTest() throws Exception {
        FoxHttpClientBuilder clientBuilder = new FoxHttpClientBuilder();
        clientBuilder.setFoxHttpResponseParser(new GsonParser());

        //Add query entry
        clientBuilder.addFoxHttpInterceptor(FoxHttpInterceptorType.REQUEST, new RequestInterceptor());

        FoxHttpRequestBuilder requestBuilder = new FoxHttpRequestBuilder(endpoint + "get", RequestType.GET, clientBuilder.build());
        FoxHttpRequest request = requestBuilder.build();

        GetResponse response = request.execute().getParsedBody(GetResponse.class);

        assertThat(response.getArgs().get("key")).isEqualTo("Fox");
    }

    @Test
    public void requestHeaderInterceptorTest() throws Exception {
        FoxHttpClientBuilder clientBuilder = new FoxHttpClientBuilder();
        clientBuilder.setFoxHttpResponseParser(new GsonParser());

        //Add header entry
        clientBuilder.addFoxHttpInterceptor(FoxHttpInterceptorType.REQUEST_HEADER, new RequestHeaderInterceptor());

        FoxHttpRequestBuilder requestBuilder = new FoxHttpRequestBuilder(endpoint + "get", RequestType.GET, clientBuilder.build());
        FoxHttpRequest request = requestBuilder.build();

        GetResponse response = request.execute().getParsedBody(GetResponse.class);

        assertThat(response.getHeaders().get("Header-Key")).isEqualTo("Fox");
    }

    @Test
    public void requestBodyInterceptorTest() throws Exception {
        FoxHttpClientBuilder clientBuilder = new FoxHttpClientBuilder();
        clientBuilder.setFoxHttpResponseParser(new GsonParser());
        clientBuilder.setFoxHttpLogger(new LambdaLogger((message, e) -> System.out.println(message)));

        //Add new string body
        clientBuilder.addFoxHttpInterceptor(FoxHttpInterceptorType.REQUEST_BODY, new RequestBodyInterceptor());

        FoxHttpRequestBuilder requestBuilder = new FoxHttpRequestBuilder(endpoint + "post", RequestType.POST, clientBuilder.build());
        requestBuilder.setRequestBody(new RequestStringBody("Dont send this!", ContentType.DEFAULT_TEXT));
        FoxHttpRequest request = requestBuilder.build();

        PostResponse response = request.execute().getParsedBody(PostResponse.class);

        assertThat(response.getData()).isEqualTo("New Body");
    }

    @Test
    public void responseCodeInterceptorTest() throws Exception {
        FoxHttpClientBuilder clientBuilder = new FoxHttpClientBuilder();
        clientBuilder.setFoxHttpResponseParser(new GsonParser());

        //Throw exception on 404
        clientBuilder.addFoxHttpInterceptor(FoxHttpInterceptorType.RESPONSE_CODE, new ResponseCodeInterceptor());

        FoxHttpRequestBuilder requestBuilder = new FoxHttpRequestBuilder(endpoint + "status/404", RequestType.GET, clientBuilder.build());
        FoxHttpRequest request = requestBuilder.build();

        try {
            request.execute().getParsedBody(GetResponse.class);
            assertThat(false).isEqualTo(true);
        } catch (FoxHttpRequestException e) {
            assertThat(e.getMessage()).isEqualTo("Found 404");
        }
    }

    @Test
    public void responseCodeDefaultInterceptorTest() throws Exception {
        FoxHttpClientBuilder clientBuilder = new FoxHttpClientBuilder();
        clientBuilder.setFoxHttpResponseParser(new GsonParser());

        //Throw exception on error response code
        clientBuilder.addFoxHttpInterceptor(FoxHttpInterceptorType.RESPONSE_CODE, new HttpErrorResponseInterceptor());

        FoxHttpRequestBuilder requestBuilder = new FoxHttpRequestBuilder(endpoint + "status/404", RequestType.GET, clientBuilder.build());
        FoxHttpRequest request = requestBuilder.build();

        try {
            request.execute().getParsedBody(GetResponse.class);
            assertThat(false).isEqualTo(true);
        } catch (FoxHttpResponseException e) {
            assertThat(e.getMessage()).isEqualTo("Response Error-Code: 404");
        }
    }

    @Test
    public void responseBodyDefaultInterceptorTest() throws Exception {
        FoxHttpClientBuilder clientBuilder = new FoxHttpClientBuilder();
        clientBuilder.setFoxHttpResponseParser(new GsonParser());

        //Throw exception on error response code
        clientBuilder.addFoxHttpInterceptor(FoxHttpInterceptorType.RESPONSE_BODY, new HttpErrorResponseInterceptor());

        FoxHttpRequestBuilder requestBuilder = new FoxHttpRequestBuilder(endpoint + "status/404", RequestType.GET, clientBuilder.build());
        FoxHttpRequest request = requestBuilder.build();

        try {
            request.execute().getParsedBody(GetResponse.class);
            assertThat(false).isEqualTo(true);
        } catch (FoxHttpResponseException e) {
            assertThat(e.getMessage()).isEqualTo("Response Error-Code: 404");
        }
    }

    @Test
    public void responseDefaultInterceptorTest() throws Exception {
        FoxHttpClientBuilder clientBuilder = new FoxHttpClientBuilder();
        clientBuilder.setFoxHttpResponseParser(new GsonParser());

        //Throw exception on error response code
        clientBuilder.addFoxHttpInterceptor(FoxHttpInterceptorType.RESPONSE, new HttpErrorResponseInterceptor());

        FoxHttpRequestBuilder requestBuilder = new FoxHttpRequestBuilder(endpoint + "status/404", RequestType.GET, clientBuilder.build());
        FoxHttpRequest request = requestBuilder.build();

        try {
            request.execute().getParsedBody(GetResponse.class);
            assertThat(false).isEqualTo(true);
        } catch (FoxHttpResponseException e) {
            assertThat(e.getMessage()).isEqualTo("Response Error-Code: 404");
        }
    }

    @Test
    public void responseInterceptorTest() throws Exception {
        FoxHttpClientBuilder clientBuilder = new FoxHttpClientBuilder();
        clientBuilder.setFoxHttpResponseParser(new GsonParser());

        //Set response code to 500
        clientBuilder.addFoxHttpInterceptor(FoxHttpInterceptorType.RESPONSE, new ResponseInterceptor());

        FoxHttpRequestBuilder requestBuilder = new FoxHttpRequestBuilder(endpoint + "status/404", RequestType.GET, clientBuilder.build());
        FoxHttpRequest request = requestBuilder.build();

        FoxHttpResponse response = request.execute();

        assertThat(response.getResponseCode()).isEqualTo(500);
    }

    @Test
    public void responseBodyInterceptorTest() throws Exception {
        FoxHttpClientBuilder clientBuilder = new FoxHttpClientBuilder();
        clientBuilder.setFoxHttpResponseParser(new GsonParser());

        //Set new response body
        clientBuilder.addFoxHttpInterceptor(FoxHttpInterceptorType.RESPONSE_BODY, new ResponseBodyInterceptor());

        FoxHttpRequestBuilder requestBuilder = new FoxHttpRequestBuilder(endpoint + "status/404", RequestType.GET, clientBuilder.build());
        FoxHttpRequest request = requestBuilder.build();

        FoxHttpResponse response = request.execute();

        assertThat(response.getStringBody()).isEqualTo("Hi!");
    }

    @Test
    public void requestInterceptorWeightTest() throws Exception {
        FoxHttpClientBuilder clientBuilder = new FoxHttpClientBuilder();
        clientBuilder.setFoxHttpResponseParser(new GsonParser());

        //Add query entry
        clientBuilder.addFoxHttpInterceptor("INTERCEPTOR-1", FoxHttpInterceptorType.REQUEST_HEADER, new RequestHeaderInterceptor(10));

        //Add header entry
        RequestHeaderInterceptor headerInterceptor = new RequestHeaderInterceptor(5);
        clientBuilder.addFoxHttpInterceptor("INTERCEPTOR-2", FoxHttpInterceptorType.REQUEST_HEADER, headerInterceptor);


        FoxHttpRequestBuilder requestBuilder = new FoxHttpRequestBuilder(endpoint + "get", RequestType.GET, clientBuilder.build());
        FoxHttpRequest request = requestBuilder.build();

        GetResponse response = request.execute().getParsedBody(GetResponse.class);

        assertThat(request.getFoxHttpClient().getFoxHttpInterceptorStrategy().getInterceptorByKey(FoxHttpInterceptorType.REQUEST_HEADER, "INTERCEPTOR-2").getWeight()).isEqualTo(5);
        assertThat(request.getFoxHttpClient().getFoxHttpInterceptorStrategy().getInterceptorByKey(FoxHttpInterceptorType.REQUEST_HEADER, "INTERCEPTOR-1").getWeight()).isEqualTo(10);
    }

    @Test
    public void interceptorStrategyTest() throws Exception {
        DefaultInterceptorStrategy strategy = new DefaultInterceptorStrategy();
        strategy.addInterceptor(FoxHttpInterceptorType.RESPONSE, new HttpErrorResponseInterceptor(), "response-interceptor-1");

        assertThat(strategy.doesTypeExist(FoxHttpInterceptorType.RESPONSE)).isTrue();
        assertThat(strategy.getAllInterceptorsFromType(FoxHttpInterceptorType.RESPONSE).containsKey("response-interceptor-1")).isTrue();

        strategy.removeInterceptorByKey(FoxHttpInterceptorType.RESPONSE, "response-interceptor-1");
        assertThat(strategy.doesTypeExist(FoxHttpInterceptorType.RESPONSE)).isTrue();
        assertThat(strategy.getAllInterceptorsFromType(FoxHttpInterceptorType.RESPONSE).containsKey("response-interceptor-1")).isFalse();


        strategy.addInterceptor(FoxHttpInterceptorType.RESPONSE, new HttpErrorResponseInterceptor(), "response-interceptor-1");
        strategy.replaceInterceptor(FoxHttpInterceptorType.RESPONSE, new LambdaResponseInterceptor(context -> System.out.println(context.getResponseCode()), 1), "response-interceptor-1");

        assertThat(strategy.getInterceptorsByClass(FoxHttpInterceptorType.RESPONSE,LambdaResponseInterceptor.class).size()).isEqualTo(1);

        strategy.addInterceptor(FoxHttpInterceptorType.RESPONSE, new HttpErrorResponseInterceptor(), "response-interceptor-2");

        assertThat(strategy.getAllInterceptorsFromType(FoxHttpInterceptorType.RESPONSE).size()).isEqualTo(2);

        strategy.removeInterceptorByKey(FoxHttpInterceptorType.RESPONSE,"response-interceptor-2");

        assertThat(strategy.getAllInterceptorsFromType(FoxHttpInterceptorType.RESPONSE).size()).isEqualTo(1);

        strategy.addInterceptor(FoxHttpInterceptorType.RESPONSE, new HttpErrorResponseInterceptor(), "response-interceptor-2");

        assertThat(strategy.getAllInterceptorsFromType(FoxHttpInterceptorType.RESPONSE).size()).isEqualTo(2);
        
        strategy.removeInterceptorByClass(FoxHttpInterceptorType.RESPONSE,HttpErrorResponseInterceptor.class);

        assertThat(strategy.getAllInterceptorsFromType(FoxHttpInterceptorType.RESPONSE).size()).isEqualTo(1);
        assertThat(strategy.getInterceptorsByClass(FoxHttpInterceptorType.RESPONSE,HttpErrorResponseInterceptor.class).size()).isEqualTo(0);
    }
}
