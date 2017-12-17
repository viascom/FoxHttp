package ch.viascom.groundwork.foxhttp.log;

import lombok.AllArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * System.Out logger for FoxHttp
 *
 * @author patrick.boesch@viascom.ch
 */
@AllArgsConstructor
public class SystemOutFoxHttpLogger implements FoxHttpLogger {

    private boolean enabled = false;
    private String loggerName = "FoxHttp";
    private FoxHttpLoggerLevel foxHttpLoggerLevel = FoxHttpLoggerLevel.INFO;

    public SystemOutFoxHttpLogger() {
    }

    public SystemOutFoxHttpLogger(boolean enabled) {
        setLoggingEnabled(enabled);
    }

    public SystemOutFoxHttpLogger(boolean enabled, String loggerName) {
        setLoggingEnabled(enabled);
        setName(loggerName);
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
        loggerName = name;
    }

    @Override
    public String getName() {
        return this.loggerName;
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
        log(message, loggerName);
    }

    @Override
    public void log(FoxHttpLoggerLevel logLevel, String message) {
        log(logLevel, message, loggerName);
    }

    @Override
    public void log(FoxHttpLoggerLevel logLevel, String message, boolean overrideEnabled) {
        log(logLevel, message, loggerName, overrideEnabled);
    }

    public void log(String message, String name) {
        log(foxHttpLoggerLevel, message, name);
    }

    public void log(FoxHttpLoggerLevel logLevel, String message, String name) {
        log(logLevel, message, name, enabled);
    }

    public void log(FoxHttpLoggerLevel logLevel, String message, String name, boolean overrideEnabled) {
        if (overrideEnabled) {
            if (foxHttpLoggerLevel.equals(logLevel) || FoxHttpLoggerLevel.DEBUG.equals(foxHttpLoggerLevel)) {
                System.out.println("[" + getTime() + "] - " + logLevel + " - " + name + ": " + message);
            }
        }
    }

    private String getTime() {
        DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
        Date date = new Date();
        return df.format(date);
    }
}
