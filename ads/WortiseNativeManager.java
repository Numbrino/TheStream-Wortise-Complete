package com.thestream.app.ads;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.wortise.ads.AdError;
import com.wortise.ads.natives.NativeAd;
import com.wortise.ads.natives.NativeAdListener;

/**
 * WortiseNativeManager - Complete Wortise Native Ad Implementation
 * Handles native ad loading, population, and lifecycle management
 */
public class WortiseNativeManager {

    private Activity activity;
    private NativeAd nativeAd;
    private String adUnitId;
    private NativeAdCallback callback;

    /**
     * Callback interface for native ad events
     */
    public interface NativeAdCallback {
        void onAdLoaded();
        void onAdFailedToLoad(String error);
        void onAdClicked();
        void onAdImpression();
    }

    /**
     * Constructor
     * @param activity The activity context
     * @param adUnitId The Wortise native ad unit ID
     */
    public WortiseNativeManager(Activity activity, String adUnitId) {
        this.activity = activity;
        this.adUnitId = adUnitId;
    }

    /**
     * Set callback listener for ad events
     * @param callback The callback interface implementation
     */
    public void setCallback(NativeAdCallback callback) {
        this.callback = callback;
    }

    /**
     * Load native ad
     */
    public void loadAd() {
        if (activity == null || activity.isFinishing()) {
            if (callback != null) {
                callback.onAdFailedToLoad("Activity is null or finishing");
            }
            return;
        }

        // Initialize NativeAd with ad unit ID
        nativeAd = new NativeAd(activity, adUnitId);

        // Set native ad listener
        nativeAd.setListener(new NativeAdListener() {
            @Override
            public void onNativeClicked(@NonNull NativeAd nativeAd) {
                if (callback != null) {
                    callback.onAdClicked();
                }
            }

            @Override
            public void onNativeFailed(@NonNull NativeAd nativeAd, @NonNull AdError adError) {
                if (callback != null) {
                    callback.onAdFailedToLoad(adError.toString());
                }
            }

            @Override
            public void onNativeImpression(@NonNull NativeAd nativeAd) {
                if (callback != null) {
                    callback.onAdImpression();
                }
            }

            @Override
            public void onNativeLoaded(@NonNull NativeAd nativeAd) {
                if (callback != null) {
                    callback.onAdLoaded();
                }
            }
        });

        // Load the native ad
        nativeAd.loadAd();
    }

    /**
     * Populate native ad views with ad content
     * @param containerView The root container view for the native ad
     * @param iconView The ImageView for the ad icon
     * @param titleView The TextView for the ad title
     * @param descriptionView The TextView for the ad description
     * @param ratingBar The RatingBar for the ad rating
     * @param callToActionButton The Button for call-to-action
     * @param advertiserView The TextView for the advertiser name
     * @param mediaView The View for media content (image/video)
     * @param priceView The TextView for the price
     * @param storeView The TextView for the store name
     */
    public void populateNativeAd(
            View containerView,
            ImageView iconView,
            TextView titleView,
            TextView descriptionView,
            RatingBar ratingBar,
            Button callToActionButton,
            TextView advertiserView,
            View mediaView,
            TextView priceView,
            TextView storeView) {

        if (nativeAd == null) {
            return;
        }

        // Set icon
        if (iconView != null && nativeAd.getIcon() != null) {
            iconView.setImageDrawable(nativeAd.getIcon());
            iconView.setVisibility(View.VISIBLE);
        } else if (iconView != null) {
            iconView.setVisibility(View.GONE);
        }

        // Set title
        if (titleView != null && nativeAd.getTitle() != null) {
            titleView.setText(nativeAd.getTitle());
            titleView.setVisibility(View.VISIBLE);
        } else if (titleView != null) {
            titleView.setVisibility(View.GONE);
        }

        // Set description
        if (descriptionView != null && nativeAd.getDescription() != null) {
            descriptionView.setText(nativeAd.getDescription());
            descriptionView.setVisibility(View.VISIBLE);
        } else if (descriptionView != null) {
            descriptionView.setVisibility(View.GONE);
        }

        // Set rating
        if (ratingBar != null && nativeAd.getRating() != null) {
            ratingBar.setRating(nativeAd.getRating().floatValue());
            ratingBar.setVisibility(View.VISIBLE);
        } else if (ratingBar != null) {
            ratingBar.setVisibility(View.GONE);
        }

        // Set call-to-action button
        if (callToActionButton != null && nativeAd.getCallToAction() != null) {
            callToActionButton.setText(nativeAd.getCallToAction());
            callToActionButton.setVisibility(View.VISIBLE);
        } else if (callToActionButton != null) {
            callToActionButton.setVisibility(View.GONE);
        }

        // Set advertiser
        if (advertiserView != null && nativeAd.getAdvertiser() != null) {
            advertiserView.setText(nativeAd.getAdvertiser());
            advertiserView.setVisibility(View.VISIBLE);
        } else if (advertiserView != null) {
            advertiserView.setVisibility(View.GONE);
        }

        // Set price
        if (priceView != null && nativeAd.getPrice() != null) {
            priceView.setText(nativeAd.getPrice());
            priceView.setVisibility(View.VISIBLE);
        } else if (priceView != null) {
            priceView.setVisibility(View.GONE);
        }

        // Set store
        if (storeView != null && nativeAd.getStore() != null) {
            storeView.setText(nativeAd.getStore());
            storeView.setVisibility(View.VISIBLE);
        } else if (storeView != null) {
            storeView.setVisibility(View.GONE);
        }

        // Register the native ad view
        if (containerView != null) {
            nativeAd.registerViewForInteraction(containerView);
        }
    }

    /**
     * Simplified populateNativeAd method with minimal parameters
     * @param containerView The root container view
     * @param iconView The ImageView for icon
     * @param titleView The TextView for title
     * @param descriptionView The TextView for description
     * @param callToActionButton The Button for call-to-action
     */
    public void populateNativeAd(
            View containerView,
            ImageView iconView,
            TextView titleView,
            TextView descriptionView,
            Button callToActionButton) {
        populateNativeAd(containerView, iconView, titleView, descriptionView,
                null, callToActionButton, null, null, null, null);
    }

    /**
     * Get the native ad instance
     * @return NativeAd instance or null if not loaded
     */
    public NativeAd getNativeAd() {
        return nativeAd;
    }

    /**
     * Check if native ad is loaded and ready to display
     * @return true if ad is loaded, false otherwise
     */
    public boolean isAdLoaded() {
        return nativeAd != null && nativeAd.isAvailable();
    }

    /**
     * Destroy the native ad and clean up resources
     * Call this method when the ad is no longer needed (e.g., in onDestroy())
     */
    public void destroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
            nativeAd = null;
        }
        callback = null;
        activity = null;
    }
}
