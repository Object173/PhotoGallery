package com.object173.photogallery.util;

import android.widget.ImageView;

import com.object173.photogallery.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public final class PhotoLoadUtil {
    public static void loadPhoto(final ImageView imageView, final String url) {
        Picasso.get()
                .load(url)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_launcher_background)
                .into(imageView);
    }

    public static boolean loadPhoto(final ImageView imageView, final String url,
                                 final Callback callback) {
        try {
            Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(imageView, callback);
            return true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
