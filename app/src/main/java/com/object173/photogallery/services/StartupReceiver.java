package com.object173.photogallery.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.object173.photogallery.fetchr.FlickrFetchr;
import com.object173.photogallery.util.PreferencesUtil;
import com.object173.photogallery.util.log.MyLoggable;
import com.object173.photogallery.util.log.MyLoggerFactory;

public final class StartupReceiver extends BroadcastReceiver {
    private static final MyLoggable mLogger = MyLoggerFactory.get(StartupReceiver.class.getSimpleName());

    @Override
    public void onReceive(final Context context, final Intent intent) {
        mLogger.info("Received broadcast intent " + intent);

        final boolean isOn = PreferencesUtil.getIsAlarmOn(context);
        PollServiceAdapter.setServiceAlarm(context, isOn);
    }
}
