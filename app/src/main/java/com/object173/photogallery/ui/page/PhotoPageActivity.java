package com.object173.photogallery.ui.page;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.object173.photogallery.base.SingleFragmentActivity;

public final class PhotoPageActivity extends SingleFragmentActivity {

    public static Intent newIntent(final Context context, final Uri uri) {
        final Intent intent = new Intent(context, PhotoPageActivity.class);
        intent.setData(uri);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        if(getIntent() == null || getIntent().getData() == null) {
            finish();
        }
        return PhotoPageFragment.newInstance(getIntent().getData());
    }

    @Override
    public void onBackPressed() {
        final PhotoPageFragment photoPageFragment = (PhotoPageFragment)mFragment;
        if(!photoPageFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
