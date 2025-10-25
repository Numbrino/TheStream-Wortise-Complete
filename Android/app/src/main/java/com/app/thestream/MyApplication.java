package com.app.thestream;

import android.app.Application;
import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import com.wortise.ads.WortiseSdk;
import com.app.thestream.ads.WortiseAppOpenManager;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks, LifecycleObserver {

    private WortiseAppOpenManager appOpenManager;
    private Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        
        // Initialize Wortise SDK
        WortiseSdk.initialize(this);
        
        // Register activity lifecycle callbacks
        registerActivityLifecycleCallbacks(this);
        
        // Initialize App Open Ad Manager
        appOpenManager = new WortiseAppOpenManager(this);
        
        // Register lifecycle observer
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onMoveToForeground() {
        // Show app open ad when app comes to foreground
        if (appOpenManager != null && currentActivity != null) {
            appOpenManager.showAdIfAvailable(currentActivity);
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        currentActivity = activity;
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
        // Do nothing
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        // Do nothing
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        // Do nothing
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        if (currentActivity == activity) {
            currentActivity = null;
        }
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }
}
