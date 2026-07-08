package com.ruqiazaitoon.sentenceadventurekids.ui.screens.journey

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ruqiazaitoon.sentenceadventurekids.R
import com.ruqiazaitoon.sentenceadventurekids.data.ProgressRepository
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StoryPanel
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StoryTopBar
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StorybookScreen
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsBrightOrange
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsDeepBlue
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsGrassGreen
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsInk
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsMagicPurple
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsSoftPink
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsSunnyYellow

data class ReadingWorld(
    val id: Int,
    val title: String,
    val subtitle: String,
    val goal: String,
    @DrawableRes val art: Int,
    val color: Color
)

val readingWorlds = listOf(
    ReadingWorld(1, "Meadow Words", "Tiny 2-3 word sentences", "Tap each word and hear the rhythm.", R.drawable.ic_story_mascot, KidsGrassGreen),
    ReadingWorld(2, "Kite Village", "Describing words and action", "Spot the color, size, or feeling word.", R.drawable.ic_story_book, KidsSunnyYellow),
    ReadingWorld(3, "Castle Connectors", "Longer ideas with and, or, because", "Follow how two ideas join together.", R.drawable.ic_story_castle, KidsSoftPink),
    ReadingWorld(4, "Planet Story", "Three connected adventure lines", "Read a mini story from start to finish.", R.drawable.ic_story_planet, KidsMagicPurple)
)

@Composable
fun JourneyScreen(
    onBack: () -> Unit,
    onLevelSelect: (Int) -> Unit
) {
    val unlockedLevels by ProgressRepository.unlockedLevels.collectAsState()

    StorybookScreen {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StoryTopBar(
                title = "Choose a World",
                subtitle = "Each world has three new reading cards",
                onBack = onBack
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(readingWorlds) { world ->
                    val isUnlocked = world.id in unlockedLevels
                    WorldCard(
                        world = world,
                        isUnlocked = isUnlocked,
                        onClick = { if (isUnlocked) onLevelSelect(world.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun WorldCard(
    world: ReadingWorld,
    isUnlocked: Boolean,
    onClick: () -> Unit
) {
    StoryPanel(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isUnlocked) { onClick() },
        color = if (isUnlocked) Color.White.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.55f),
        borderColor = if (isUnlocked) world.color else KidsDeepBlue.copy(alpha = 0.18f)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(world.art),
                contentDescription = null,
                modifier = Modifier.size(86.dp)
            )
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    world.title,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = if (isUnlocked) KidsInk else KidsDeepBlue.copy(alpha = 0.55f),
                        fontWeight = FontWeight.Black
                    )
                )
                Text(world.subtitle, style = MaterialTheme.typography.titleLarge.copy(color = KidsDeepBlue))
                Text(world.goal, style = MaterialTheme.typography.bodyLarge.copy(color = KidsInk))
            }
            Icon(
                imageVector = if (isUnlocked) Icons.Default.PlayArrow else Icons.Default.Lock,
                contentDescription = null,
                tint = if (isUnlocked) KidsBrightOrange else KidsDeepBlue.copy(alpha = 0.4f),
                modifier = Modifier.size(36.dp)
            )
        }
    }
}
