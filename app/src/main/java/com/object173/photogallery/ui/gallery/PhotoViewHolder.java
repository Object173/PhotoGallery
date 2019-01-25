package com.object173.photogallery.ui.gallery;

import android.support.v7.widget.RecyclerView;

import com.object173.photogallery.databinding.ItemPhotoListBinding;
import com.object173.photogallery.model.GalleryItem;
import com.object173.photogallery.viewmodel.PhotoViewModel;

final class PhotoViewHolder extends RecyclerView.ViewHolder {
    private ItemPhotoListBinding mBinding;

    PhotoViewHolder(final ItemPhotoListBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
        mBinding.setPhoto(new PhotoViewModel());
    }

    void bindGalleryItem(final GalleryItem item) {
        mBinding.getPhoto().setGalleryItem(item);
        mBinding.executePendingBindings();
    }
}
