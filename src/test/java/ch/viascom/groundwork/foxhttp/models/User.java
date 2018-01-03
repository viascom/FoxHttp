package ch.viascom.groundwork.foxhttp.models;

import ch.viascom.groundwork.foxhttp.annotation.types.SerializeContentType;
import lombok.Data;

import java.io.Serializable;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
@SerializeContentType(mimetype = "application/json", charset = "UTF-8")
public class User implements Serializable {
    private String username = "foxhttp@viascom.ch";
    private String firstname = "Fox";
    private String lastname = "Http";
}
