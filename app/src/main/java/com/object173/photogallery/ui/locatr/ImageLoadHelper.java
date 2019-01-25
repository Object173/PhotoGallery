package com.object173.photogallery.ui.locatr;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.object173.photogallery.fetchr.FlickrFetchr;
import com.object173.photogallery.model.GalleryItem;
import com.object173.photogallery.util.log.MyLoggable;
import com.object173.photogallery.util.log.MyLoggerFactory;

import java.io.IOException;
import java.util.List;

final class ImageLoadHelper implements Observer<Location> {
    private final MutableLiveData<List<GalleryItem>> mLiveGalleryItem;
    private final MutableLiveData<Location> mCurrentLocation;

    ImageLoadHelper(@NonNull final LifecycleOwner lifecycleOwner,
                    @NonNull final MutableLiveData<List<GalleryItem>> liveGalleryItem,
                    @NonNull final MutableLiveData<Location> currentLocation) {
        mLiveGalleryItem = liveGalleryItem;
        mCurrentLocation = currentLocation;
        currentLocation.observe(lifecycleOwner, this);
    }

    @Override
    public void onChanged(@Nullable final Location location) {
        if(location == null) {
            return;
        }
        loadImages();
    }

    private void loadImages() {
        final Location currentLocation = mCurrentLocation.getValue();
        if(currentLocation == null) {
            return;
        }
        final SearchTask task = new SearchTask(mLiveGalleryItem);
        task.execute(currentLocation);
    }

    private static final class SearchTask extends AsyncTask<Location, Void, Void> {
        private static final MyLoggable mLogger = MyLoggerFactory.get(SearchTask.class.getSimpleName());

        private MutableLiveData<List<GalleryItem>> mResultData;

        SearchTask(final MutableLiveData<List<GalleryItem>> data) {
            mResultData = data;
        }

        @Override
        protected Void doInBackground(final Location... locations) {
            final Location currentLocation = locations[0];
            try {
                final List<GalleryItem> items = FlickrFetchr.getInstance()
                        .searchItems(currentLocation.getLatitude(), currentLocation.getLongitude(), 0);
                if(items.size() > 0) {
                    mResultData.postValue(items);
                }
                else {
                    mResultData.postValue(null);
                }
            }
            catch (IOException ex) {
                mLogger.error("Failed load lat: " + currentLocation.getLatitude() +
                        " lon: " + currentLocation.getLongitude(), ex);
                mResultData.postValue(null);
            }
            return null;
        }
    }
}
