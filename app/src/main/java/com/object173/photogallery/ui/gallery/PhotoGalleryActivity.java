package com.object173.photogallery.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.object173.photogallery.base.SingleFragmentActivity;

public class PhotoGalleryActivity extends SingleFragmentActivity {

    public static Intent newIntent(final Context context) {
        return new Intent(context, PhotoGalleryActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}
