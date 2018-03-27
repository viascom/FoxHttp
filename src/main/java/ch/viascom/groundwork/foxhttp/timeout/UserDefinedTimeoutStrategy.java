package ch.viascom.groundwork.foxhttp.timeout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author patrick.boesch@viascom.ch
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserDefinedTimeoutStrategy implements FoxHttpTimeoutStrategy {

    private int connectionTimeout = 0;
    private int readTimeout = 0;
}
