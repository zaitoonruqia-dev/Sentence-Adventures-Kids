package com.ruqiazaitoon.sentenceadventurekids.ads

import com.ruqiazaitoon.sentenceadventurekids.BuildConfig

object AdUnits {
    const val APP_ID = "ca-app-pub-4190306648899689~4065183726"

    val BANNER: String
        get() = if (BuildConfig.DEBUG) {
            "ca-app-pub-3940256099942544/6300978111"
        } else {
            "ca-app-pub-4190306648899689/6336123842"
        }

    val INTERSTITIAL: String
        get() = if (BuildConfig.DEBUG) {
            "ca-app-pub-3940256099942544/1033173712"
        } else {
            "ca-app-pub-4190306648899689/6842143468"
        }

    val REWARDED: String
        get() = if (BuildConfig.DEBUG) {
            "ca-app-pub-3940256099942544/5224354917"
        } else {
            "ca-app-pub-4190306648899689/4215980128"
        }
}
