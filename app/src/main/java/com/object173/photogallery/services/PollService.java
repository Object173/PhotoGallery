package com.object173.photogallery.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.object173.photogallery.fetchr.FlickrFetchr;
import com.object173.photogallery.util.log.MyLoggable;
import com.object173.photogallery.util.log.MyLoggerFactory;

public final class PollService extends IntentService {
    private static final String TAG = PollService.class.getSimpleName();
    private static final MyLoggable mLogger = MyLoggerFactory.get(TAG);

    public PollService() {
        super(TAG);
    }

    public static Intent newIntent(final Context context) {
        return new Intent(context, PollService.class);
    }

    static void setServiceAlarm(final Context context, final boolean isOn) {
        final Intent intent = PollService.newIntent(context);
        final PendingIntent pendingIntent =
                PendingIntent.getService(context, 0, intent,0);

        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if(isOn) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), PollServiceAdapter.getInterval(), pendingIntent);
        }
        else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    static boolean isServiceAlarmOn(final Context context) {
        final Intent intent = PollService.newIntent(context);
        final PendingIntent pendingIntent = PendingIntent
                .getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        return pendingIntent != null;
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        mLogger.info("Service onHandleIntent");
        PollServiceAdapter.checkNewItems(this, TAG);
    }
}
