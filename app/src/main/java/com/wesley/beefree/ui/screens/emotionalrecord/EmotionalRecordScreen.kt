package com.wesley.beefree.ui.screens.emotionalrecord

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeButtonGhost
import com.wesley.beefree.ui.components.designsystem.BeeButtonPrimary
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineMedium
import com.wesley.beefree.ui.components.designsystem.BeeLabelLarge
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.EmotionalRecordStep
import com.wesley.beefree.ui.viewmodel.EmotionalRecordViewModel

@Composable
fun EmotionalRecordScreen(viewModel: EmotionalRecordViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        EmotionalRecordContent(
            step = uiState.step,
            emotions = uiState.emotions,
            isSaving = uiState.isSaving,
            onNext = viewModel::onNext,
            onDismiss = viewModel::onDone,
            onSliderChange = viewModel::onSliderChange,
            onSave = viewModel::onSave,
        )

        if (uiState.error != null) {
            AlertDialog(
                onDismissRequest = viewModel::resetError,
                title = { BeeHeadlineMedium(text = stringResource(R.string.error_title)) },
                text = { BeeBodyMedium(text = uiState.error!!) },
                confirmButton = {
                    BeeButtonGhost(onClick = viewModel::resetError) {
                        BeeLabelLarge(
                            text = stringResource(R.string.btn_ok),
                        )
                    }
                },
            )
        }
    }
}

@Composable
fun EmotionalRecordContent(
    step: EmotionalRecordStep,
    emotions: Map<FeelingType, Float>,
    isSaving: Boolean,
    onNext: () -> Unit,
    onDismiss: () -> Unit,
    onSliderChange: (FeelingType, Float) -> Unit,
    onSave: () -> Unit,
) {
    when (step) {
        EmotionalRecordStep.INTRO ->
            EmotionalRecordIntroStep(
                onNext = onNext,
                onDismiss = onDismiss,
            )

        EmotionalRecordStep.CAPTURE ->
            EmotionalRecordCaptureStep(
                emotions = emotions,
                isSaving = isSaving,
                onBack = onDismiss,
                onSliderChange = onSliderChange,
                onSave = onSave,
            )

        EmotionalRecordStep.DONE ->
            EmotionalRecordDoneStep(
                emotions = emotions,
                onDone = onDismiss,
            )
    }
}

@Preview(showBackground = true, name = "Intro")
@Composable
fun EmotionalRecordIntroPreview() {
    BeeFreeTheme {
        EmotionalRecordContent(
            step = EmotionalRecordStep.INTRO,
            emotions = previewEmotions(),
            isSaving = false,
            onNext = {},
            onDismiss = {},
            onSliderChange = { _, _ -> },
            onSave = {},
        )
    }
}

@Preview(showBackground = true, name = "Capture")
@Composable
fun EmotionalRecordCapturePreview() {
    BeeFreeTheme {
        EmotionalRecordContent(
            step = EmotionalRecordStep.CAPTURE,
            emotions = previewEmotions(),
            isSaving = false,
            onNext = {},
            onDismiss = {},
            onSliderChange = { _, _ -> },
            onSave = {},
        )
    }
}

@Preview(showBackground = true, name = "Capture - Error")
@Composable
fun EmotionalRecordCaptureErrorPreview() {
    BeeFreeTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            EmotionalRecordContent(
                step = EmotionalRecordStep.CAPTURE,
                emotions = previewEmotions(),
                isSaving = false,
                onNext = {},
                onDismiss = {},
                onSliderChange = { _, _ -> },
                onSave = {},
            )
            AlertDialog(
                onDismissRequest = {},
                title = { BeeHeadlineMedium(text = "Erro") },
                text = { BeeBodyMedium(text = "Não foi possível salvar seu registro. Tente novamente.") },
                confirmButton = {
                    BeeButtonPrimary(onClick = {}) {
                        BeeLabelLarge(text = "OK", color = MaterialTheme.colorScheme.onPrimary)
                    }
                },
            )
        }
    }
}

@Preview(showBackground = true, name = "Done")
@Composable
fun EmotionalRecordDonePreview() {
    BeeFreeTheme {
        EmotionalRecordDoneStep(
            emotions =
                previewEmotions(
                    FeelingType.SLEEP to 6f,
                    FeelingType.CRAVING to 3f,
                    FeelingType.STRESS to 7f,
                    FeelingType.FATIGUE to 5f,
                ),
            onDone = {},
        )
    }
}

private fun previewEmotions(vararg pairs: Pair<FeelingType, Float>): Map<FeelingType, Float> {
    val defaultValues = FeelingType.entries.associateWith { 5f }
    return defaultValues + pairs
}
