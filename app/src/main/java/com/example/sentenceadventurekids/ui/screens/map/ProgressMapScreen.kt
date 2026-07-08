package com.ruqiazaitoon.sentenceadventurekids.ui.screens.map

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
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
import com.ruqiazaitoon.sentenceadventurekids.data.ProgressRepository
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StoryPanel
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StoryTopBar
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StorybookScreen
import com.ruqiazaitoon.sentenceadventurekids.ui.screens.journey.readingWorlds
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsBrightOrange
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsDeepBlue
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsInk

@Composable
fun ProgressMapScreen(
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
                title = "Adventure Map",
                subtitle = "Open worlds as you finish reading cards",
                onBack = onBack
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(readingWorlds) { index, world ->
                    val isUnlocked = world.id in unlockedLevels
                    StoryPanel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = isUnlocked) { onLevelSelect(world.id) },
                        color = if (isUnlocked) Color.White.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.54f),
                        borderColor = if (isUnlocked) world.color else KidsDeepBlue.copy(alpha = 0.16f)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "${index + 1}",
                                style = MaterialTheme.typography.displayLarge.copy(
                                    color = if (isUnlocked) KidsBrightOrange else KidsDeepBlue.copy(alpha = 0.4f),
                                    fontWeight = FontWeight.Black
                                )
                            )
                            Spacer(Modifier.width(12.dp))
                            Image(painterResource(world.art), contentDescription = null, modifier = Modifier.size(76.dp))
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(world.title, style = MaterialTheme.typography.headlineLarge.copy(color = KidsInk, fontWeight = FontWeight.Black))
                                Text(world.subtitle, style = MaterialTheme.typography.bodyLarge.copy(color = KidsDeepBlue))
                            }
                            Icon(
                                imageVector = if (isUnlocked) Icons.Default.CheckCircle else Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (isUnlocked) world.color else KidsDeepBlue.copy(alpha = 0.38f),
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
