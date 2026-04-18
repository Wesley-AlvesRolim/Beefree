package com.wesley.beefree.ui.screens.checkin.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeCardSection
import com.wesley.beefree.ui.components.designsystem.BeeChipTag
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeLabelLarge
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.components.designsystem.BeeTextArea

@Composable
fun DailyCheckInThoughtRecordSection(
    mood: String,
    onMoodChange: (String) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
    ) {
        BeeHeadlineSmall(stringResource(R.string.check_in_thought_record_label))
        BeeChipTag(label = stringResource(R.string.check_in_thought_tag))
    }
    Spacer(modifier = Modifier.height(BeeSpacing.S))
    BeeCardSection(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(BeeSpacing.L)) {
            BeeLabelLarge(stringResource(R.string.check_in_automatic_thought_label))
            Spacer(modifier = Modifier.height(BeeSpacing.S))
            BeeTextArea(
                value = mood,
                onValueChange = { onMoodChange(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { BeeBodyMedium(stringResource(R.string.check_in_automatic_thought_hint)) },
            )
            Spacer(modifier = Modifier.height(BeeSpacing.M))
            ThoughtPromptRow(
                icon = Icons.Outlined.Psychology,
                title = stringResource(R.string.check_in_challenge_thought_title),
                description = stringResource(R.string.check_in_challenge_thought_desc),
            )
            Spacer(modifier = Modifier.height(BeeSpacing.S))
            ThoughtPromptRow(
                icon = Icons.Outlined.SelfImprovement,
                title = stringResource(R.string.check_in_flexible_response_title),
                description = stringResource(R.string.check_in_flexible_response_desc),
            )
        }
    }
}
