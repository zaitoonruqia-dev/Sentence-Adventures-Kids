package com.ruqiazaitoon.sentenceadventurekids.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

object AdsController {
    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null
    private var initialized = false

    fun initialize(context: Context) {
        if (initialized) return
        initialized = true

        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
                .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_G)
                .build()
        )
        MobileAds.initialize(context) {
            loadInterstitial(context)
            loadRewarded(context)
        }
    }

    fun showWorldCompleteInterstitial(activity: Activity, onDone: () -> Unit) {
        val ad = interstitialAd
        if (ad == null) {
            loadInterstitial(activity.applicationContext)
            onDone()
            return
        }

        interstitialAd = null
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                loadInterstitial(activity.applicationContext)
                onDone()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                loadInterstitial(activity.applicationContext)
                onDone()
            }
        }
        ad.show(activity)
    }

    fun showRewardedBonus(activity: Activity, onRewardEarned: () -> Unit, onDone: () -> Unit) {
        val ad = rewardedAd
        if (ad == null) {
            loadRewarded(activity.applicationContext)
            onDone()
            return
        }

        rewardedAd = null
        var rewardEarned = false
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                loadRewarded(activity.applicationContext)
                if (rewardEarned) onRewardEarned()
                onDone()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                loadRewarded(activity.applicationContext)
                onDone()
            }
        }
        ad.show(activity) {
            rewardEarned = true
        }
    }

    private fun loadInterstitial(context: Context) {
        InterstitialAd.load(
            context,
            AdUnits.INTERSTITIAL,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }

    private fun loadRewarded(context: Context) {
        RewardedAd.load(
            context,
            AdUnits.REWARDED,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                }
            }
        )
    }
}
