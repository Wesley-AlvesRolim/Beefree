package com.wesley.beefree.ui.components.checkin.daily

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.wesley.beefree.domain.checkin.VideoWatchStep
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import kotlinx.coroutines.delay

@Composable
fun VideoWatchStepContent(
    step: VideoWatchStep,
    onWatched: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val videoId = remember(step.videoUrl) { extractYouTubeVideoId(step.videoUrl) }
    var playbackPosition by rememberSaveable { mutableFloatStateOf(0f) }
    var hasPlayedEnough by remember { mutableStateOf(false) }

    LaunchedEffect(playbackPosition) {
        if (playbackPosition >= 10f) {
            hasPlayedEnough = true
        }
    }

    LaunchedEffect(hasPlayedEnough) {
        if (hasPlayedEnough) {
            delay(500)
            onWatched()
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        BeeHeadlineSmall(stringByName(step.titleKey))
        Spacer(modifier = Modifier.height(BeeSpacing.S))
        BeeBodyMedium(
            text = stringByName(step.subtitleKey),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.M))

        if (videoId != null) {
            YouTubePlayerViewComponent(
                videoId = videoId,
                initialPosition = playbackPosition,
                onCurrentSecondChanged = { playbackPosition = it },
            )
        }
    }
}

@Composable
private fun YouTubePlayerViewComponent(
    videoId: String,
    initialPosition: Float,
    onCurrentSecondChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
        factory = { context ->
            YouTubePlayerView(context).apply {
                lifecycleOwner.lifecycle.addObserver(this)

                addYouTubePlayerListener(
                    object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.loadVideo(videoId, initialPosition)
                        }

                        override fun onCurrentSecond(
                            youTubePlayer: YouTubePlayer,
                            second: Float,
                        ) {
                            onCurrentSecondChanged(second)
                        }
                    },
                )
            }
        },
    )
}

private fun extractYouTubeVideoId(url: String): String? {
    val pattern = Regex("""(?:youtube\.com/watch\?v=|youtu\.be/|youtube\.com/embed/)([a-zA-Z0-9_-]{11})""")
    return pattern.find(url)?.groupValues?.getOrNull(1)
}
