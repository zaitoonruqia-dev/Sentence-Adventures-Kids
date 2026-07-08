package com.ruqiazaitoon.sentenceadventurekids.ads

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsDeepBlue
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsInk
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError

@Composable
fun FriendlyBannerAd(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var status by remember { mutableStateOf("Loading ad...") }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = Color.White.copy(alpha = 0.82f),
        border = BorderStroke(2.dp, Color.White),
        shadowElevation = 3.dp
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            val adWidth = maxWidth.value.toInt().coerceAtLeast(320)
            val adSize = remember(adWidth) {
                AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(adSize.height.dp),
                    factory = {
                        AdView(it).apply {
                            setAdSize(adSize)
                            adUnitId = AdUnits.BANNER
                            adListener = object : AdListener() {
                                override fun onAdLoaded() {
                                    status = if (com.ruqiazaitoon.sentenceadventurekids.BuildConfig.DEBUG) {
                                        "Test ad loaded"
                                    } else {
                                        "Ad"
                                    }
                                }

                                override fun onAdFailedToLoad(error: LoadAdError) {
                                    status = "Ad unavailable: ${error.code}"
                                }
                            }
                            loadAd(AdRequest.Builder().build())
                        }
                    },
                    update = { adView ->
                        if (adView.adSize != adSize) {
                            status = "Loading ad..."
                            adView.setAdSize(adSize)
                            adView.loadAd(AdRequest.Builder().build())
                        }
                    }
                )
                Text(status, style = MaterialTheme.typography.bodyLarge.copy(color = KidsInk))
            }
        }
    }
}

@Composable
fun AdUnavailableNote(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge.copy(color = KidsDeepBlue)
    )
}

fun Context.findActivity(): Activity? {
    var current = this
    while (current is ContextWrapper) {
        if (current is Activity) return current
        current = current.baseContext
    }
    return null
}
