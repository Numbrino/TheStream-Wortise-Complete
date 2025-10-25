package com.app.thestream.ads;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;
import com.wortise.ads.AdError;
import com.wortise.ads.AdSize;
import com.wortise.ads.banner.BannerAd;
import com.wortise.ads.banner.BannerAdListener;

public class WortiseBannerManager {
    private static final String TAG = "WortiseBannerManager";
    private BannerAd bannerAd;
    private Activity activity;
    private ViewGroup adContainer;
    private String adUnitId;

    public WortiseBannerManager(Activity activity, ViewGroup adContainer, String adUnitId) {
        this.activity = activity;
        this.adContainer = adContainer;
        this.adUnitId = adUnitId;
    }

    public void loadBanner() {
        if (bannerAd != null) {
            return;
        }

        bannerAd = new BannerAd(activity);
        bannerAd.setAdSize(AdSize.BANNER);
        bannerAd.setAdUnitId(adUnitId);
        
        bannerAd.setListener(new BannerAdListener() {
            @Override
            public void onBannerClicked(BannerAd bannerAd) {
                Log.d(TAG, "Banner ad clicked");
            }

            @Override
            public void onBannerFailed(BannerAd bannerAd, AdError adError) {
                Log.e(TAG, "Banner ad failed to load: " + adError.toString());
            }

            @Override
            public void onBannerImpression(BannerAd bannerAd) {
                Log.d(TAG, "Banner ad impression");
            }

            @Override
            public void onBannerLoaded(BannerAd bannerAd) {
                Log.d(TAG, "Banner ad loaded successfully");
                if (adContainer != null) {
                    adContainer.removeAllViews();
                    adContainer.addView(bannerAd);
                }
            }
        });

        bannerAd.loadAd();
        Log.d(TAG, "Loading banner ad...");
    }

    public void loadLargeBanner() {
        if (bannerAd != null) {
            return;
        }

        bannerAd = new BannerAd(activity);
        bannerAd.setAdSize(AdSize.LARGE_BANNER);
        bannerAd.setAdUnitId(adUnitId);
        
        bannerAd.setListener(new BannerAdListener() {
            @Override
            public void onBannerClicked(BannerAd bannerAd) {
                Log.d(TAG, "Large banner ad clicked");
            }

            @Override
            public void onBannerFailed(BannerAd bannerAd, AdError adError) {
                Log.e(TAG, "Large banner ad failed to load: " + adError.toString());
            }

            @Override
            public void onBannerImpression(BannerAd bannerAd) {
                Log.d(TAG, "Large banner ad impression");
            }

            @Override
            public void onBannerLoaded(BannerAd bannerAd) {
                Log.d(TAG, "Large banner ad loaded successfully");
                if (adContainer != null) {
                    adContainer.removeAllViews();
                    adContainer.addView(bannerAd);
                }
            }
        });

        bannerAd.loadAd();
        Log.d(TAG, "Loading large banner ad...");
    }

    public void destroy() {
        if (bannerAd != null) {
            bannerAd.destroy();
            bannerAd = null;
            Log.d(TAG, "Banner ad destroyed");
        }
    }

    public void pause() {
        if (bannerAd != null) {
            bannerAd.onPause();
            Log.d(TAG, "Banner ad paused");
        }
    }

    public void resume() {
        if (bannerAd != null) {
            bannerAd.onResume();
            Log.d(TAG, "Banner ad resumed");
        }
    }
}
