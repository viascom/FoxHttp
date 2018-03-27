package ch.viascom.groundwork.foxhttp.log;

/**
 * FoxHttpLogger interface
 *
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpLogger {

    boolean isLoggingEnabled();

    void setLoggingEnabled(boolean enabled);

    String getName();

    void setName(String name);

    FoxHttpLoggerLevel getLogLevel();

    void setLogLevel(FoxHttpLoggerLevel logLevel);

    void log(String message);

    void log(FoxHttpLoggerLevel logLevel, String message);

    void log(FoxHttpLoggerLevel logLevel, String message, boolean overrideEnabled);
}
