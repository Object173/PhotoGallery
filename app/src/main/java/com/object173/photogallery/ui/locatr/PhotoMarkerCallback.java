package com.object173.photogallery.ui.locatr;

import android.widget.ImageView;

import com.google.android.gms.maps.model.Marker;
import com.object173.photogallery.util.PhotoLoadUtil;
import com.squareup.picasso.Callback;

final class PhotoMarkerCallback implements Callback {
    private final Marker mMarker;
    private final String mURL;
    private final ImageView mImageView;

    PhotoMarkerCallback(final Marker marker, final String URL, final ImageView imageView) {
        this.mMarker  = marker;
        this.mURL = URL;
        this.mImageView = imageView;
    }

    @Override
    public void onSuccess() {
        if (mMarker != null && mMarker.isInfoWindowShown()) {
            mMarker.hideInfoWindow();
            PhotoLoadUtil.loadPhoto(mImageView, mURL);
            mMarker.showInfoWindow();
        }
    }

    @Override
    public void onError(final Exception e) {
    }
}
