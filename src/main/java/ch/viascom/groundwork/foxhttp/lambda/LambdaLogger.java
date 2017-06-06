package ch.viascom.groundwork.foxhttp.lambda;

import ch.viascom.groundwork.foxhttp.log.FoxHttpLogger;

import java.util.function.BiConsumer;

/**
 * @author patrick.boesch@viascom.ch
 */
public class LambdaLogger implements FoxHttpLogger {

    private boolean enabled;
    private LoggerMethod logMessage;
    public LambdaLogger(LoggerMethod logMessage) {
        this.logMessage = logMessage;
    }

    @Override
    public void setLoggingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public void log(String message) {
        logMessage.accept(message, enabled);
    }

    public interface LoggerMethod extends BiConsumer<String, Boolean> {
    }
}
