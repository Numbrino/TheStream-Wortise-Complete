package com.app.thestream.ads;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import com.wortise.ads.AdError;
import com.wortise.ads.appopen.AppOpenAd;
import com.wortise.ads.appopen.AppOpenAdListener;

import java.util.Date;

/**
 * Manages Wortise App Open Ads with lifecycle awareness and auto-reload functionality.
 * Integrates with ProcessLifecycleOwner to show ads when app comes to foreground.
 */
public class WortiseAppOpenManager implements Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    private static final String TAG = "WortiseAppOpenManager";
    private static final long CACHE_TIMEOUT_MS = 4 * 60 * 60 * 1000; // 4 hours in milliseconds

    private AppOpenAd appOpenAd;
    private Activity currentActivity;
    private String adUnitId;
    private boolean isShowingAd = false;
    private boolean isLoadingAd = false;
    private long loadTime = 0;

    /**
     * Constructor for WortiseAppOpenManager.
     *
     * @param application The application instance
     * @param adUnitId    The Wortise ad unit ID for app open ads
     */
    public WortiseAppOpenManager(@NonNull Application application, @NonNull String adUnitId) {
        this.adUnitId = adUnitId;
        application.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    /**
     * Load an app open ad.
     */
    public void loadAd(@NonNull Context context) {
        if (isLoadingAd || isAdAvailable()) {
            Log.d(TAG, "Ad is already loading or available");
            return;
        }

        isLoadingAd = true;
        Log.d(TAG, "Loading app open ad...");

        AppOpenAd.loadAd(context, adUnitId, new AppOpenAdListener() {
            @Override
            public void onAppOpenClicked(@NonNull AppOpenAd appOpenAd) {
                Log.d(TAG, "App open ad clicked");
            }

            @Override
            public void onAppOpenClosed(@NonNull AppOpenAd appOpenAd) {
                Log.d(TAG, "App open ad closed");
                isShowingAd = false;
                WortiseAppOpenManager.this.appOpenAd = null;
                loadAd(context);
            }

            @Override
            public void onAppOpenFailed(@NonNull AppOpenAd appOpenAd, @NonNull AdError adError) {
                Log.e(TAG, "App open ad failed to load: " + adError.getMessage());
                isLoadingAd = false;
            }

            @Override
            public void onAppOpenImpression(@NonNull AppOpenAd appOpenAd) {
                Log.d(TAG, "App open ad impression recorded");
            }

            @Override
            public void onAppOpenLoaded(@NonNull AppOpenAd appOpenAd) {
                Log.d(TAG, "App open ad loaded successfully");
                WortiseAppOpenManager.this.appOpenAd = appOpenAd;
                isLoadingAd = false;
                loadTime = new Date().getTime();
            }

            @Override
            public void onAppOpenShown(@NonNull AppOpenAd appOpenAd) {
                Log.d(TAG, "App open ad shown");
            }
        });
    }

    /**
     * Show the app open ad if available and not expired.
     */
    public void showAdIfAvailable() {
        if (!isShowingAd && isAdAvailable()) {
            if (currentActivity != null) {
                Log.d(TAG, "Showing app open ad");
                isShowingAd = true;
                appOpenAd.showAd(currentActivity);
            } else {
                Log.w(TAG, "No current activity to show ad");
            }
        } else {
            Log.d(TAG, "Ad not available or already showing");
            if (currentActivity != null) {
                loadAd(currentActivity);
            }
        }
    }

    /**
     * Check if an ad is available and not expired.
     *
     * @return true if ad is available and valid, false otherwise
     */
    private boolean isAdAvailable() {
        return appOpenAd != null && !isAdExpired();
    }

    /**
     * Check if the cached ad has expired.
     *
     * @return true if ad has expired, false otherwise
     */
    private boolean isAdExpired() {
        return (new Date().getTime() - loadTime) > CACHE_TIMEOUT_MS;
    }

    // Application.ActivityLifecycleCallbacks implementation

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        if (currentActivity == activity) {
            currentActivity = null;
        }
    }

    // DefaultLifecycleObserver implementation

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        Log.d(TAG, "App moved to foreground");
        showAdIfAvailable();
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        Log.d(TAG, "App moved to background");
    }
}
