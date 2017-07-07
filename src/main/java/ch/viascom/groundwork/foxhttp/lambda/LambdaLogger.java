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
    public void setLoggingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void setName(String name) {
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
        if (enabled) {
            logMessage.accept(message, foxHttpLoggerLevel);
        }
    }

    public interface LoggerMethod extends BiConsumer<String, FoxHttpLoggerLevel> {
    }
}
