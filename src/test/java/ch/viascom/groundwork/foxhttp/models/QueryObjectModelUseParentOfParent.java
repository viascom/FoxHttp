package ch.viascom.groundwork.foxhttp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryObjectModelUseParentOfParent extends QueryObjectModelUseParent {

    private String firstName;
    private String lastName;
}
