package ch.viascom.groundwork.foxhttp.builder;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.header.FoxHttpHeader;
import ch.viascom.groundwork.foxhttp.type.RequestType;
import ch.viascom.groundwork.foxhttp.util.DeepCopy;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CachableRequestBuilder {

    private final String url;
    private final FoxHttpHeader requestHeader;
    private final RequestType requestType;
    private final boolean skipResponseBody;
    private final boolean followRedirect;
    private final FoxHttpClient foxHttpClient;

    public FoxHttpRequestBuilder buildFoxHttpRequestBuilder() throws FoxHttpRequestException {

        FoxHttpRequestBuilder requestBuilder = new FoxHttpRequestBuilder(DeepCopy.copy(url), DeepCopy.copy(requestType), foxHttpClient);
        requestBuilder.setRequestHeader(DeepCopy.copy(requestHeader));
        requestBuilder.setSkipResponseBody(DeepCopy.copy(skipResponseBody));
        requestBuilder.setFollowRedirect(DeepCopy.copy(followRedirect));

        return requestBuilder;
    }

}
