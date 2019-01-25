package com.object173.photogallery.util;

import android.content.Context;
import android.preference.PreferenceManager;

public final class PreferencesUtil {
    private static final String PREF_SEARCH_QUERY = "search_query";
    private static final String PREF_LAST_RESULT_ID = "last_result_id";
    private static final String PREF_IS_ALARM_ON = "is_alarm_on";

    public static String getStoredQuery(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_QUERY, null);
    }

    public static void setStoredQuery(final Context context, final String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SEARCH_QUERY, query)
                .apply();
    }

    public static String getLastResultId(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_LAST_RESULT_ID, null);
    }

    public static void setLastResultId(final Context context, final String id) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_LAST_RESULT_ID, id)
                .apply();
    }

    public static boolean getIsAlarmOn(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_ALARM_ON, false);
    }

    public static void setIsAlarmOn(final Context context, final boolean isOn) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_ALARM_ON, isOn)
                .apply();
    }
}
