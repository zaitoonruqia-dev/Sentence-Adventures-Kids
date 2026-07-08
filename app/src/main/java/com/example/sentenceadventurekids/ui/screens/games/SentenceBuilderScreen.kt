package com.ruqiazaitoon.sentenceadventurekids.ui.screens.games

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ruqiazaitoon.sentenceadventurekids.ui.components.CelebrationDialog
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StoryActionButton
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StoryPanel
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StoryTopBar
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StorybookScreen
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsCoral
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsDeepBlue
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsGrassGreen
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsInk
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsMagicPurple
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsMint
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsPanel
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsSkyBlue
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsSunnyYellow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SentenceBuilderScreen(
    sentence: String,
    onBack: () -> Unit,
    onComplete: () -> Unit
) {
    val cleanSentence = remember(sentence) {
        Uri.decode(sentence).trim().trim('.', '!', '?')
    }
    val correctWords = remember(cleanSentence) { cleanSentence.split(" ").filter { it.isNotBlank() } }
    val shuffledWords = remember(cleanSentence) { correctWords.shuffled().toMutableStateList() }
    val currentWords = remember(cleanSentence) { mutableStateListOf<String>() }
    var showSuccess by remember { mutableStateOf(false) }

    StorybookScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StoryTopBar(
                title = "Build the Sentence",
                subtitle = "Tap words in the right order",
                onBack = onBack
            )

            StoryPanel(
                modifier = Modifier.fillMaxWidth(),
                color = KidsPanel,
                borderColor = KidsMagicPurple
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        cleanSentence,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = KidsInk,
                            fontWeight = FontWeight.Black
                        ),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Remember it, then rebuild it below.",
                        style = MaterialTheme.typography.bodyLarge.copy(color = KidsDeepBlue),
                        textAlign = TextAlign.Center
                    )
                }
            }

            StoryPanel(modifier = Modifier.fillMaxWidth(), borderColor = KidsSunnyYellow) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Your sentence", style = MaterialTheme.typography.titleLarge.copy(color = KidsInk, fontWeight = FontWeight.Bold))
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(132.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (currentWords.isEmpty()) {
                            Text(
                                "Tap a word tile to start",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.titleLarge.copy(color = KidsDeepBlue)
                            )
                        } else {
                            currentWords.forEachIndexed { index, word ->
                                WordTile(
                                    word = word,
                                    onClick = {
                                        currentWords.removeAt(index)
                                        shuffledWords.add(word)
                                    },
                                    color = KidsSunnyYellow
                                )
                            }
                        }
                    }
                }
            }

            StoryPanel(modifier = Modifier.fillMaxWidth(), borderColor = KidsSkyBlue) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Word tiles", style = MaterialTheme.typography.titleLarge.copy(color = KidsInk, fontWeight = FontWeight.Bold))
                    FlowRow(
                        horizontalArrangement = Arrangement.Center,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        shuffledWords.forEach { word ->
                            WordTile(
                                word = word,
                                onClick = {
                                    shuffledWords.remove(word)
                                    currentWords.add(word)
                                    if (currentWords.toList() == correctWords) {
                                        showSuccess = true
                                    }
                                },
                                color = KidsMint
                            )
                        }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StoryActionButton(
                    text = "Undo",
                    icon = Icons.AutoMirrored.Filled.Backspace,
                    onClick = {
                        if (currentWords.isNotEmpty()) {
                            val last = currentWords.removeAt(currentWords.lastIndex)
                            shuffledWords.add(last)
                        }
                    },
                    color = KidsCoral,
                    modifier = Modifier.weight(1f)
                )
                StoryActionButton(
                    text = "Reset",
                    icon = Icons.Default.RestartAlt,
                    onClick = {
                        currentWords.clear()
                        shuffledWords.clear()
                        shuffledWords.addAll(correctWords.shuffled())
                    },
                    color = KidsGrassGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    if (showSuccess) {
        CelebrationDialog(
            title = "You Built It!",
            message = "That sentence is in the perfect order.",
            buttonText = "Collect Bonus",
            accent = KidsMagicPurple,
            onDismiss = onComplete
        )
    }
}

@Composable
fun WordTile(
    word: String,
    onClick: () -> Unit,
    color: Color
) {
    Surface(
        modifier = Modifier
            .padding(5.dp)
            .clickable { onClick() },
        color = color,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(2.dp, Color.White.copy(alpha = 0.82f)),
        shadowElevation = 5.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material3.Icon(Icons.Default.Extension, contentDescription = null, tint = KidsDeepBlue)
            Spacer(Modifier.width(8.dp))
            Text(
                text = word,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = KidsInk,
                    fontWeight = FontWeight.Black
                )
            )
        }
    }
}
