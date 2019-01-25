package com.object173.photogallery.services;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.object173.photogallery.util.PreferencesUtil;
import com.object173.photogallery.R;
import com.object173.photogallery.fetchr.FlickrFetchr;
import com.object173.photogallery.ui.gallery.PhotoGalleryActivity;
import com.object173.photogallery.model.GalleryItem;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class PollServiceAdapter {

    private static final long POLL_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);
    public static final String ACTION_SHOW_NOTIFICATION = "com.object173.photogallery.SHOW_NOTIFICATION";
    public static final String PERM_PRIVATE = "com.object173.photogallery.PRIVATE";
    static final String REQUEST_CODE = "REQUEST_CODE";
    static final String NOTIFICATION = "NOTIFICATION";

    static long getInterval() {
        return POLL_INTERVAL_MS;
    }

    public static void setServiceAlarm(final Context context, final boolean isOn) {
        PreferencesUtil.setIsAlarmOn(context, isOn);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            PollJobService.setServiceAlarm(context, isOn);
        }
        else {
            PollService.setServiceAlarm(context, isOn);
        }
    }

    public static boolean isServiceAlarmOn(final Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return PollJobService.isServiceAlarmOn(context);
        }
        else {
            return PollService.isServiceAlarmOn(context);
        }
    }

    private static void showNotification(final Context context) {
        final Resources resources = context.getResources();
        final Intent intent = PhotoGalleryActivity.newIntent(context);
        final PendingIntent pendingIntent = PendingIntent
                .getActivity(context, 0, intent, 0);

        final Notification notification = new NotificationCompat.Builder(context, "Default")
                .setTicker(resources.getString(R.string.new_pictures_title))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(resources.getString(R.string.new_pictures_title))
                .setContentText(resources.getString(R.string.new_pictures_text))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        showBackgroundNotification(context, 0, notification);
    }

    private static void showBackgroundNotification(final Context context,
                                                   final int requestCode,
                                                   final Notification notification) {
        final Intent intent = new Intent(ACTION_SHOW_NOTIFICATION);
        intent.putExtra(REQUEST_CODE, requestCode);
        intent.putExtra(NOTIFICATION, notification);
        context.sendOrderedBroadcast(intent, PERM_PRIVATE, null, null,
                Activity.RESULT_OK, null, null);
    }

    private static boolean isNetworkAvaibleAndConnected(final Context context) {
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = connectivityManager.getActiveNetworkInfo() != null;

        return isNetworkAvailable &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }

    static boolean checkNewItems(final Context context, final String TAG) {
        if(!PollServiceAdapter.isNetworkAvaibleAndConnected(context)) {
            Log.i(TAG, "network is not connect");
            return false;
        }
        final String query = PreferencesUtil.getStoredQuery(context);
        final String lastResultId = PreferencesUtil.getLastResultId(context);
        final List<GalleryItem> items;

        try {
            if (query == null) {
                items = FlickrFetchr.getInstance().fetchItems(0);
            }
            else {
                items = FlickrFetchr.getInstance().searchItems(query, 0);
            }
            if(items.size() == 0) {
                return false;
            }

            final String resultId = items.get(0).getId();
            if(resultId.equals(lastResultId)) {
                Log.i(TAG, "Got an old result: " + resultId);
            }
            else {
                PreferencesUtil.setLastResultId(context, resultId);
                PollServiceAdapter.showNotification(context);
                Log.i(TAG, "Got an new result: " + resultId);
            }
        }
        catch (IOException ioe) {
            Log.e(TAG, "Failed load new items", ioe);
            return false;
        }
        return true;
    }
}
