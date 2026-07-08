package com.ruqiazaitoon.sentenceadventurekids.ui.screens.rewards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ruqiazaitoon.sentenceadventurekids.R
import com.ruqiazaitoon.sentenceadventurekids.ads.AdsController
import com.ruqiazaitoon.sentenceadventurekids.ads.FriendlyBannerAd
import com.ruqiazaitoon.sentenceadventurekids.ads.findActivity
import com.ruqiazaitoon.sentenceadventurekids.data.ProgressRepository
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StatChip
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StoryActionButton
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StoryPanel
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StoryTopBar
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StorybookScreen
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsDeepBlue
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsGrassGreen
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsInk
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsMagicPurple
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsPanel
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsSunnyYellow

@Composable
fun RewardsScreen(onBack: () -> Unit) {
    val stars by ProgressRepository.stars.collectAsState()
    val coins by ProgressRepository.coins.collectAsState()
    val badges by ProgressRepository.badges.collectAsState()
    val activity = LocalContext.current.findActivity()
    var rewardMessage by remember { mutableStateOf("Earn a small bonus after a grown-up approved ad.") }

    StorybookScreen {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StoryTopBar(
                title = "Treasure Shelf",
                subtitle = "Rewards for brave reading",
                onBack = onBack
            )

            StoryPanel(modifier = Modifier.fillMaxWidth(), color = KidsPanel, borderColor = KidsSunnyYellow) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painterResource(R.drawable.ic_story_treasure), contentDescription = null, modifier = Modifier.size(112.dp))
                    Spacer(Modifier.width(14.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatChip(Icons.Default.Stars, "stars", stars.toString(), KidsSunnyYellow)
                        StatChip(Icons.Default.MonetizationOn, "coins", coins.toString(), KidsMagicPurple)
                    }
                }
            }

            StoryPanel(modifier = Modifier.fillMaxWidth(), borderColor = KidsMagicPurple) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Bonus Practice Reward",
                        style = MaterialTheme.typography.titleLarge.copy(color = KidsInk, fontWeight = FontWeight.Black)
                    )
                    Text(rewardMessage, style = MaterialTheme.typography.bodyLarge.copy(color = KidsDeepBlue))
                    StoryActionButton(
                        text = "Watch Bonus",
                        icon = Icons.Default.PlayCircle,
                        onClick = {
                            val currentActivity = activity
                            if (currentActivity == null) {
                                rewardMessage = "Bonus is not available right now."
                            } else {
                                rewardMessage = "Loading bonus..."
                                AdsController.showRewardedBonus(
                                    activity = currentActivity,
                                    onRewardEarned = {
                                        ProgressRepository.addRewardedPracticeBonus()
                                        rewardMessage = "Bonus collected: 2 stars and 20 coins."
                                    },
                                    onDone = {
                                        if (rewardMessage == "Loading bonus...") {
                                            rewardMessage = "Bonus ad is still getting ready. Try again soon."
                                        }
                                    }
                                )
                            }
                        },
                        color = KidsMagicPurple,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Text(
                "Badges",
                style = MaterialTheme.typography.headlineLarge.copy(color = KidsInk, fontWeight = FontWeight.Black)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(badges) { badge ->
                    BadgeItem(badge)
                }
            }

            FriendlyBannerAd()
        }
    }
}

@Composable
fun BadgeItem(name: String) {
    StoryPanel(modifier = Modifier.fillMaxWidth(), borderColor = KidsGrassGreen) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = KidsSunnyYellow, modifier = Modifier.size(42.dp))
            Column {
                Text(name, style = MaterialTheme.typography.titleLarge.copy(color = KidsInk, fontWeight = FontWeight.Black))
                Text("Keep reading to collect more.", style = MaterialTheme.typography.bodyLarge.copy(color = KidsDeepBlue))
            }
        }
    }
}
