package com.example.sentenceadventurekids.ui.screens.journey

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sentenceadventurekids.data.ProgressRepository
import com.example.sentenceadventurekids.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JourneyScreen(
    onBack: () -> Unit,
    onLevelSelect: (Int) -> Unit
) {
    val unlockedLevels by ProgressRepository.unlockedLevels.collectAsState()
    
    val levels = listOf(
        "Level 1: Two-word sentences",
        "Level 2: Three-word sentences",
        "Level 3: Simple complete sentences",
        "Level 4: Short stories"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Reading Journey",
                        style = MaterialTheme.typography.headlineLarge.copy(color = KidsDeepBlue)
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = KidsDeepBlue)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = KidsSkyBlue
                )
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
                )
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(levels.indices.toList()) { index ->
                    val levelId = index + 1
                    val isUnlocked = levelId in unlockedLevels
                    LevelCard(
                        title = levels[index],
                        isUnlocked = isUnlocked,
                        onClick = { if (isUnlocked) onLevelSelect(levelId) },
                        color = when(index) {
                            0 -> KidsGrassGreen
                            1 -> KidsSunnyYellow
                            2 -> KidsSoftPink
                            else -> KidsMagicPurple
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LevelCard(
    title: String,
    isUnlocked: Boolean,
    onClick: () -> Unit,
    color: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(enabled = isUnlocked) { onClick() },
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) color else Color.Gray.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 6.dp else 0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!isUnlocked) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = KidsDeepBlue.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = if (isUnlocked) KidsDeepBlue else KidsDeepBlue.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                )
            }
        }
    }
}
