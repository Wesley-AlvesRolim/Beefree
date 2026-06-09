package com.wesley.beefree.ui.screens.emotionalrecord

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeButtonPrimary
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineMedium
import com.wesley.beefree.ui.components.designsystem.BeeLabelLarge
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeech
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeechTone
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@Composable
fun EmotionalRecordDoneStep(
    emotions: Map<FeelingType, Float>,
    onDone: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth()
                    .padding(horizontal = BeeSpacing.L),
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
        ) {
            item {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = BeeSpacing.M),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
                ) {
                    BeeHeadlineMedium(text = stringResource(R.string.emotional_record_done_title))
                    BeeMascotSpeech(
                        speechText = stringResource(R.string.emotional_record_done_motivational),
                        tone = BeeMascotSpeechTone.Primary,
                    )
                    BeeBodySmall(
                        text = stringResource(R.string.emotional_record_done_message),
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }

            item { EmotionalRecordSummaryCard(emotions = emotions) }
        }

        BeeButtonPrimary(
            onClick = onDone,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(BeeSpacing.L),
        ) {
            BeeLabelLarge(
                text = stringResource(R.string.emotional_record_done_button),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}
