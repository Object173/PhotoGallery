package com.object173.photogallery.ui.locatr;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.location.Location;
import android.os.AsyncTask;

import com.object173.photogallery.fetchr.FlickrFetchr;
import com.object173.photogallery.model.GalleryItem;
import com.object173.photogallery.util.log.MyLoggable;
import com.object173.photogallery.util.log.MyLoggerFactory;

import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

final class ImageLoadHelper {
    private static final ImageLoadHelper mInstance = new ImageLoadHelper();

    private final MutableLiveData<List<GalleryItem>> mLiveGalleryItem = new MutableLiveData<>();

    public static ImageLoadHelper getInstance() {
        return mInstance;
    }

    public void loadImages(final Location currentLocation) {
        if(currentLocation == null) {
            return;
        }
        FlickrFetchr.getInstance().searchItemsRx(currentLocation.getLatitude(),
                currentLocation.getLongitude(), 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemList -> mLiveGalleryItem.setValue(itemList));
    }

    public boolean observe(final LifecycleOwner lifecycleOwner,
                           final Observer<List<GalleryItem>> observer) {
        if(lifecycleOwner == null || observer == null) {
            return false;
        }
        mLiveGalleryItem.observe(lifecycleOwner, observer);
        return true;
    }
}
