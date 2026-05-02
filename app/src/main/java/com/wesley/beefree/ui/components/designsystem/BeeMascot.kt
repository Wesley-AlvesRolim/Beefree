package com.wesley.beefree.ui.components.designsystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wesley.beefree.R

enum class BeeMascotEmotion {
    Happy,
    Sad,
    Thinking,
    Excited,
    Neutral,
}

enum class BeeMascotSize {
    Hero,
    Small,
}

private data class BeeMascotDimensions(
    val outer: Dp,
    val outerAlpha: Float,
    val inner: Dp,
    val image: Dp,
)

private fun BeeMascotSize.dimensions() =
    when (this) {
        BeeMascotSize.Hero -> BeeMascotDimensions(outer = 200.dp, outerAlpha = 0.15f, inner = 180.dp, image = 140.dp)
        BeeMascotSize.Small -> BeeMascotDimensions(outer = 70.dp, outerAlpha = 0.22f, inner = 60.dp, image = 44.dp)
    }

private fun BeeMascotEmotion.drawable() =
    when (this) {
        BeeMascotEmotion.Happy -> R.drawable.cute_bee_1
        BeeMascotEmotion.Sad -> R.drawable.cute_bee_1
        BeeMascotEmotion.Thinking -> R.drawable.cute_bee_1
        BeeMascotEmotion.Excited -> R.drawable.cute_bee_1
        BeeMascotEmotion.Neutral -> R.drawable.cute_bee_1
    }

@Composable
fun BeeMascot(
    modifier: Modifier = Modifier,
    emotion: BeeMascotEmotion = BeeMascotEmotion.Happy,
    size: BeeMascotSize = BeeMascotSize.Hero,
    contentDescription: String? = null,
) {
    val dims = size.dimensions()
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Box(
            modifier =
                Modifier
                    .size(dims.outer)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = dims.outerAlpha)),
        )
        Box(
            modifier =
                Modifier
                    .size(dims.inner)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(emotion.drawable()),
                contentDescription = contentDescription,
                modifier = Modifier.size(dims.image),
            )
        }
    }
}
