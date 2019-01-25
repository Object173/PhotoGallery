package com.object173.photogallery.ui.locatr;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.object173.photogallery.R;
import com.object173.photogallery.model.GalleryItem;
import com.object173.photogallery.util.PermissionUtil;
import com.object173.photogallery.util.PhotoLoadUtil;

import java.util.HashMap;
import java.util.List;

final class MapHelper implements LifecycleObserver {
    private static final String[] LOCATION_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int REQUEST_LOCATION_PERMISSIONS = 0;

    private Context mContext;

    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleApiClient mClient;

    private GoogleMap mMap;
    private PhotoInfoWindow mPhotoInfoWindow;

    private MutableLiveData<Location> mCurrentLocation = new MutableLiveData<>();

    private MapListener mMapListener;
    public interface MapListener {
        void onMapConnected();
        void onGalleryItemClick(GalleryItem item);
    }

    MapHelper (@NonNull final Context context,
               @NonNull final MapListener mapListener) {
        mContext = context;
        mMapListener = mapListener;

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        mClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable final Bundle bundle) {
                        mMapListener.onMapConnected();
                    }
                    @Override
                    public void onConnectionSuspended(final int i) {
                    }
                })
                .build();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onStart() {
        mMapListener.onMapConnected();
        mClient.connect();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onStop() {
        mMapListener = null;
        mContext = null;
        mClient.disconnect();
    }

    void setMap(@NonNull final GoogleMap map) {
        mMap = map;
        mPhotoInfoWindow = new PhotoInfoWindow();
        mMap.setInfoWindowAdapter(mPhotoInfoWindow);
        mMap.setOnInfoWindowLongClickListener(mPhotoInfoWindow);
    }

    @SuppressLint("MissingPermission")
    void updateMap(final List<GalleryItem> galleryItems) {
        if(mMap == null || galleryItems == null) {
            return;
        }

        mMap.clear();
        mPhotoInfoWindow.clearItems();

        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(GalleryItem item : galleryItems) {
            final LatLng itemPoint = new LatLng(item.getLat(), item.getLon());
            final Marker itemMarker = mMap.addMarker(
                    new MarkerOptions()
                            .position(itemPoint)
                            .title(item.getCaption()));
            mPhotoInfoWindow.addItem(itemMarker, item);
            builder.include(itemPoint);
        }

        final int margin = mContext.getResources().getDimensionPixelSize(R.dimen.map_inset_margin);
        final CameraUpdate update = CameraUpdateFactory.newLatLngBounds(builder.build(), margin);
        mMap.animateCamera(update);
        mMap.setMyLocationEnabled(true);
    }

    @SuppressLint("MissingPermission")
    void refreshLocation() {
        if(!PermissionUtil.hasLocationPermission(mContext, LOCATION_PERMISSIONS[0])) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(final Location location) {
                        mCurrentLocation.setValue(location);
                    }
                });
    }

    boolean isClientConnected() {
        return mClient.isConnected();
    }

    MutableLiveData<Location> getCurrentLocation() {
        return mCurrentLocation;
    }

    boolean onRequestPermission(final int requestCode) {
        if(requestCode != REQUEST_LOCATION_PERMISSIONS) {
            return false;
        }
        if(PermissionUtil.hasLocationPermission(mContext, LOCATION_PERMISSIONS[0])) {
            refreshLocation();
        }
        return true;
    }

    private final class PhotoInfoWindow implements GoogleMap.InfoWindowAdapter,
            GoogleMap.OnInfoWindowLongClickListener {

        private final HashMap<Marker, GalleryItem> mEventMarkerMap = new HashMap<>();


        final void addItem(final Marker marker, final GalleryItem item) {
            if(marker != null && item != null) {
                mEventMarkerMap.put(marker, item);
            }
        }

        final void clearItems() {
            mEventMarkerMap.clear();
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(final Marker marker) {
            final GalleryItem item = mEventMarkerMap.get(marker);
            if(item == null) {
                return null;
            }

            @SuppressLint("InflateParams")
            final View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.marker_gallery_item, null);

            final ImageView imageView = view.findViewById(R.id.item_image_view);
            final TextView textView = view.findViewById(R.id.item_text_view);

            PhotoLoadUtil.loadPhoto(imageView, item.getUrl(),
                    new PhotoMarkerCallback(marker, item.getUrl(), imageView));

            textView.setText(item.getCaption());

            return view;
        }

        @Override
        public void onInfoWindowLongClick(final Marker marker) {
            final GalleryItem item = mEventMarkerMap.get(marker);
            if(item == null) {
                return;
            }
            mMapListener.onGalleryItemClick(item);
        }
    }
}
