package com.object173.photogallery.ui.locatr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.object173.photogallery.R;

public final class PermissionDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_permission_dialog)
                .setMessage(R.string.text_dialog_permission)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
