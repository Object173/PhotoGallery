package com.object173.photogallery.fetchr;
import android.util.Log;

import com.object173.photogallery.model.GalleryItem;
import com.object173.photogallery.model.GalleryItemList;
import com.object173.photogallery.util.log.MyLoggable;
import com.object173.photogallery.util.log.MyLoggerFactory;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class FlickrFetchr {

    private static final FlickrFetchr mFlickrFetchr = new FlickrFetchr();
    private final FlickrAPI mFlickrAPI;

    private static final MyLoggable mLogger = MyLoggerFactory.get(FlickrFetchr.class.getSimpleName());
    private static final String API_KEY = "1940f7e72ea82ec9a56f36d0b16098ea";

    private FlickrFetchr() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FlickrAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mFlickrAPI = retrofit.create(FlickrAPI.class);
    }

    public static FlickrFetchr getInstance() {
        return mFlickrFetchr;
    }

    public List<GalleryItem> searchItems(final String query, final int page) throws IOException {
        return fetchItems(mFlickrAPI.getPhotos(API_KEY, query, page).execute());
    }

    public List<GalleryItem> fetchItems(final int page) throws IOException {
        return fetchItems(mFlickrAPI.getPhotos(API_KEY, page).execute());
    }

    public List<GalleryItem> searchItems(final double latitude, final double longitude,
                                         final int page) throws IOException {
        return fetchItems(mFlickrAPI.getPhotos(API_KEY, latitude, longitude, page).execute());
    }

    private List<GalleryItem> fetchItems(final Response<GalleryItemList> response) {
        if(response.isSuccessful()) {
            final GalleryItemList galleryItemList = response.body();
            if(galleryItemList == null || galleryItemList.getPhotos() == null) {
                mLogger.info("GalleryList is empty");
                return null;
            }
            return galleryItemList.getPhotos().getGalleryItemList();
        }
        else {
            mLogger.error("Failed to fetch items " + response.errorBody());
        }
        return null;
    }
}
