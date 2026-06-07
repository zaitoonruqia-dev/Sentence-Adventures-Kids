package com.example.sentenceadventurekids.ui.screens.journey

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sentenceadventurekids.data.ProgressRepository
import com.example.sentenceadventurekids.data.SentenceRepository
import com.example.sentenceadventurekids.model.AnimationType
import com.example.sentenceadventurekids.model.Sentence
import com.example.sentenceadventurekids.ui.components.SentenceReader
import com.example.sentenceadventurekids.ui.theme.KidsCloudWhite
import com.example.sentenceadventurekids.ui.theme.KidsSkyBlue
import com.example.sentenceadventurekids.ui.theme.KidsDeepBlue
import com.example.sentenceadventurekids.ui.theme.KidsMagicPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingLevelScreen(
    levelId: Int,
    onBack: () -> Unit,
    onPlayGame: (String) -> Unit
) {
    val repository = remember { SentenceRepository() }
    var sentences by remember { mutableStateOf<List<Sentence>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    
    LaunchedEffect(levelId) {
        isLoading = true
        sentences = repository.fetchSentencesForLevel(levelId)
        isLoading = false
    }
    
    var currentSentenceIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Level $levelId") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(KidsSkyBlue, KidsCloudWhite)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = KidsDeepBlue)
            } else if (sentences.isNotEmpty()) {
                val currentSentence = sentences[currentSentenceIndex]
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Animation Area
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(250.dp),
                        color = Color.White.copy(alpha = 0.4f),
                        shape = MaterialTheme.shapes.extraLarge,
                        border = androidx.compose.foundation.BorderStroke(4.dp, KidsDeepBlue.copy(alpha = 0.2f))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = when(currentSentence.animationType) {
                                        AnimationType.RUN -> Icons.Default.DirectionsRun
                                        AnimationType.JUMP -> Icons.Default.KeyboardArrowUp
                                        AnimationType.SLEEP -> Icons.Default.Bedtime
                                        AnimationType.EAT -> Icons.Default.Restaurant
                                        AnimationType.HAPPY -> Icons.Default.SentimentVerySatisfied
                                        AnimationType.SMILE -> Icons.Default.SentimentSatisfiedAlt
                                        else -> Icons.Default.Face
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(100.dp),
                                    tint = KidsDeepBlue
                                )
                                Text(
                                    text = "Watch the ${currentSentence.words.lastOrNull()}!",
                                    style = MaterialTheme.typography.titleLarge.copy(color = KidsDeepBlue)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    SentenceReader(
                        sentence = currentSentence,
                        onWordTap = { /* Play word audio */ }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { onPlayGame(Uri.encode(currentSentence.text)) },
                        colors = ButtonDefaults.buttonColors(containerColor = KidsMagicPurple),
                        modifier = Modifier.size(width = 200.dp, height = 60.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Build, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Build It!")
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            if (currentSentenceIndex < sentences.size - 1) {
                                currentSentenceIndex++
                            } else {
                                ProgressRepository.completeLevel(levelId)
                                onBack()
                            }
                        },
                        modifier = Modifier.size(width = 200.dp, height = 60.dp)
                    ) {
                        Text(if (currentSentenceIndex < sentences.size - 1) "Next" else "Finish")
                    }
                }
            } else {
                Text("Failed to load sentences. Please try again.")
            }
        }
    }
}
