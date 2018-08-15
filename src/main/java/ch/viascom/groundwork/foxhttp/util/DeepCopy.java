package ch.viascom.groundwork.foxhttp.util;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class DeepCopy {

    /**
     * Returns a copy of the object, or null if the object cannot
     * be serialized.
     */
    @SuppressWarnings("unchecked")
    public static  <T extends Serializable> T copy(final T orig) throws FoxHttpRequestException {
        try {
            // Write the object out to a byte array
            FastByteArrayOutputStream fbos =
                new FastByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(fbos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Retrieve an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in =
                new ObjectInputStream(fbos.getInputStream());

            return (T) in.readObject();
        }
        catch(IOException | ClassNotFoundException e) {
            throw new FoxHttpRequestException(e);
        }
    }

}
