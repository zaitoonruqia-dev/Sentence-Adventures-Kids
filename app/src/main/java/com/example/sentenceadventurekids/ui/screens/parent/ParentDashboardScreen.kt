package com.ruqiazaitoon.sentenceadventurekids.ui.screens.parent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ruqiazaitoon.sentenceadventurekids.ads.FriendlyBannerAd
import com.ruqiazaitoon.sentenceadventurekids.data.ProgressRepository
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StatChip
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StoryActionButton
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StoryPanel
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StoryTopBar
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StorybookScreen
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsCoral
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsDeepBlue
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsGrassGreen
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsInk
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsMagicPurple
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsPanel
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsSunnyYellow

@Composable
fun ParentDashboardScreen(onBack: () -> Unit) {
    val stars by ProgressRepository.stars.collectAsState()
    val sentencesRead by ProgressRepository.sentencesRead.collectAsState()
    val unlockedLevels by ProgressRepository.unlockedLevels.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }

    StorybookScreen {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StoryTopBar(
                title = "Parent Corner",
                subtitle = "Quiet controls and progress at a glance",
                onBack = onBack
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item { SectionHeader(icon = Icons.Default.Analytics, title = "Reading Progress") }
                item {
                    StoryPanel(modifier = Modifier.fillMaxWidth(), borderColor = KidsSunnyYellow) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            StatChip(Icons.Default.Analytics, "stars earned", stars.toString(), KidsSunnyYellow)
                            StatChip(Icons.Default.Timer, "sentences read", sentencesRead.toString(), KidsGrassGreen)
                            StatChip(Icons.Default.Lock, "worlds open", "${unlockedLevels.size}/4", KidsMagicPurple)
                        }
                    }
                }

                item { SectionHeader(icon = Icons.Default.Timer, title = "Learning Controls") }
                item {
                    SettingsToggle(
                        label = "Daily reading goal",
                        description = "Keep practice gentle with a 10 minute target.",
                        initialValue = true
                    )
                }
                item {
                    SettingsToggle(
                        label = "Parent lock",
                        description = "Require grown-up help before changing settings.",
                        initialValue = false
                    )
                }
                item {
                    StoryActionButton(
                        text = "Reset Progress",
                        icon = Icons.Default.DeleteForever,
                        onClick = { showResetDialog = true },
                        color = KidsCoral,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            FriendlyBannerAd()
        }
    }

    if (showResetDialog) {
        ResetProgressDialog(
            onCancel = { showResetDialog = false },
            onConfirm = {
                ProgressRepository.resetAll()
                showResetDialog = false
            }
        )
    }
}

@Composable
fun SectionHeader(icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = KidsDeepBlue)
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Black,
                color = KidsInk
            )
        )
    }
}

@Composable
fun SettingsToggle(label: String, description: String, initialValue: Boolean) {
    var checked by remember { mutableStateOf(initialValue) }

    StoryPanel(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = KidsInk))
                Text(description, style = MaterialTheme.typography.bodyLarge.copy(color = KidsDeepBlue))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Switch(
                checked = checked,
                onCheckedChange = { checked = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = KidsGrassGreen,
                    checkedTrackColor = KidsGrassGreen.copy(alpha = 0.35f)
                )
            )
        }
    }
}

@Composable
private fun ResetProgressDialog(
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onCancel) {
        StoryPanel(color = KidsPanel, borderColor = KidsCoral) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Icon(Icons.Default.RestartAlt, contentDescription = null, tint = KidsCoral)
                Text(
                    "Reset Progress?",
                    style = MaterialTheme.typography.headlineLarge.copy(color = KidsInk, fontWeight = FontWeight.Black),
                    textAlign = TextAlign.Center
                )
                Text(
                    "This returns stars, badges, and unlocked worlds to the starting point.",
                    style = MaterialTheme.typography.bodyLarge.copy(color = KidsDeepBlue),
                    textAlign = TextAlign.Center
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StoryActionButton(
                        text = "Keep",
                        icon = Icons.Default.Lock,
                        onClick = onCancel,
                        color = KidsGrassGreen,
                        modifier = Modifier.weight(1f)
                    )
                    StoryActionButton(
                        text = "Reset",
                        icon = Icons.Default.DeleteForever,
                        onClick = onConfirm,
                        color = KidsCoral,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
