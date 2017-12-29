package ch.viascom.groundwork.foxhttp.log;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default logger for FoxHttp
 *
 * @author patrick.boesch@viascom.ch
 */
public class DefaultFoxHttpLogger implements FoxHttpLogger {

    private Logger logger = Logger.getLogger(FoxHttpClient.class.getCanonicalName());
    private boolean enabled = false;
    private FoxHttpLoggerLevel foxHttpLoggerLevel = FoxHttpLoggerLevel.INFO;

    public DefaultFoxHttpLogger() {
    }

    public DefaultFoxHttpLogger(boolean enabled) {
        setLoggingEnabled(enabled);
    }

    public DefaultFoxHttpLogger(boolean enabled, FoxHttpLoggerLevel foxHttpLoggerLevel) {
        this.enabled = enabled;
        this.foxHttpLoggerLevel = foxHttpLoggerLevel;
    }

    @Override
    public void setLoggingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isLoggingEnabled() {
        return this.enabled;
    }

    @Override
    public void setName(String name) {
        logger = Logger.getLogger(name);
    }

    @Override
    public String getName() {
        return this.logger.getName();
    }

    @Override
    public void setLogLevel(FoxHttpLoggerLevel logLevel) {
        this.foxHttpLoggerLevel = logLevel;
    }

    @Override
    public FoxHttpLoggerLevel getLogLevel() {
        return this.foxHttpLoggerLevel;
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
        if (overrideEnabled) {

            if (foxHttpLoggerLevel.equals(logLevel) || FoxHttpLoggerLevel.DEBUG.equals(foxHttpLoggerLevel)) {
                switch (foxHttpLoggerLevel) {
                    case INFO:
                        logger.log(Level.INFO, message);
                        break;
                    case DEBUG:
                        logger.log(Level.FINE, message);
                        break;
                }
            }
        }
    }
}
