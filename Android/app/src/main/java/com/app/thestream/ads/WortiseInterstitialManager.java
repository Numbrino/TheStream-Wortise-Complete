package com.app.thestream.ads;

import android.app.Activity;
import android.util.Log;
import com.wortise.ads.interstitial.InterstitialAd;
import com.wortise.ads.interstitial.models.InterstitialAdListener;

public class WortiseInterstitialManager {
    private static final String TAG = "WortiseInterstitialManager";
    
    private InterstitialAd interstitialAd;
    private Activity activity;
    private String adUnitId;
    private InterstitialAdCallback callback;
    private boolean isAdReady = false;
    
    // Callback interface for interstitial ad events
    public interface InterstitialAdCallback {
        void onAdLoaded();
        void onAdFailedToLoad(String error);
        void onAdShown();
        void onAdFailedToShow(String error);
        void onAdClicked();
        void onAdClosed();
    }
    
    public WortiseInterstitialManager(Activity activity, String adUnitId) {
        this.activity = activity;
        this.adUnitId = adUnitId;
        initializeAd();
    }
    
    private void initializeAd() {
        interstitialAd = new InterstitialAd(activity, adUnitId);
        
        interstitialAd.setListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialClicked(InterstitialAd interstitialAd) {
                Log.d(TAG, "Interstitial ad clicked");
                if (callback != null) {
                    callback.onAdClicked();
                }
            }
            
            @Override
            public void onInterstitialClosed(InterstitialAd interstitialAd) {
                Log.d(TAG, "Interstitial ad closed");
                isAdReady = false;
                if (callback != null) {
                    callback.onAdClosed();
                }
                // Load next ad
                loadAd();
            }
            
            @Override
            public void onInterstitialFailed(InterstitialAd interstitialAd, String error) {
                Log.e(TAG, "Interstitial ad failed to load: " + error);
                isAdReady = false;
                if (callback != null) {
                    callback.onAdFailedToLoad(error);
                }
            }
            
            @Override
            public void onInterstitialLoaded(InterstitialAd interstitialAd) {
                Log.d(TAG, "Interstitial ad loaded successfully");
                isAdReady = true;
                if (callback != null) {
                    callback.onAdLoaded();
                }
            }
            
            @Override
            public void onInterstitialShown(InterstitialAd interstitialAd) {
                Log.d(TAG, "Interstitial ad shown");
                if (callback != null) {
                    callback.onAdShown();
                }
            }
        });
    }
    
    /**
     * Load an interstitial ad
     */
    public void loadAd() {
        if (interstitialAd != null && activity != null) {
            Log.d(TAG, "Loading interstitial ad...");
            isAdReady = false;
            interstitialAd.loadAd();
        } else {
            Log.e(TAG, "Cannot load ad - interstitial or activity is null");
        }
    }
    
    /**
     * Show the interstitial ad if ready
     */
    public void showAd() {
        if (interstitialAd != null && isAdReady()) {
            Log.d(TAG, "Showing interstitial ad");
            activity.runOnUiThread(() -> {
                try {
                    interstitialAd.showAd();
                } catch (Exception e) {
                    Log.e(TAG, "Error showing ad: " + e.getMessage());
                    if (callback != null) {
                        callback.onAdFailedToShow(e.getMessage());
                    }
                }
            });
        } else {
            Log.w(TAG, "Ad is not ready to show");
            if (callback != null) {
                callback.onAdFailedToShow("Ad not ready");
            }
        }
    }
    
    /**
     * Check if ad is ready to be shown
     * @return true if ad is ready, false otherwise
     */
    public boolean isAdReady() {
        return isAdReady && interstitialAd != null;
    }
    
    /**
     * Set callback for ad events
     * @param callback InterstitialAdCallback instance
     */
    public void setCallback(InterstitialAdCallback callback) {
        this.callback = callback;
    }
    
    /**
     * Destroy and cleanup resources
     */
    public void destroy() {
        Log.d(TAG, "Destroying interstitial manager");
        if (interstitialAd != null) {
            interstitialAd.setListener(null);
            interstitialAd = null;
        }
        callback = null;
        activity = null;
        isAdReady = false;
    }
}
