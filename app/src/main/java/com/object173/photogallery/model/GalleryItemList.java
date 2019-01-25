package com.object173.photogallery.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public final class GalleryItemList {
    @SerializedName("photos")
    private JsonPhotosEntity mPhotos;

    public JsonPhotosEntity getPhotos() {
        return mPhotos;
    }

    public final class JsonPhotosEntity {
        @SerializedName("photo")
        private ArrayList<GalleryItem> mGalleryItemList;
        @SerializedName("pages")
        private int mPages;
        @SerializedName("perpage")
        private int mPerpage;
        @SerializedName("total")
        private int mTotal;

        public ArrayList<GalleryItem> getGalleryItemList() {
            return mGalleryItemList;
        }

        public int getPages() {
            return mPages;
        }

        public int getPerpage() {
            return mPerpage;
        }

        public int getTotal() {
            return mTotal;
        }
    }
}
