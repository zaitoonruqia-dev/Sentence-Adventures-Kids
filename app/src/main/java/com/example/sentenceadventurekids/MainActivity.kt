package com.ruqiazaitoon.sentenceadventurekids

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ruqiazaitoon.sentenceadventurekids.ads.AdsController
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.SentenceAdventureKidsTheme
import com.ruqiazaitoon.sentenceadventurekids.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AdsController.initialize(applicationContext)
        enableEdgeToEdge()
        setContent {
            SentenceAdventureKidsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}
