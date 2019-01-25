package com.object173.photogallery.util.log;

import com.google.android.gms.common.logging.Logger;
import com.object173.photogallery.BuildConfig;

public final class MyLoggerFactory {

    private static boolean isLogEnabled() {
        return BuildConfig.DEBUG;
    }

    public static MyLoggable get(final String TAG) {
        if(isLogEnabled()) {
            return new WorkedLogger(new Logger(TAG));
        }
        else {
            return new EmptyLogger();
        }
    }
}
