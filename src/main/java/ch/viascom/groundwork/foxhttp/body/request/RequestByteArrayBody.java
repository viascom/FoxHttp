package ch.viascom.groundwork.foxhttp.body.request;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * RequestByteArrayBody for FoxHttp <p> Stores an ByteArray for a request body.
 *
 * @author patrick.boesch@viascom.ch
 */
public class RequestByteArrayBody extends FoxHttpRequestBody {

    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private ContentType outputContentType;

    /**
     * Create a new RequestByteArrayBody
     *
     * @param outputStream content of the body as ByteArrayOutputStream
     * @param outputContentType defines the content of the ByteArrayOutputStream
     */
    public RequestByteArrayBody(ByteArrayOutputStream outputStream, ContentType outputContentType) {
        this.outputContentType = outputContentType;
        this.outputStream = outputStream;
    }

    /**
     * Create a new RequestByteArrayBody
     *
     * @param inputStream content of the body as InputStream
     * @param outputContentType defines the content of the InputStream
     */
    public RequestByteArrayBody(InputStream inputStream, ContentType outputContentType) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        this.outputContentType = outputContentType;
        this.outputStream = buffer;
    }

    /**
     * Set the body of the request
     *
     * @param context context of the request
     * @throws FoxHttpRequestException can throw different exception based on input streams and interceptors
     */
    @Override
    public void setBody(FoxHttpRequestBodyContext context) throws FoxHttpRequestException {
        try {
            context.getUrlConnection().getOutputStream().write(outputStream.toByteArray());
        } catch (IOException e) {
            throw new FoxHttpRequestException(e.getMessage());
        }
    }

    @Override
    public boolean hasBody() {
        return outputStream.size() > 0;
    }

    @Override
    public ContentType getOutputContentType() {
        return outputContentType;
    }
}
