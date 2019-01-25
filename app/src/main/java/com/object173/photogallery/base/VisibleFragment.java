package com.object173.photogallery.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;

import com.object173.photogallery.services.PollServiceAdapter;

public class VisibleFragment extends Fragment {

    @Override
    public void onStart() {
        super.onStart();

        final IntentFilter filter = new IntentFilter(PollServiceAdapter.ACTION_SHOW_NOTIFICATION);
        getActivity().registerReceiver(mOnShowNotification, filter, PollServiceAdapter.PERM_PRIVATE, null);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mOnShowNotification);
    }

    private BroadcastReceiver mOnShowNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            setResultCode(Activity.RESULT_CANCELED);
        }
    };
}
