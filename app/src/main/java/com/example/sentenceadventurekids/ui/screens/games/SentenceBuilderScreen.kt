package com.example.sentenceadventurekids.ui.screens.games

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sentenceadventurekids.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentenceBuilderScreen(
    sentence: String,
    onBack: () -> Unit,
    onComplete: () -> Unit
) {
    val cleanSentence = remember { sentence.trim().removeSuffix(".") }
    val correctWords = remember { cleanSentence.split(" ") }
    val shuffledWords = remember { correctWords.shuffled().toMutableStateList() }
    val currentWords = remember { mutableStateListOf<String>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sentence Builder") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(KidsMagicPurple, KidsCloudWhite)
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Build the sentence!",
                style = MaterialTheme.typography.headlineLarge.copy(color = KidsDeepBlue)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Current built sentence
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = 16.dp)
                    .background(Color.White.copy(alpha = 0.5f), MaterialTheme.shapes.medium),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                currentWords.forEachIndexed { index, word ->
                    WordTile(
                        word = word,
                        onClick = {
                            currentWords.removeAt(index)
                            shuffledWords.add(word)
                        },
                        color = KidsSunnyYellow
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Available words
            FlowRow(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                maxItemsInEachRow = 3
            ) {
                shuffledWords.forEach { word ->
                    WordTile(
                        word = word,
                        onClick = {
                            shuffledWords.remove(word)
                            currentWords.add(word)
                            if (currentWords.toList() == correctWords) {
                                onComplete()
                            }
                        },
                        color = KidsSkyBlue
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        maxItemsInEachRow = maxItemsInEachRow
    ) {
        content()
    }
}

@Composable
fun WordTile(
    word: String,
    onClick: () -> Unit,
    color: Color
) {
    Surface(
        onClick = onClick,
        color = color,
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp,
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = word,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.titleLarge.copy(
                color = KidsDeepBlue,
                fontWeight = FontWeight.Bold
            )
        )
    }
}
