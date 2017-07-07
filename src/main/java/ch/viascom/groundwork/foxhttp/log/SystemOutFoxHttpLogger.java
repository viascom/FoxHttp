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

    public SystemOutFoxHttpLogger(boolean enabled, String loggerName) {
        this.enabled = enabled;
        this.loggerName = loggerName;
    }

    @Override
    public void setLoggingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void setName(String name) {
        loggerName = name;
    }

    @Override
    public void setLogLevel(FoxHttpLoggerLevel logLevel) {
        this.foxHttpLoggerLevel = logLevel;
    }

    @Override
    public void log(String message) {
        log(message, loggerName);
    }

    @Override
    public void log(FoxHttpLoggerLevel logLevel, String message) {
        log(logLevel, message, loggerName);
    }

    public void log(String message, String name) {
        log(foxHttpLoggerLevel, message, name);
    }

    public void log(FoxHttpLoggerLevel logLevel, String message, String name) {
        if (enabled) {
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
