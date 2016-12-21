package ch.viascom.groundwork.foxhttp.response;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import ch.viascom.groundwork.foxhttp.FoxHttpResponse;
import ch.viascom.groundwork.foxhttp.body.response.FoxHttpResponseBody;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpResponseException;
import ch.viascom.groundwork.foxhttp.header.FoxHttpHeader;
import ch.viascom.groundwork.serviceresult.ServiceResult;
import ch.viascom.groundwork.serviceresult.ServiceResultStatus;
import ch.viascom.groundwork.serviceresult.exception.ServiceFault;
import ch.viascom.groundwork.serviceresult.util.Metadata;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class FoxHttpServiceResultParser<T extends Serializable> {

    private ServiceResultStatus status;
    private String type;
    @Getter(AccessLevel.PRIVATE)
    private T content;
    private String hash;
    private String destination;
    private HashMap<String, Metadata> metadata = new HashMap<>();

    private FoxHttpResponseBody responseBody = new FoxHttpResponseBody();
    private int responseCode = -1;
    private FoxHttpHeader responseHeaders;
    private FoxHttpClient foxHttpClient;
    private FoxHttpRequest foxHttpRequest;

    private Gson parser = new Gson();
    private FoxHttpServiceResultHasher objectHasher;


    public FoxHttpServiceResultParser(FoxHttpResponse foxHttpResponse) throws IOException, FoxHttpException {
        this(foxHttpResponse, null);
    }

    public FoxHttpServiceResultParser(FoxHttpResponse foxHttpResponse, FoxHttpServiceResultHasher objectHasher) throws IOException, FoxHttpException {
        this.responseBody = foxHttpResponse.getResponseBody();
        this.foxHttpClient = foxHttpResponse.getFoxHttpClient();
        this.responseCode = foxHttpResponse.getResponseCode();
        this.foxHttpRequest = foxHttpResponse.getFoxHttpRequest();
        this.responseHeaders = foxHttpResponse.getResponseHeaders();
        this.objectHasher = objectHasher;
        foxHttpClient.getFoxHttpLogger().log("FoxHttpServiceResultParser(" + foxHttpResponse + "," + objectHasher + ")");
    }

    public InputStream getInputStreamBody() {
        return new ByteArrayInputStream(responseBody.getBody().toByteArray());
    }

    public ByteArrayOutputStream getByteArrayOutputStreamBody() {
        return responseBody.getBody();
    }

    protected void setBody(InputStream body) throws IOException {
        this.responseBody.setBody(body);
    }

    private String getStringBody() throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(getInputStreamBody()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\n');
        }
        rd.close();
        return response.toString();
    }

    public T getContent(Class<T> contentClass) throws FoxHttpResponseException {
        return getContent(contentClass, false);
    }

    public T getContent(Class<T> contentClass, boolean checkHash) throws FoxHttpResponseException {
        try {

            Type parameterizedType = new ServiceResultParameterizedType(contentClass);

            String body = getStringBody();

            ServiceResult<T> result = parser.fromJson(body, parameterizedType);
            foxHttpClient.getFoxHttpLogger().log("processServiceResult(" + result + ")");
            this.type = result.getType();
            this.hash = result.getHash();
            this.destination = result.getDestination();
            this.metadata = result.getMetadata();
            this.content = result.getContent();

            checkHash(checkHash, body, result);

            return this.content;
        } catch (IOException e) {
            throw new FoxHttpResponseException(e);
        }

    }

    private void checkHash(boolean checkHash, String body, ServiceResult<?> result) throws FoxHttpResponseException {
        if (checkHash && objectHasher != null) {
            foxHttpClient.getFoxHttpLogger().log("checkHash(" + result.getHash() + ")");
            if (!objectHasher.hash(result, body).equals(result.getHash())) {
                throw new FoxHttpResponseException("Hash not Equal!");
            }
            foxHttpClient.getFoxHttpLogger().log("-> successful");
        }
    }

    public ServiceFault getFault() throws FoxHttpResponseException {
        return getFault(false);
    }

    public ServiceFault getFault(boolean checkHash) throws FoxHttpResponseException {
        try {

            String body = getStringBody();

            ServiceResult<ServiceFault> result = parser.fromJson(body, new TypeToken<ServiceResult<ServiceFault>>() {
            }.getType());
            foxHttpClient.getFoxHttpLogger().log("processFault(" + result + ")");
            this.hash = result.getHash();
            this.destination = result.getDestination();
            this.metadata = result.getMetadata();

            checkHash(checkHash, body, result);

            return result.getContent();
        } catch (IOException e) {
            throw new FoxHttpResponseException(e);
        }

    }
}
