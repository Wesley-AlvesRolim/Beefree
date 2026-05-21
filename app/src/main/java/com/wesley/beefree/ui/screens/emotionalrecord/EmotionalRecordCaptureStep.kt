package com.wesley.beefree.ui.screens.emotionalrecord

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeButtonPrimary
import com.wesley.beefree.ui.components.designsystem.BeeCardElevated
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineMedium
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeLabelLarge
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeLabelSmall
import com.wesley.beefree.ui.components.designsystem.BeeScale0to10
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmotionalRecordCaptureStep(
    emotions: Map<FeelingType, Float>,
    isSaving: Boolean,
    onBack: () -> Unit,
    onSliderChange: (FeelingType, Float) -> Unit,
    onSave: () -> Unit,
) {
    val variables = rememberEmotionalRecordVariables()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.surfaceContainerLowest),
                title = { BeeHeadlineSmall(stringResource(R.string.emotional_record_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.onboarding_btn_back),
                        )
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            LazyColumn(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = BeeSpacing.L),
                verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
            ) {
                item {
                    Column(modifier = Modifier.padding(vertical = BeeSpacing.M)) {
                        BeeHeadlineMedium(text = stringResource(R.string.emotional_record_capture_title))
                    }
                }

                items(variables.size) { index ->
                    val variable = variables[index]
                    val value = emotions[variable.emotion] ?: 5f
                    EmotionalRecordVariableSlider(
                        variable = variable,
                        value = value.toInt(),
                        onValueChange = { onSliderChange(variable.emotion, it.toFloat()) },
                    )
                }
            }

            Spacer(Modifier.height(BeeSpacing.M))

            BeeButtonPrimary(
                onClick = onSave,
                enabled = !isSaving,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(BeeSpacing.L),
            ) {
                BeeLabelLarge(text = stringResource(R.string.emotional_record_save_button), color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
private fun EmotionalRecordVariableSlider(
    variable: EmotionalRecordVariable,
    value: Int,
    onValueChange: (Int) -> Unit,
) {
    BeeCardElevated(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(BeeSpacing.M),
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = BeeSpacing.XS),
                horizontalArrangement = Arrangement.spacedBy(BeeSpacing.M),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(BeeSpacing.L)
                            .clip(RoundedCornerShape(BeeSpacing.S))
                            .background(variable.color.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = variable.icon,
                        contentDescription = stringResource(variable.labelRes),
                        tint = variable.color,
                        modifier = Modifier.size(BeeSpacing.M),
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(BeeSpacing.XS),
                ) {
                    BeeBodyMedium(text = stringResource(variable.labelRes))
                    BeeLabelSmall(text = stringResource(variable.hintRes))
                }

                BeeBodyMedium(
                    text = "$value/10",
                    color = variable.color,
                )
            }

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = BeeSpacing.S),
                horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BeeScale0to10(value = value, onChange = onValueChange)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                BeeLabelMedium(text = stringResource(variable.lowLabelRes))
                BeeLabelMedium(text = stringResource(variable.highLabelRes))
            }
        }
    }
}
