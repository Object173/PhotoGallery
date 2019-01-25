package com.object173.photogallery.services;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

public final class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if(getResultCode() != Activity.RESULT_OK) {
            return;
        }

        final int requestCode = intent.getIntExtra(PollServiceAdapter.REQUEST_CODE, 0);
        final Notification notification = (Notification)intent
                .getParcelableExtra(PollServiceAdapter.NOTIFICATION);

        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(requestCode, notification);
    }
}
