package ch.viascom.groundwork.foxhttp.builder;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import ch.viascom.groundwork.foxhttp.FoxHttpResponse;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorization;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;
import ch.viascom.groundwork.foxhttp.body.request.FoxHttpRequestBody;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.header.FoxHttpHeader;
import ch.viascom.groundwork.foxhttp.header.HeaderEntry;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptorType;
import ch.viascom.groundwork.foxhttp.log.FoxHttpLogger;
import ch.viascom.groundwork.foxhttp.placeholder.FoxHttpPlaceholderStrategy;
import ch.viascom.groundwork.foxhttp.query.FoxHttpRequestQuery;
import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import ch.viascom.groundwork.foxhttp.type.RequestType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * FoxHttpRequest builder to create a new FoxHttpRequest
 *
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpRequestBuilder {

    //private FoxHttpRequest foxHttpRequest;
    private String url;

    private FoxHttpRequestQuery requestQuery = new FoxHttpRequestQuery();
    private FoxHttpRequestBody requestBody;
    private FoxHttpHeader requestHeader = new FoxHttpHeader();
    private RequestType requestType = RequestType.GET;
    private boolean skipResponseBody = false;
    private boolean followRedirect = true;
    private FoxHttpClient foxHttpClient;

    private FoxHttpPlaceholderStrategy foxHttpPlaceholderStrategy;

    // -- Constructors

    /**
     * Create a new builder with a default request
     */
    public FoxHttpRequestBuilder() {
    }

    /**
     * Create a new builder with a default request and set the url
     *
     * @param url url of the request
     * @throws FoxHttpRequestException If the url is not well formed
     */
    public FoxHttpRequestBuilder(URL url) throws FoxHttpRequestException {
        this(url.toString());
    }

    /**
     * Create a new builder with a default request and set the url
     *
     * @param url url of the request
     * @throws FoxHttpRequestException If the url is not well formed
     */
    public FoxHttpRequestBuilder(String url) throws FoxHttpRequestException {
        this(url, RequestType.GET);
    }

    /**
     * Create a new builder with a default request and set the url and request type
     *
     * @param url url of the request
     * @param requestType request type
     */
    public FoxHttpRequestBuilder(URL url, RequestType requestType) throws FoxHttpRequestException {
        this(url.toString(), requestType);
    }

    /**
     * Create a new builder with a default request and set the url
     *
     * @param url url of the request
     * @param requestType request type
     * @throws FoxHttpRequestException If the url is not well formed
     */
    public FoxHttpRequestBuilder(String url, RequestType requestType) throws FoxHttpRequestException {
        this(url, requestType, new FoxHttpClient());
    }

    /**
     * Create a new builder with a default request and set the url, request type and FoxHttpClient
     *
     * @param url url of the request
     * @param requestType request type
     * @param foxHttpClient FoxHttpClient in which the request gets executed
     * @throws FoxHttpRequestException If the url is not well formed
     */
    public FoxHttpRequestBuilder(URL url, RequestType requestType, FoxHttpClient foxHttpClient) throws FoxHttpRequestException {
        this(url.toString(), requestType, foxHttpClient);
    }

    /**
     * Create a new builder with a default request and set the url, request type and FoxHttpClient
     *
     * @param url url of the request
     * @param requestType request type
     * @param foxHttpClient FoxHttpClient in which the request gets executed
     * @throws FoxHttpRequestException If the url is not well formed
     */
    public FoxHttpRequestBuilder(String url, RequestType requestType, FoxHttpClient foxHttpClient) throws FoxHttpRequestException {
        try {
            this.foxHttpClient = foxHttpClient;
            this.url = url;
            this.requestType = requestType;

            // Copy configuration from client to request
            this.foxHttpPlaceholderStrategy = this.foxHttpClient.getFoxHttpPlaceholderStrategy().getClass().newInstance();
            this.foxHttpPlaceholderStrategy.getPlaceholderMap().putAll(this.foxHttpClient.getFoxHttpPlaceholderStrategy().getPlaceholderMap());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new FoxHttpRequestException("Could not copy foxHttpPlaceholderStrategy from client to request: " + e.getMessage());
        }
    }

    // -- Setters

    /**
     * Set a FoxHttpClient
     *
     * @param foxHttpClient a FoxHttpClient
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder setFoxHttpClient(FoxHttpClient foxHttpClient) {
        this.foxHttpClient = foxHttpClient;
        return this;
    }

    /**
     * Set a request type
     *
     * @param requestType a request type
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder setRequestType(RequestType requestType) {
        this.requestType = requestType;
        return this;
    }

    /**
     * Set a FoxHttpRequestQuery
     *
     * @param foxHttpRequestQuery a FoxHttpRequestQuery
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder setRequestQuery(FoxHttpRequestQuery foxHttpRequestQuery) {
        this.requestQuery = foxHttpRequestQuery;
        return this;
    }

    /**
     * Add a new query entry
     *
     * @param name name of the query entry
     * @param value value of the query entry
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder addRequestQueryEntry(String name, String value) {
        this.requestQuery.addQueryEntry(name, value);
        return this;
    }

    /**
     * Set a body for this request <i>Do not set this if you have a request type other than POST or PUT</i>
     *
     * @param foxHttpRequestBody a body
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder setRequestBody(FoxHttpRequestBody foxHttpRequestBody) {
        this.requestBody = foxHttpRequestBody;
        return this;
    }

    /**
     * Set a header for this request
     *
     * @param foxHttpRequestHeader a header
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder setRequestHeader(FoxHttpHeader foxHttpRequestHeader) {
        this.requestHeader = foxHttpRequestHeader;
        return this;
    }

    /**
     * Add a new header entry
     *
     * @param headerField a header field
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder addRequestHeader(HeaderEntry headerField) {
        this.requestHeader.addHeader(headerField.getName(), headerField.getValue());
        return this;
    }

    /**
     * Add a new header entry
     *
     * @param name name of the header entry
     * @param value value of the header entry
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder addRequestHeader(String name, String value) {
        this.requestHeader.addHeader(name, value);
        return this;
    }

    /**
     * Add a new header entry
     *
     * @param name name of the header entry
     * @param value value of the header entry
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder addRequestHeader(HeaderTypes name, String value) {
        this.requestHeader.addHeader(name, value);
        return this;
    }

    /**
     * Sets if the response body should be skiped
     *
     * @param skipResponseBody should skip response body?
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder setSkipResponseBody(boolean skipResponseBody) {
        this.skipResponseBody = skipResponseBody;
        return this;
    }

    /**
     * Sets if the request should follow redirects
     *
     * @param followRedirect should follow redirects?
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder setFollowRedirect(boolean followRedirect) {
        this.followRedirect = followRedirect;
        return this;
    }

    /**
     * Register an interceptor
     *
     * @param interceptorType Type of the interceptor
     * @param foxHttpInterceptor Interceptor instance
     * @return FoxHttpClientBuilder (this)
     * @throws FoxHttpException Throws an exception if the interceptor does not match the type
     */
    public FoxHttpRequestBuilder addFoxHttpInterceptor(FoxHttpInterceptorType interceptorType, FoxHttpInterceptor foxHttpInterceptor) throws FoxHttpException {
        this.foxHttpClient.getFoxHttpInterceptorStrategy().addInterceptor(interceptorType, foxHttpInterceptor);
        return this;
    }

    /**
     * Add a FoxHttpPlaceholderEntry to the FoxHttpPlaceholderStrategy
     *
     * @param placeholder name of the placeholder (without escape char)
     * @param value value of the placeholder
     * @return FoxHttpClientBuilder (this)
     */
    public FoxHttpRequestBuilder addFoxHttpPlaceholderEntry(String placeholder, String value) {
        this.foxHttpPlaceholderStrategy.addPlaceholder(placeholder, value);
        return this;
    }

    /**
     * Add an Authorization to the AuthorizationStrategy
     *
     * @param foxHttpAuthorizationScope Scope of the authorization
     * @param foxHttpAuthorization Authorization itself
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder addFoxHttpAuthorization(FoxHttpAuthorizationScope foxHttpAuthorizationScope, FoxHttpAuthorization foxHttpAuthorization) {
        this.foxHttpClient.getFoxHttpAuthorizationStrategy().addAuthorization(foxHttpAuthorizationScope, foxHttpAuthorization);
        return this;
    }

    /**
     * Add an Authorization to the AuthorizationStrategy
     *
     * @param foxHttpAuthorizationScopes Scopes of the authorization
     * @param foxHttpAuthorization Authorization itself
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder addFoxHttpAuthorization(List<FoxHttpAuthorizationScope> foxHttpAuthorizationScopes, FoxHttpAuthorization foxHttpAuthorization) {
        this.foxHttpClient.getFoxHttpAuthorizationStrategy().addAuthorization(foxHttpAuthorizationScopes, foxHttpAuthorization);
        return this;
    }

    /**
     * Set a Logger
     *
     * @param foxHttpLogger a logger
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder setFoxHttpLogger(FoxHttpLogger foxHttpLogger) {
        this.foxHttpClient.setFoxHttpLogger(foxHttpLogger);
        return this;
    }

    /**
     * Set a Logger
     *
     * @param foxHttpLogger a logger
     * @param activate activate logger
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder setFoxHttpLogger(FoxHttpLogger foxHttpLogger, boolean activate) {
        this.foxHttpClient.setFoxHttpLogger(foxHttpLogger);
        activateFoxHttpLogger(activate);
        return this;
    }

    /**
     * Activate defined Logger
     *
     * @param activate activate logger
     * @return FoxHttpClientBuilder (this)
     */
    public FoxHttpRequestBuilder activateFoxHttpLogger(boolean activate) {
        this.foxHttpClient.getFoxHttpLogger().setLoggingEnabled(activate);
        return this;
    }

    /**
     * Get the FoxHttpRequest of this builder
     *
     * @return FoxHttpRequest
     */
    public FoxHttpRequest build() throws FoxHttpRequestException {
        FoxHttpRequest request = new FoxHttpRequest();
        request.setFoxHttpClient(this.foxHttpClient);

        if (this.foxHttpPlaceholderStrategy != null) {
            request.getFoxHttpPlaceholderStrategy().getPlaceholderMap().putAll(this.foxHttpPlaceholderStrategy.getPlaceholderMap());
        }

        if (this.url == null || "".equals(this.url)) {
            throw new FoxHttpRequestException("URL cant be null or empty.");
        }
        try {
            request.setUrl(this.url);
        } catch (MalformedURLException e) {
            throw new FoxHttpRequestException("URL is malformed: " + this.url);
        }
        request.setRequestType(this.requestType);
        request.setRequestHeader(this.requestHeader);
        request.setRequestQuery(this.requestQuery);
        request.setRequestBody(this.requestBody);
        request.setFollowRedirect(this.followRedirect);
        request.setSkipResponseBody(this.skipResponseBody);

        return request;
    }

    /**
     * Build and execute the FoxHttpRequest of this builder
     *
     * @return FoxHttpResponse response of the request
     */
    public FoxHttpResponse buildAndExecute() throws FoxHttpException {
        return build().execute();
    }

}
