package com.ruqiazaitoon.sentenceadventurekids.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ruqiazaitoon.sentenceadventurekids.R
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsBrightOrange
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsCloudWhite
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsCoral
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsDeepBlue
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsGrassGreen
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsInk
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsLilac
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsMint
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsPanel
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsSkyBlue
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsSoftPink
import com.ruqiazaitoon.sentenceadventurekids.ui.theme.KidsSunnyYellow

@Composable
fun StorybookScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(18.dp),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(KidsSkyBlue, KidsCloudWhite, KidsPanel)))
            .systemBarsPadding()
            .padding(contentPadding)
    ) {
        StoryClouds()
        content()
    }
}

@Composable
fun StoryTopBar(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(Color.White.copy(alpha = 0.82f))
            .border(2.dp, Color.White, RoundedCornerShape(26.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = KidsDeepBlue)
            }
        } else {
            Image(
                painter = painterResource(R.drawable.ic_story_mascot),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = KidsInk,
                    fontWeight = FontWeight.Black
                )
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyLarge.copy(color = KidsDeepBlue)
                )
            }
        }
        trailing?.invoke()
    }
}

@Composable
fun StoryPanel(
    modifier: Modifier = Modifier,
    color: Color = Color.White.copy(alpha = 0.86f),
    borderColor: Color = Color.White,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        border = BorderStroke(2.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.padding(18.dp)) {
            content()
        }
    }
}

@Composable
fun StoryActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = KidsSunnyYellow,
    textColor: Color = KidsInk,
    height: Dp = 60.dp
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = textColor),
        shape = RoundedCornerShape(22.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 7.dp),
        modifier = modifier.height(height)
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(10.dp))
        Text(text, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black))
    }
}

@Composable
fun StatChip(
    icon: ImageVector,
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.86f),
        border = BorderStroke(2.dp, color.copy(alpha = 0.55f)),
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = color)
            Spacer(Modifier.width(6.dp))
            Text("$value $label", color = KidsInk, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FloatingStoryArt(
    @DrawableRes resId: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 150.dp
) {
    val transition = rememberInfiniteTransition(label = "floating-art")
    val bob by transition.animateFloat(
        initialValue = 0f,
        targetValue = -12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bob"
    )
    Image(
        painter = painterResource(resId),
        contentDescription = contentDescription,
        modifier = modifier
            .offset(y = bob.dp)
            .size(size)
    )
}

@Composable
fun CelebrationDialog(
    title: String,
    message: String,
    buttonText: String,
    onDismiss: () -> Unit,
    accent: Color = KidsGrassGreen
) {
    Dialog(onDismissRequest = onDismiss) {
        StoryPanel(color = KidsPanel, borderColor = accent) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Surface(
                        modifier = Modifier.size(96.dp),
                        shape = CircleShape,
                        color = accent.copy(alpha = 0.22f)
                    ) {}
                    Image(
                        painter = painterResource(R.drawable.ic_story_treasure),
                        contentDescription = null,
                        modifier = Modifier.size(92.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = KidsBrightOrange)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        title,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = KidsInk,
                            fontWeight = FontWeight.Black
                        ),
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    message,
                    style = MaterialTheme.typography.bodyLarge.copy(color = KidsDeepBlue),
                    textAlign = TextAlign.Center
                )
                StoryActionButton(
                    text = buttonText,
                    icon = Icons.Default.Star,
                    onClick = onDismiss,
                    color = accent,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun BoxScope.StoryClouds() {
    Surface(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .offset(x = 52.dp, y = 56.dp)
            .size(116.dp),
        shape = CircleShape,
        color = Color.White.copy(alpha = 0.38f)
    ) {}
    Surface(
        modifier = Modifier
            .align(Alignment.BottomStart)
            .offset(x = (-36).dp, y = (-42).dp)
            .size(140.dp),
        shape = CircleShape,
        color = KidsLilac.copy(alpha = 0.34f)
    ) {}
    Surface(
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .offset(x = 80.dp)
            .size(130.dp),
        shape = CircleShape,
        color = KidsMint.copy(alpha = 0.22f)
    ) {}
    Surface(
        modifier = Modifier
            .align(Alignment.TopStart)
            .offset(x = (-52).dp, y = 120.dp)
            .size(96.dp),
        shape = CircleShape,
        color = KidsCoral.copy(alpha = 0.2f)
    ) {}
}
