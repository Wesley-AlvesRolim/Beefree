package com.wesley.beefree.ui.components.checkin.daily

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wesley.beefree.domain.checkin.DualScaleStep
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeCardSection
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeLabelSmall
import com.wesley.beefree.ui.components.designsystem.BeeScale0to10
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@Composable
fun DualScaleStepContent(
    spec: DualScaleStep,
    firstValue: Int,
    secondValue: Int,
    onFirstChange: (Int) -> Unit,
    onSecondChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        BeeHeadlineSmall(stringByName(spec.titleKey))
        Spacer(modifier = Modifier.height(BeeSpacing.S))
        BeeBodyMedium(
            text = stringByName(spec.subtitleKey),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.L))
        BeeCardSection(modifier = Modifier.fillMaxWidth()) {
            BeeLabelSmall(
                text = stringByName(spec.first.titleKey).uppercase(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(BeeSpacing.S))
            BeeScale0to10(
                value = firstValue,
                onChange = onFirstChange,
                lowLabel = stringByName(spec.first.lowLabelKey),
                highLabel = stringByName(spec.first.highLabelKey),
            )
        }
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        BeeCardSection(modifier = Modifier.fillMaxWidth()) {
            BeeLabelSmall(
                text = stringByName(spec.second.titleKey).uppercase(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(BeeSpacing.S))
            BeeScale0to10(
                value = secondValue,
                onChange = onSecondChange,
                lowLabel = stringByName(spec.second.lowLabelKey),
                highLabel = stringByName(spec.second.highLabelKey),
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}
