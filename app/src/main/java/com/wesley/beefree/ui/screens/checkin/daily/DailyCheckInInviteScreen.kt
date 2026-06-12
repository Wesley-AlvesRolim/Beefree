package com.wesley.beefree.ui.screens.checkin.daily

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.wesley.beefree.R
import com.wesley.beefree.domain.checkin.BooleanBranchStep
import com.wesley.beefree.domain.checkin.DailyCheckInFlow
import com.wesley.beefree.domain.checkin.DailyCheckInStep
import com.wesley.beefree.domain.checkin.DualScaleStep
import com.wesley.beefree.domain.checkin.EmotionalRecordStep
import com.wesley.beefree.domain.checkin.MindfulnessStep
import com.wesley.beefree.domain.checkin.MultiSelectStep
import com.wesley.beefree.domain.checkin.RelapseRegistrationStep
import com.wesley.beefree.domain.checkin.ScaleStep
import com.wesley.beefree.domain.checkin.SingleSelectStep
import com.wesley.beefree.domain.checkin.SingleSelectWithContextStep
import com.wesley.beefree.domain.checkin.TextStep
import com.wesley.beefree.domain.checkin.TextWithSuggestionsStep
import com.wesley.beefree.domain.checkin.TherapeuticActivityStep
import com.wesley.beefree.domain.checkin.VideoWatchStep
import com.wesley.beefree.domain.treatments.checkin.TccDailyCheckInFlow
import com.wesley.beefree.ui.components.checkin.daily.stringByName
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeButtonOutlined
import com.wesley.beefree.ui.components.designsystem.BeeButtonPrimary
import com.wesley.beefree.ui.components.designsystem.BeeCardSection
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineMedium
import com.wesley.beefree.ui.components.designsystem.BeeLabelLarge
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeLabelSmall
import com.wesley.beefree.ui.components.designsystem.BeeMascot
import com.wesley.beefree.ui.components.designsystem.BeeMascotSize
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.theme.BeeFreeTheme

@Composable
fun DailyCheckInInviteScreen(
    flow: DailyCheckInFlow,
    profileName: String?,
    isCheckInDone: Boolean,
    onStartCheckIn: () -> Unit,
    onStartEmotionalRecord: () -> Unit,
) {
    Scaffold { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = BeeSpacing.L)
                    .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(BeeSpacing.L))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                BeeMascot(size = BeeMascotSize.Hero)
            }
            Spacer(modifier = Modifier.height(BeeSpacing.L))
            BeeLabelSmall(
                text = stringByName(flow.pathLabelKey).uppercase(),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(BeeSpacing.S))
            BeeHeadlineMedium(
                text =
                    profileName?.let {
                        stringResource(R.string.daily_checkin_invite_greeting, it)
                    } ?: stringResource(R.string.daily_checkin_invite_greeting_no_name),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(BeeSpacing.S))
            if (isCheckInDone) {
                BeeBodyMedium(
                    text = stringResource(R.string.daily_checkin_already_done_message),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(BeeSpacing.XL))
                BeeButtonPrimary(
                    onClick = onStartEmotionalRecord,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    BeeLabelLarge(
                        text = stringResource(R.string.daily_checkin_invite_start_emotional_record),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            } else {
                BeeBodyMedium(
                    text = stringResource(R.string.daily_checkin_invite_subtitle),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(BeeSpacing.L))
                BeeCardSection(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(BeeSpacing.M),
                        verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
                    ) {
                        BeeLabelMedium(
                            text = stringResource(R.string.daily_checkin_invite_steps_title),
                        )
                        flow.steps.forEachIndexed { index, step ->
                            StepPreviewRow(index = index + 1, step = step)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(BeeSpacing.XL))
                BeeButtonPrimary(
                    onClick = onStartCheckIn,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    BeeLabelLarge(
                        text = stringResource(R.string.daily_checkin_invite_start),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
                Spacer(modifier = Modifier.height(BeeSpacing.S))
                BeeButtonOutlined(
                    onClick = onStartEmotionalRecord,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    BeeLabelLarge(
                        text = stringResource(R.string.daily_checkin_invite_start_emotional_record),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            Spacer(modifier = Modifier.height(BeeSpacing.L))
        }
    }
}

@Composable
private fun StepPreviewRow(
    index: Int,
    step: DailyCheckInStep,
) {
    val titleKey =
        when (step) {
            is BooleanBranchStep -> step.titleKey
            is ScaleStep -> step.titleKey
            is DualScaleStep -> step.titleKey
            is MultiSelectStep -> step.titleKey
            is SingleSelectStep -> step.titleKey
            is TextStep -> step.titleKey
            is EmotionalRecordStep -> step.titleKey
            is SingleSelectWithContextStep -> step.titleKey
            is TherapeuticActivityStep -> step.titleKey
            is TextWithSuggestionsStep -> step.titleKey
            is RelapseRegistrationStep -> step.titleKey
            is VideoWatchStep -> step.titleKey
            is MindfulnessStep -> "urge_surfing_headline"
        }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier
                    .size(BeeSpacing.L)
                    .background(MaterialTheme.colorScheme.tertiaryContainer, CircleShape),
        ) {
            BeeLabelSmall(
                text = index.toString(),
                color = MaterialTheme.colorScheme.onTertiaryContainer,
            )
        }
        Spacer(modifier = Modifier.size(BeeSpacing.M))
        BeeBodyMedium(
            text = stringByName(titleKey),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DailyCheckInInvitePreview() {
    BeeFreeTheme {
        DailyCheckInInviteScreen(
            flow = TccDailyCheckInFlow.flow,
            profileName = "Wesley",
            isCheckInDone = false,
            onStartCheckIn = {},
            onStartEmotionalRecord = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DailyCheckInInviteWhenItIsDonePreview() {
    BeeFreeTheme {
        DailyCheckInInviteScreen(
            flow = TccDailyCheckInFlow.flow,
            profileName = "Wesley",
            isCheckInDone = true,
            onStartCheckIn = {},
            onStartEmotionalRecord = {},
        )
    }
}
