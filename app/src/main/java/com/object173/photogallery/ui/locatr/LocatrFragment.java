package com.object173.photogallery.ui.locatr;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.SupportMapFragment;
import com.object173.photogallery.ui.page.PhotoPageActivity;
import com.object173.photogallery.R;
import com.object173.photogallery.model.GalleryItem;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class LocatrFragment extends SupportMapFragment
        implements Observer<List<GalleryItem>>, MapHelper.MapListener {

    private boolean mIsRefreshing = false;
    private MenuItem mSearchMenuItem;

    private MapHelper mMapHelper;

    public static LocatrFragment newInstance() {
        return new LocatrFragment();
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        mMapHelper = new MapHelper(getActivity(), this);
        mMapHelper.getLocationSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ImageLoadHelper.getInstance()::loadImages);
        getLifecycle().addObserver(mMapHelper);

        getMapAsync(googleMap -> {
            mMapHelper.setMap(googleMap);
            ImageLoadHelper.getInstance().observe(this, this);
            if(savedInstanceState == null) {
                findImage();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_locatr, menu);

        mSearchMenuItem = menu.findItem(R.id.action_locate);
        mSearchMenuItem.setEnabled(mMapHelper.isClientConnected());
        setRefreshing(mIsRefreshing);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_locate:
                findImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        if(!mMapHelper.onRequestPermission(requestCode)) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void findImage() {
        setRefreshing(true);
        mMapHelper.refreshLocation();
    }

    private void setRefreshing(final boolean refreshing) {
        mIsRefreshing = refreshing;
        if(mSearchMenuItem == null) {
            return;
        }
        if(refreshing) {
            mSearchMenuItem.setActionView(R.layout.actionbar_refresh_progress);
        }
        else {
            mSearchMenuItem.setActionView(null);
        }
    }

    @Override
    public void onChanged(@Nullable final List<GalleryItem> galleryItems) {
        setRefreshing(false);
        if(galleryItems != null) {
            mMapHelper.updateMap(galleryItems);
        }
    }

    @Override
    public void onMapConnected() {
        if(getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

    @Override
    public void onGalleryItemClick(final GalleryItem item) {
        if(getActivity() != null) {
            final Intent intent = PhotoPageActivity.newIntent(getActivity(), item.getPhotoPageUri());
            startActivity(intent);
        }
    }
}
