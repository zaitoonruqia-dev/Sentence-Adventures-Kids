package com.example.sentenceadventurekids.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sentenceadventurekids.model.Sentence
import com.example.sentenceadventurekids.ui.theme.KidsDeepBlue
import com.example.sentenceadventurekids.ui.theme.KidsMagicPurple
import kotlinx.coroutines.delay

@Composable
fun SentenceReader(
    sentence: Sentence,
    onWordTap: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var highlightedWordIndex by remember { mutableIntStateOf(-1) }

    // Simulation of auto-reading
    LaunchedEffect(sentence) {
        for (i in sentence.words.indices) {
            highlightedWordIndex = i
            delay(1000) // Simulate word duration
        }
        highlightedWordIndex = -1
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        sentence.words.forEachIndexed { index, word ->
            WordView(
                word = word,
                isHighlighted = index == highlightedWordIndex,
                onTap = { onWordTap(word) }
            )
            if (index < sentence.words.size - 1) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun WordView(
    word: String,
    isHighlighted: Boolean,
    onTap: () -> Unit
) {
    val color by animateColorAsState(
        targetValue = if (isHighlighted) KidsMagicPurple else KidsDeepBlue,
        label = "color"
    )
    val scale by animateFloatAsState(
        targetValue = if (isHighlighted) 1.2f else 1.0f,
        label = "scale"
    )

    Text(
        text = word,
        style = MaterialTheme.typography.displayLarge.copy(
            color = color,
            fontSize = 40.sp,
            fontWeight = if (isHighlighted) FontWeight.ExtraBold else FontWeight.Bold
        ),
        modifier = Modifier
            .clickable { onTap() }
            .padding(4.dp)
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
    )
}
