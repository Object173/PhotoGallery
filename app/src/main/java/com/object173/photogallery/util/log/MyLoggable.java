package com.object173.photogallery.util.log;

public interface MyLoggable {
    void info(String message, Object object);
    void info(String message);
    void warning(String message, Object object);
    void warning(String message);
    void error(String message, Throwable throwable);
    void error(String message);
    void verbose(String message, Object object);
    void verbose(String message);
    void debug(String message, Object object);
    void debug(String message);
}
