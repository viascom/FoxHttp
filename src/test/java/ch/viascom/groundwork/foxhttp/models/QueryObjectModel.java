package ch.viascom.groundwork.foxhttp.models;

import ch.viascom.groundwork.foxhttp.annotation.types.QueryName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryObjectModel {

    @QueryName("user-id")
    private String userId;
    @QueryName(allowOptional = true)
    private String password;

}
