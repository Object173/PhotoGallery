package com.object173.photogallery.fetchr;

import com.object173.photogallery.model.GalleryItemList;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface FlickrAPI {
    static final String BASE_URL = "https://api.flickr.com/services/rest/";

    @GET("?method=flickr.photos.getRecent&format=json&nojsoncallback=1&extras=url_s")
    Call<GalleryItemList> getPhotos(@Query("api_key") String apiKey,
                                    @Query("page") int page);

    @GET("?method=flickr.photos.search&format=json&nojsoncallback=1&extras=url_s")
    Call<GalleryItemList> getPhotos(@Query("api_key") String apiKey,
                                    @Query("text") String query,
                                    @Query("page") int page);

    @GET("?method=flickr.photos.search&format=json&nojsoncallback=1&extras=url_s,geo")
    Call<GalleryItemList> getPhotos(@Query("api_key") String apiKey,
                                    @Query("lat") double latitude,
                                    @Query("lon") double longitude,
                                    @Query("page") int page);

    @GET("?method=flickr.photos.search&format=json&nojsoncallback=1&extras=url_s,geo")
    Single<GalleryItemList> getPhotosRx(@Query("api_key") String apiKey,
                                        @Query("lat") double latitude,
                                        @Query("lon") double longitude,
                                        @Query("page") int page);
}
