package ch.viascom.groundwork.foxhttp.log;

/**
 * FoxHttpLogger interface
 *
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpLogger {
    void setLoggingEnabled(boolean enabled);
    void setName(String name);
    void setLogLevel(FoxHttpLoggerLevel logLevel);
    void log(String message);
    void log(FoxHttpLoggerLevel logLevel, String message);
}
