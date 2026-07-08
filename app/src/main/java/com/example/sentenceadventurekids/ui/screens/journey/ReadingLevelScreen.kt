package com.ruqiazaitoon.sentenceadventurekids.ui.screens.journey

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.ViewColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ruqiazaitoon.sentenceadventurekids.R
import com.ruqiazaitoon.sentenceadventurekids.ads.AdsController
import com.ruqiazaitoon.sentenceadventurekids.ads.findActivity
import com.ruqiazaitoon.sentenceadventurekids.data.ProgressRepository
import com.ruqiazaitoon.sentenceadventurekids.data.SentenceRepository
import com.ruqiazaitoon.sentenceadventurekids.model.AnimationType
import com.ruqiazaitoon.sentenceadventurekids.model.Sentence
import com.ruqiazaitoon.sentenceadventurekids.ui.components.CelebrationDialog
import com.ruqiazaitoon.sentenceadventurekids.ui.components.SentenceReader
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StoryActionButton
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StoryPanel
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StoryTopBar
import com.ruqiazaitoon.sentenceadventurekids.ui.components.StorybookScreen
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsBrightOrange
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsCoral
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsDeepBlue
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsGrassGreen
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsInk
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsMagicPurple
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsMint
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsPanel
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsSunnyYellow

private enum class LessonStep {
    READ, TAP, BUILD
}

@Composable
fun ReadingLevelScreen(
    levelId: Int,
    onBack: () -> Unit,
    onPlayGame: (String) -> Unit
) {
    val repository = remember { SentenceRepository() }
    var sentences by remember { mutableStateOf<List<Sentence>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var currentSentenceIndex by remember(levelId) { mutableIntStateOf(0) }
    var lessonStep by remember(levelId) { mutableStateOf(LessonStep.READ) }
    var showCompleteDialog by remember { mutableStateOf(false) }
    var reloadToken by remember { mutableIntStateOf(0) }
    val activity = LocalContext.current.findActivity()
    val world = readingWorlds.firstOrNull { it.id == levelId } ?: readingWorlds.first()

    LaunchedEffect(levelId, reloadToken) {
        isLoading = true
        sentences = repository.fetchSentencesForLevel(levelId)
        currentSentenceIndex = 0
        lessonStep = LessonStep.READ
        isLoading = false
    }

    StorybookScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StoryTopBar(
                title = world.title,
                subtitle = "Sentence ${if (sentences.isEmpty()) 1 else currentSentenceIndex + 1} of 3",
                onBack = onBack
            )

            if (isLoading) {
                LoadingCard(world.art, world.color)
            } else if (sentences.isNotEmpty()) {
                val currentSentence = sentences[currentSentenceIndex]
                LessonProgress(step = lessonStep)
                SentenceStage(sentence = currentSentence, art = actionArt(currentSentence.animationType, world.art), color = world.color)

                when (lessonStep) {
                    LessonStep.READ -> ReadPractice(
                        sentence = currentSentence,
                        onReady = { lessonStep = LessonStep.TAP }
                    )
                    LessonStep.TAP -> TapWordsPractice(
                        words = currentSentence.words,
                        onComplete = { lessonStep = LessonStep.BUILD }
                    )
                    LessonStep.BUILD -> BuildSentencePractice(
                        words = currentSentence.words,
                        onComplete = {
                            if (currentSentenceIndex < sentences.lastIndex) {
                                currentSentenceIndex++
                                lessonStep = LessonStep.READ
                            } else {
                                ProgressRepository.completeLevel(levelId)
                                showCompleteDialog = true
                            }
                        },
                        isLastSentence = currentSentenceIndex == sentences.lastIndex
                    )
                }
            } else {
                StoryPanel(modifier = Modifier.fillMaxWidth(), borderColor = KidsCoral) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("The story cards hid for a moment.", style = MaterialTheme.typography.headlineLarge.copy(color = KidsInk))
                        StoryActionButton(
                            text = "Try Again",
                            icon = Icons.Default.Refresh,
                            onClick = {
                                reloadToken++
                                showCompleteDialog = false
                            },
                            color = KidsCoral,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

    if (showCompleteDialog) {
        CelebrationDialog(
            title = "World Complete!",
            message = "You read, tapped, and built every sentence in this world.",
            buttonText = "Back to Worlds",
            accent = world.color,
            onDismiss = {
                val currentActivity = activity
                if (currentActivity != null) {
                    AdsController.showWorldCompleteInterstitial(currentActivity, onDone = onBack)
                } else {
                    onBack()
                }
            }
        )
    }
}

@Composable
private fun LoadingCard(art: Int, color: Color) {
    StoryPanel(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(painterResource(art), contentDescription = null, modifier = Modifier.size(150.dp))
            CircularProgressIndicator(color = color)
            Text("Finding fresh sentences...", style = MaterialTheme.typography.titleLarge.copy(color = KidsInk))
        }
    }
}

@Composable
private fun LessonProgress(step: LessonStep) {
    val steps = listOf(
        LessonStep.READ to "Read",
        LessonStep.TAP to "Tap",
        LessonStep.BUILD to "Build"
    )
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        steps.forEach { (itemStep, label) ->
            val active = itemStep == step
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp),
                color = if (active) KidsSunnyYellow else Color.White.copy(alpha = 0.72f),
                border = BorderStroke(2.dp, if (active) KidsBrightOrange else Color.White)
            ) {
                Text(
                    text = label,
                    modifier = Modifier.padding(vertical = 9.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = KidsInk,
                        fontWeight = FontWeight.Black
                    )
                )
            }
        }
    }
}

