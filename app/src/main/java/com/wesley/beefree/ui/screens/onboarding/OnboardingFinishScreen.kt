package com.wesley.beefree.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.OnboardingLayout
import com.wesley.beefree.ui.components.OnboardingNavigationRow
import com.wesley.beefree.ui.components.designsystem.*

@Composable
fun OnboardingFinishScreen(onFinish: () -> Unit) {
    OnboardingLayout(
        showTopBar = false,
        bottomBar = {
            OnboardingNavigationRow(
                text = stringResource(R.string.onboarding_finish_cta),
                onNext = onFinish,
            )
        },
        contentVerticalArrangement = Arrangement.Center,
    ) {
        BeeMascot(
            size = BeeMascotSize.Hero,
            contentDescription = stringResource(R.string.onboarding_mascot_description),
        )
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        BeeHeadlineLarge(
            text = stringResource(R.string.onboarding_finish_title),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.S))
        BeeBodyMedium(
            text = stringResource(R.string.onboarding_finish_body),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.L))
        BeeCardSection(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(BeeSpacing.M),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(BeeSpacing.M),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                    contentDescription = stringResource(R.string.onboarding_finish_progress_icon),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(BeeSpacing.XL),
                )
                Column {
                    BeeLabelSmall(
                        text = stringResource(R.string.onboarding_finish_next_step_label),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    BeeLabelLarge(
                        text = stringResource(R.string.onboarding_finish_next_step_value),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}
