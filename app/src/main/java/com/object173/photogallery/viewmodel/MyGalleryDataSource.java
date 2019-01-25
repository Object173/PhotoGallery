package com.object173.photogallery.viewmodel;

import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.object173.photogallery.fetchr.FlickrFetchr;
import com.object173.photogallery.model.GalleryItem;
import com.object173.photogallery.services.StartupReceiver;
import com.object173.photogallery.util.log.MyLoggable;
import com.object173.photogallery.util.log.MyLoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public final class MyGalleryDataSource extends PageKeyedDataSource<Integer, GalleryItem> {
    private static final MyLoggable mLogger = MyLoggerFactory.get(MyGalleryDataSource.class.getSimpleName());
    private final FlickrFetchr mFlickrFetchr;

    private String mQuery;

    private MyGalleryDataSource(final FlickrFetchr flickrFetchr, final String query) {
        mFlickrFetchr = flickrFetchr;
        mQuery = query;
    }

    private List<GalleryItem> loadData(final int currentPage) {
        try {
            final List<GalleryItem> itemList;
            if(mQuery == null || mQuery.length() == 0) {
                itemList = mFlickrFetchr.fetchItems(currentPage);
            }
            else {
                itemList = mFlickrFetchr.searchItems(mQuery, currentPage);
            }
            return itemList;
        }
        catch (IOException ex) {
            mLogger.error("Fail load items", ex);
            return null;
        }
    }

    @Override
    public void loadInitial(@NonNull final LoadInitialParams params,
                            @NonNull final LoadInitialCallback callback) {
        final List<GalleryItem> itemList = loadData(0);
        if(itemList != null) {
            callback.onResult(itemList, null, 1);
        }
    }

    @Override
    public void loadBefore(@NonNull final LoadParams params, @NonNull final LoadCallback callback) {
    }

    @Override
    public void loadAfter(@NonNull final LoadParams params, @NonNull final LoadCallback callback) {
        final int currentPage = (Integer) params.key;
        final List<GalleryItem> itemList = loadData(currentPage);
        if(itemList != null) {
            callback.onResult(itemList,  currentPage + 1);
        }
    }

    public static final class Factory extends DataSource.Factory<Integer, GalleryItem> {
        private final FlickrFetchr mFlickrFetchr;
        private final String mQuery;

        public Factory(final FlickrFetchr flickrFetchr, final String query) {
            mFlickrFetchr = flickrFetchr;
            mQuery = query;
        }

        @Override
        public DataSource<Integer, GalleryItem> create() {
            return new MyGalleryDataSource(mFlickrFetchr, mQuery);
        }
    }
}
