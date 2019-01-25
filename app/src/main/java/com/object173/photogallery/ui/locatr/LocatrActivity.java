package com.object173.photogallery.ui.locatr;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.object173.photogallery.base.SingleFragmentActivity;

public class LocatrActivity extends SingleFragmentActivity {
    private static final int REQUEST_ERROR = 0;

    public static Intent newIntent(final Context context) {
        final Intent intent = new Intent(context, LocatrActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return LocatrFragment.newInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();

        final GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int errorCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if(errorCode != ConnectionResult.SUCCESS) {
            final Dialog errorDialog = apiAvailability
                    .getErrorDialog(this, errorCode, REQUEST_ERROR,
                            new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(final DialogInterface dialog) {
                                    finish();
                                }
                            });
            errorDialog.show();
        }
    }
}
