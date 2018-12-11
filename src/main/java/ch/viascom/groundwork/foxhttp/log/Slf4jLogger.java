package ch.viascom.groundwork.foxhttp.log;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logger for the Slf4J Api
 *
 * FoxHttpLogger levels are mapped to the corresponding SLF4J levels (INFO, DEBUG), defaults to INFO if no level is specified.
 * Setting log-levels or disabling logging with the FoxHttpLogger-interface has no effect
 *
 */
public final class Slf4jLogger implements FoxHttpLogger {

    private static final FoxHttpLogger anonymousLogger = new Slf4jLogger(LoggerFactory.getLogger(FoxHttpClient.class));
    private final Logger logger;

    private Slf4jLogger(Logger logger) {
        this.logger = logger;
    }

    public static FoxHttpLogger create(Class clazz) {
        return create(LoggerFactory.getLogger(clazz));
    }

    public static FoxHttpLogger create(Logger logger) {
        return new Slf4jLogger(logger);
    }

    public static FoxHttpLogger anonymous() {
        return anonymousLogger;
    }

    @Override
    public boolean isLoggingEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void setLoggingEnabled(boolean enabled) { // unsupported
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public void setName(String name) { // unsupported
    }

    @Override
    public FoxHttpLoggerLevel getLogLevel() {
        return FoxHttpLoggerLevel.DEBUG;
    }

    @Override
    public void setLogLevel(FoxHttpLoggerLevel logLevel) { // unsupported
    }

    @Override
    public void log(String message) {
        logger.info(message);
    }

    @Override
    public void log(FoxHttpLoggerLevel logLevel, String message) {
        switch (logLevel) {
            case INFO:
                logger.info(message);
                break;
            case DEBUG:
                logger.debug(message);
                break;
        }
    }

    @Override
    public void log(FoxHttpLoggerLevel logLevel, String message, boolean overrideEnabled) {
        log(logLevel, message);
    }

}
