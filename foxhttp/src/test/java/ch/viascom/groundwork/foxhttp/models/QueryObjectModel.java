package ch.viascom.groundwork.foxhttp.models;

import ch.viascom.groundwork.foxhttp.annotation.types.QueryName;
import lombok.Data;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class QueryObjectModel {

    @QueryName("user-id")
    private String userId;
    private String password;

}
