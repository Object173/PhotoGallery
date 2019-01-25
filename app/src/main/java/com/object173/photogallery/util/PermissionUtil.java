package com.object173.photogallery.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public final class PermissionUtil {

    public static boolean hasLocationPermission(final Context context, final String permission) {
        final int result = ContextCompat
                .checkSelfPermission(context, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
