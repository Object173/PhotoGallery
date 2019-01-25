package com.object173.photogallery.ui.page;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.object173.photogallery.R;
import com.object173.photogallery.base.VisibleFragment;
import com.object173.photogallery.databinding.FragmentPhotoPageBinding;

public final class PhotoPageFragment extends VisibleFragment {
    private static final String ARG_URI = "photo_page_uri";

    private Uri mUri;
    private FragmentPhotoPageBinding mBinding;

    public static PhotoPageFragment newInstance(@NonNull final Uri uri) {
        final Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        final PhotoPageFragment fragment = new PhotoPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            mUri = getArguments().getParcelable(ARG_URI);
        }
        else {
            getActivity().finish();
        }
    }

    @SuppressWarnings("SetJavaScriptEnabled")
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_page, container, false);

        mBinding.webView.getSettings().setJavaScriptEnabled(true);

        mBinding.progressBar.setMax(100);
        mBinding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(final WebView view, final int newProgress) {
                if(newProgress == 100) {
                    mBinding.progressBar.setVisibility(View.GONE);
                }
                else {
                    mBinding.progressBar.setVisibility(View.VISIBLE);
                    mBinding.progressBar.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(final WebView view, final String title) {
                final AppCompatActivity activity = (AppCompatActivity)getActivity();
                if(activity != null && activity.getSupportActionBar() != null) {
                    activity.getSupportActionBar().setSubtitle(title);
                }
            }
        });

        mBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view,
                                                    final String url) {
                final Uri uri = Uri.parse(url);
                final String scheme = uri.getScheme();

                if(scheme != null && (scheme.equals("http") || scheme.equals("https"))) {
                    return super.shouldOverrideUrlLoading(view, url);
                }
                else {
                    final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    return true;
                }
            }
        });
        mBinding.webView.loadUrl(mUri.toString());

        return mBinding.getRoot();
    }

    public boolean onBackPressed() {
        if(mBinding.webView.canGoBack()) {
            mBinding.webView.goBack();
            return true;
        }
        return false;
    }
}
