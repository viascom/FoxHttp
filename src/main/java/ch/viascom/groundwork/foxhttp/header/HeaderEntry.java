package ch.viascom.groundwork.foxhttp.header;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author patrick.boesch@viascom.ch
 */
@Getter
@AllArgsConstructor
@ToString
public class HeaderEntry implements Serializable {

    private String name;
    private String value;
}
