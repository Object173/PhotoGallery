package com.object173.photogallery.ui.gallery;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.object173.photogallery.model.GalleryItem;

final class PhotoDiffUtilCallback extends DiffUtil.ItemCallback<GalleryItem> {

    @Override
    public boolean areItemsTheSame(@NonNull final GalleryItem oldItem, @NonNull final GalleryItem newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull final GalleryItem oldItem, @NonNull final GalleryItem newItem) {
        return oldItem.getCaption().equals(newItem.getCaption());
    }
}
