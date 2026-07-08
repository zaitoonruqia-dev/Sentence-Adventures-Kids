package com.ruqiazaitoon.sentenceadventurekids.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ruqiazaitoon.sentenceadventurekids.R
import com.ruqiazaitoon.sentenceadventurekids.data.ProgressRepository
import com.ruqiazaitoon.sentenceadventurekids.ui.components.FloatingStoryArt
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StatChip
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StoryActionButton
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

@Composable
fun HomeScreen(
    onPlayClick: () -> Unit,
    onRewardsClick: () -> Unit,
    onParentClick: () -> Unit,
    onMapClick: () -> Unit
) {
    val stars by ProgressRepository.stars.collectAsState()
    val unlockedLevels by ProgressRepository.unlockedLevels.collectAsState()
    val sentencesRead by ProgressRepository.sentencesRead.collectAsState()

    StorybookScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            StoryTopBar(
                title = "Sentence Adventure",
                subtitle = "Read, tap, build, then unlock story worlds"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatChip(Icons.Default.Star, "stars", stars.toString(), KidsSunnyYellow, Modifier.weight(1f))
                StatChip(Icons.AutoMirrored.Filled.MenuBook, "read", sentencesRead.toString(), KidsDeepBlue, Modifier.weight(1f))
            }

            StoryPanel(modifier = Modifier.fillMaxWidth()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FloatingStoryArt(
                        resId = R.drawable.ic_story_mascot,
                        contentDescription = "Sentence Adventure mascot",
                        size = 178.dp
                    )
                    Text(
                        "Pick a world. For each sentence, read it, tap the words in order, then rebuild it.",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = KidsInk,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "${unlockedLevels.size} of 4 worlds open",
                        style = MaterialTheme.typography.bodyLarge.copy(color = KidsDeepBlue),
                        textAlign = TextAlign.Center
                    )
                }
            }

            StoryActionButton(
                text = "Start Reading",
                icon = Icons.Default.PlayArrow,
                onClick = onPlayClick,
                color = KidsSunnyYellow,
                modifier = Modifier.fillMaxWidth(),
                height = 72.dp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StoryActionButton(
                    text = "Map",
                    icon = Icons.Default.Map,
                    onClick = onMapClick,
                    color = KidsGrassGreen,
                    modifier = Modifier.weight(1f)
                )
                StoryActionButton(
                    text = "Rewards",
                    icon = Icons.Default.WorkspacePremium,
                    onClick = onRewardsClick,
                    color = KidsSoftPink,
                    modifier = Modifier.weight(1f)
                )
            }

            StoryActionButton(
                text = "Parent Corner",
                icon = Icons.Default.FamilyRestroom,
                onClick = onParentClick,
                color = KidsMagicPurple,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(4.dp))
            Image(
                painter = painterResource(R.drawable.ic_story_book),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 180.dp, height = 120.dp)
                    .padding(bottom = 8.dp)
            )
        }
    }
}
