package com.example.sentenceadventurekids.ui.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sentenceadventurekids.data.ProgressRepository
import com.example.sentenceadventurekids.ui.theme.*

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(KidsSkyBlue, KidsCloudWhite)
                )
            )
    ) {
        // Star Counter at top right
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            color = Color.White.copy(alpha = 0.8f),
            shape = MaterialTheme.shapes.large,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Star, contentDescription = null, tint = KidsSunnyYellow)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stars.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = KidsDeepBlue
                    )
                )
            }
        }

        // Animated Clouds/Stars (simplified for now)
        AnimatedBackgroundElements()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Sentence Adventure",
                style = MaterialTheme.typography.displayLarge.copy(
                    color = KidsDeepBlue,
                    fontWeight = FontWeight.Black
                )
            )

            // Daily Streak
            Surface(
                color = KidsSunnyYellow.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Red)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "${unlockedLevels.size} Adventure${if (unlockedLevels.size > 1) "s" else ""} Started!", 
                        fontWeight = FontWeight.Bold, 
                        color = KidsDeepBlue
                    )
                }
            }

            // Mascot Placeholder
            MascotCharacter(modifier = Modifier.size(200.dp))

            // Main Play Button
            LargeRoundedButton(
                text = "PLAY",
                onClick = onPlayClick,
                color = KidsSunnyYellow,
                icon = Icons.Default.PlayArrow
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SmallRoundedButton(
                    text = "Map",
                    onClick = onMapClick,
                    color = KidsGrassGreen,
                    icon = Icons.Default.Place
                )
                SmallRoundedButton(
                    text = "Rewards",
                    onClick = onRewardsClick,
                    color = KidsSoftPink,
                    icon = Icons.Default.Star
                )
            }

            SmallRoundedButton(
                text = "Parents",
                onClick = onParentClick,
                color = KidsBrightOrange,
                icon = Icons.Default.Settings
            )
        }
    }
}

@Composable
fun AnimatedBackgroundElements() {
    val infiniteTransition = rememberInfiniteTransition(label = "stars")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "twinkle"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = KidsSunnyYellow.copy(alpha = alpha),
            modifier = Modifier.padding(top = 40.dp, start = 30.dp).size(60.dp)
        )
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = KidsSunnyYellow.copy(alpha = alpha),
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 80.dp, end = 50.dp).size(80.dp)
        )
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = KidsSunnyYellow.copy(alpha = alpha),
            modifier = Modifier.align(Alignment.BottomStart).padding(bottom = 100.dp, start = 60.dp).size(40.dp)
        )
    }
}

@Composable
fun MascotCharacter(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "mascot")
    val dy by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -20f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Surface(
        modifier = modifier.offset(y = dy.dp),
        color = KidsMagicPurple,
        shape = MaterialTheme.shapes.large
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Default.Face,
                contentDescription = "Mascot",
                modifier = Modifier.size(100.dp),
                tint = Color.White
            )
        }
    }
}

@Composable
fun LargeRoundedButton(
    text: String,
    onClick: () -> Unit,
    color: Color,
    icon: ImageVector
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = MaterialTheme.shapes.extraLarge,
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(80.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = KidsDeepBlue, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = KidsDeepBlue,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun SmallRoundedButton(
    text: String,
    onClick: () -> Unit,
    color: Color,
    icon: ImageVector
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .width(140.dp)
            .height(60.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = KidsDeepBlue)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = KidsDeepBlue,
                    fontSize = 16.sp
                )
            )
        }
    }
}
