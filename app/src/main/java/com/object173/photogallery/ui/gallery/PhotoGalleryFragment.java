package com.object173.photogallery.ui.gallery;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.object173.photogallery.ui.locatr.LocatrActivity;
import com.object173.photogallery.util.PreferencesUtil;
import com.object173.photogallery.base.VisibleFragment;
import com.object173.photogallery.fetchr.FlickrFetchr;
import com.object173.photogallery.R;
import com.object173.photogallery.databinding.FragmentPhotoGalleryBinding;
import com.object173.photogallery.model.GalleryItem;
import com.object173.photogallery.services.PollServiceAdapter;
import com.object173.photogallery.viewmodel.MyGalleryDataSource;

import java.util.concurrent.Executors;


public final class PhotoGalleryFragment extends VisibleFragment {

    private FragmentPhotoGalleryBinding mBinding;
    private PhotoPagedAdapter mPhotoAdapter;
    private LiveData<PagedList<GalleryItem>> mPagedListLiveData;

    private MutableLiveData<Boolean> mIsLoadData = new MutableLiveData<>();

    private static final int PREFETCH_DISTANCE = 10;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        mPhotoAdapter = new PhotoPagedAdapter(new PhotoDiffUtilCallback());
        final String query = PreferencesUtil.getStoredQuery(getActivity());
        setSearchQuery(query);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_gallery, container, false);

        mIsLoadData.observe(this, aBoolean -> setProgressStatus(aBoolean == null ? true : aBoolean));

        mBinding.photoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mBinding.photoRecyclerView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        final int photoSpanCount = (int)(mBinding.photoRecyclerView.getWidth() /
                                getResources().getDimension(R.dimen.gallery_image_size));
                        final GridLayoutManager layoutManager =
                                (GridLayoutManager) mBinding.photoRecyclerView.getLayoutManager();
                        layoutManager.setSpanCount(photoSpanCount);

                        mBinding.photoRecyclerView.setAdapter(mPhotoAdapter);
                        mBinding.photoRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

        mBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            final String query = PreferencesUtil.getStoredQuery(getActivity());
            setSearchQuery(query);
        });

        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_photo_gallery, menu);

        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView)searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                setSearchQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String query) {
                return false;
            }
        });

        searchView.setOnSearchClickListener(view -> {
            final String query = PreferencesUtil.getStoredQuery(getActivity());
            searchView.setQuery(query, false);
        });

        final MenuItem toggleItem = menu.findItem(R.id.menu_item_toggle_polling);
        toggleItem.setTitle(PollServiceAdapter.isServiceAlarmOn(getActivity()) ?
                R.string.stop_polling : R.string.start_polling);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                setSearchQuery(null);
                return true;
            case R.id.menu_item_toggle_polling:
                final boolean shouldStartAlarm = !PollServiceAdapter.isServiceAlarmOn(getActivity());
                PollServiceAdapter.setServiceAlarm(getActivity(), shouldStartAlarm);
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.menu_item_locate:
                final Intent intent = LocatrActivity.newIntent(getActivity());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setProgressStatus(final boolean isLoad) {
        if(mBinding == null) {
            return;
        }
        mBinding.swipeRefreshLayout.setRefreshing(isLoad);
    }

    private void setSearchQuery(final String query) {
        PreferencesUtil.setStoredQuery(getActivity(), query);
        mIsLoadData.setValue(true);

        if(mPagedListLiveData != null) {
            mPagedListLiveData.removeObservers(this);
        }

        final MyGalleryDataSource.Factory dataSourceFactory =
                new MyGalleryDataSource.Factory(FlickrFetchr.getInstance(), query);
        final PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(100)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .build();
        mPagedListLiveData = new LivePagedListBuilder<>(dataSourceFactory, config)
                        .setFetchExecutor(Executors.newSingleThreadExecutor())
                        .setInitialLoadKey(0)
                        .build();

        mPagedListLiveData.observe(this, galleryItems -> {
            mPhotoAdapter.submitList(galleryItems);
            mIsLoadData.setValue(false);
        });
    }
}