@Composable
private fun SentenceStage(sentence: Sentence, art: Int, color: Color) {
    StoryPanel(
        modifier = Modifier.fillMaxWidth(),
        color = KidsPanel,
        borderColor = color
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                painter = painterResource(art),
                contentDescription = null,
                modifier = Modifier.size(132.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, tint = KidsBrightOrange)
                Spacer(Modifier.size(8.dp))
                Text(
                    actionPrompt(sentence),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = KidsDeepBlue,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ReadPractice(sentence: Sentence, onReady: () -> Unit) {
    StoryPanel(modifier = Modifier.fillMaxWidth(), borderColor = KidsSunnyYellow) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Read with the glowing words.",
                style = MaterialTheme.typography.titleLarge.copy(color = KidsInk, fontWeight = FontWeight.Black),
                textAlign = TextAlign.Center
            )
            SentenceReader(sentence = sentence, onWordTap = { }, modifier = Modifier.fillMaxWidth())
            StoryActionButton(
                text = "I Read It",
                icon = Icons.Default.CheckCircle,
                onClick = onReady,
                color = KidsGrassGreen,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TapWordsPractice(words: List<String>, onComplete: () -> Unit) {
    var nextIndex by remember(words) { mutableIntStateOf(0) }
    var message by remember(words) { mutableStateOf("Tap the words from left to right.") }

    StoryPanel(modifier = Modifier.fillMaxWidth(), borderColor = KidsMint) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                message,
                style = MaterialTheme.typography.titleLarge.copy(color = KidsInk, fontWeight = FontWeight.Black),
                textAlign = TextAlign.Center
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                words.forEachIndexed { index, word ->
                    val done = index < nextIndex
                    PracticeTile(
                        word = word,
                        color = if (done) KidsGrassGreen else Color.White,
                        onClick = {
                            if (index == nextIndex) {
                                nextIndex++
                                message = if (nextIndex == words.size) "Great! Now build the sentence." else "Nice. Find the next word."
                                if (nextIndex == words.size) onComplete()
                            } else if (!done) {
                                message = "Try '${words[nextIndex]}' next."
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BuildSentencePractice(
    words: List<String>,
    onComplete: () -> Unit,
    isLastSentence: Boolean
) {
    val shuffledWords = remember(words) { words.shuffled().toMutableStateList() }
    val builtWords = remember(words) { mutableStateListOf<String>() }
    var message by remember(words) { mutableStateOf("Build the whole sentence in order.") }

    StoryPanel(modifier = Modifier.fillMaxWidth(), borderColor = KidsMagicPurple) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                message,
                style = MaterialTheme.typography.titleLarge.copy(color = KidsInk, fontWeight = FontWeight.Black),
                textAlign = TextAlign.Center
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (builtWords.isEmpty()) {
                    Text("Your sentence will appear here.", style = MaterialTheme.typography.bodyLarge.copy(color = KidsDeepBlue))
                } else {
                    builtWords.forEachIndexed { index, word ->
                        PracticeTile(
                            word = word,
                            color = KidsSunnyYellow,
                            onClick = {
                                builtWords.removeAt(index)
                                shuffledWords.add(word)
                                message = "Keep building the sentence."
                            }
                        )
                    }
                }
            }
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                shuffledWords.forEach { word ->
                    PracticeTile(
                        word = word,
                        color = KidsMint,
                        onClick = {
                            shuffledWords.remove(word)
                            builtWords.add(word)
                            message = "Keep going."
                        }
                    )
                }
            }
            val sentenceIsCorrect = builtWords.toList() == words
            StoryActionButton(
                text = if (sentenceIsCorrect) {
                    if (isLastSentence) "Finish World" else "Next Sentence"
                } else {
                    "Check Sentence"
                },
                icon = if (sentenceIsCorrect && isLastSentence) Icons.Default.Celebration else Icons.Default.ViewColumn,
                onClick = {
                    if (sentenceIsCorrect) {
                        onComplete()
                    } else {
                        message = "Check the order. Tap a yellow word to move it back."
                    }
                },
                color = if (sentenceIsCorrect) KidsGrassGreen else KidsMagicPurple,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PracticeTile(
    word: String,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(4.dp)
            .clickable { onClick() },
        color = color,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(2.dp, Color.White),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.TouchApp, contentDescription = null, tint = KidsDeepBlue, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
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

private fun actionArt(animationType: AnimationType, fallback: Int): Int {
    return when (animationType) {
        AnimationType.RUN, AnimationType.JUMP -> R.drawable.ic_story_planet
        AnimationType.HAPPY, AnimationType.SMILE -> R.drawable.ic_story_mascot
        AnimationType.EAT -> R.drawable.ic_story_treasure
        AnimationType.SLEEP -> R.drawable.ic_story_castle
        AnimationType.NONE -> fallback
    }
}

private fun actionPrompt(sentence: Sentence): String {
    return when (sentence.animationType) {
        AnimationType.RUN -> "Read it with quick feet"
        AnimationType.JUMP -> "Read it with a big bounce"
        AnimationType.SLEEP -> "Read it in a soft sleepy voice"
        AnimationType.HAPPY, AnimationType.SMILE -> "Read it with a smile"
        AnimationType.EAT -> "Read it like snack time"
        AnimationType.NONE -> "Watch, say, tap, then build"
    }
}
