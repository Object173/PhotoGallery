package com.object173.photogallery.ui.gallery;

import android.arch.paging.PagedListAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.object173.photogallery.R;
import com.object173.photogallery.databinding.ItemPhotoListBinding;
import com.object173.photogallery.model.GalleryItem;

final class PhotoPagedAdapter extends PagedListAdapter<GalleryItem, PhotoViewHolder> {

    PhotoPagedAdapter(final DiffUtil.ItemCallback<GalleryItem> diffUtilCallback) {
        super(diffUtilCallback);
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        final ItemPhotoListBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.item_photo_list, viewGroup, false);
        return new PhotoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final PhotoViewHolder photoViewHolder, final int position) {
        photoViewHolder.bindGalleryItem(getItem(position));
    }
}
