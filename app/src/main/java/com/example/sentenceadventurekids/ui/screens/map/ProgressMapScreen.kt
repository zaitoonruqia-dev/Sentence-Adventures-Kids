package com.example.sentenceadventurekids.ui.screens.map

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sentenceadventurekids.data.ProgressRepository
import com.example.sentenceadventurekids.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressMapScreen(
    onBack: () -> Unit,
    onLevelSelect: (Int) -> Unit
) {
    val unlockedLevels by ProgressRepository.unlockedLevels.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Adventure Map", style = MaterialTheme.typography.headlineLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = KidsSkyBlue)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Brush.verticalGradient(listOf(KidsSkyBlue, KidsGrassGreen)))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                MapNode(level = 3, isUnlocked = 3 in unlockedLevels, label = "Dino Island", onClick = { onLevelSelect(3) })
                MapNode(level = 2, isUnlocked = 2 in unlockedLevels, label = "Jungle", onClick = { onLevelSelect(2) })
                MapNode(level = 1, isUnlocked = 1 in unlockedLevels, label = "Farm", onClick = { onLevelSelect(1) })
            }
        }
    }
}

@Composable
fun MapNode(level: Int, isUnlocked: Boolean, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(if (isUnlocked) KidsSunnyYellow else Color.Gray)
                .border(4.dp, KidsDeepBlue, CircleShape)
                .clickable(enabled = isUnlocked) { onClick() },
            contentAlignment = Alignment.Center
        ) {
            if (isUnlocked) {
                Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(50.dp), tint = KidsDeepBlue)
            } else {
                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(50.dp), tint = Color.White)
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = KidsDeepBlue
            )
        )
    }
}
