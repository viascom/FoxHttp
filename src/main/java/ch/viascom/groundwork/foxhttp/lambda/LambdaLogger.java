package ch.viascom.groundwork.foxhttp.lambda;

import ch.viascom.groundwork.foxhttp.log.FoxHttpLogger;
import ch.viascom.groundwork.foxhttp.log.FoxHttpLoggerLevel;
import java.util.function.BiConsumer;

/**
 * @author patrick.boesch@viascom.ch
 */
public class LambdaLogger implements FoxHttpLogger {

    private boolean enabled;
    private LoggerMethod logMessage;
    private FoxHttpLoggerLevel foxHttpLoggerLevel = FoxHttpLoggerLevel.INFO;

    public LambdaLogger(LoggerMethod logMessage) {
        this.logMessage = logMessage;
    }

    @Override
    public boolean isLoggingEnabled() {
        return enabled;
    }

    @Override
    public void setLoggingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void setName(String name) {
    }

    @Override
    public FoxHttpLoggerLevel getLogLevel() {
        return foxHttpLoggerLevel;
    }

    @Override
    public void setLogLevel(FoxHttpLoggerLevel logLevel) {
        this.foxHttpLoggerLevel = logLevel;
    }

    @Override
    public void log(String message) {
        log(foxHttpLoggerLevel, message);
    }

    @Override
    public void log(FoxHttpLoggerLevel logLevel, String message) {
        log(logLevel, message, enabled);
    }

    @Override
    public void log(FoxHttpLoggerLevel logLevel, String message, boolean overrideEnabled) {
        if (enabled) {
            logMessage.accept(message, foxHttpLoggerLevel);
        }
    }

    public interface LoggerMethod extends BiConsumer<String, FoxHttpLoggerLevel> {

    }
}
