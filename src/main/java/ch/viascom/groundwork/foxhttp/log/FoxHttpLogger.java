package ch.viascom.groundwork.foxhttp.log;

/**
 * FoxHttpLogger interface
 *
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpLogger {
    void setLoggingEnabled(boolean enabled);

    boolean isLoggingEnabled();

    void setName(String name);

    String getName();

    void setLogLevel(FoxHttpLoggerLevel logLevel);

    FoxHttpLoggerLevel getLogLevel();

    void log(String message);

    void log(FoxHttpLoggerLevel logLevel, String message);

    void log(FoxHttpLoggerLevel logLevel, String message, boolean overrideEnabled);
}
