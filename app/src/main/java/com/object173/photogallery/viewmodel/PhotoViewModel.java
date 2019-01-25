package com.object173.photogallery.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.object173.photogallery.util.PhotoLoadUtil;
import com.object173.photogallery.model.GalleryItem;
import com.object173.photogallery.ui.page.PhotoPageActivity;

public final class PhotoViewModel extends BaseObservable {
    private GalleryItem mGalleryItem;

    public void setGalleryItem(final GalleryItem galleryItem) {
        mGalleryItem = galleryItem;
        notifyChange();
    }

    public String getTitle() {
        return mGalleryItem == null ? null : mGalleryItem.getCaption();
    }

    public String getUrl() {return mGalleryItem.getUrl();}

    public void onClick(final View view) {
        final Context context = view.getContext();
        final Intent intent = PhotoPageActivity.newIntent(context, mGalleryItem.getPhotoPageUri());
        context.startActivity(intent);
    }

    @BindingAdapter({"app:image"})
    public static void loadPhoto(final ImageView view, final String url) {
        if(url == null) {
            view.setImageDrawable(null);
        }
        Log.i("PhotoViewModel", "load image " + url);
        PhotoLoadUtil.loadPhoto(view, url);
    }
}
