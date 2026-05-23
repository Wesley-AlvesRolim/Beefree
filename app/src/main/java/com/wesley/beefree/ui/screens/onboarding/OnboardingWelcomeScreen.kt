package com.wesley.beefree.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

private val chapterLabels =
    listOf(
        R.string.onboarding_section_sobre_voce,
        R.string.onboarding_section_seu_padrao,
        R.string.onboarding_section_sua_perspectiva,
        R.string.onboarding_section_seu_contexto,
        R.string.onboarding_section_sua_direcao,
    )

@Composable
fun OnboardingWelcomeScreen(onNext: () -> Unit) {
    OnboardingLayout(
        showTopBar = false,
        bottomBar = { OnboardingNavigationRow(onNext, text = stringResource(R.string.onboarding_welcome_cta)) },
        contentVerticalArrangement = Arrangement.Center,
    ) {
        BeeMascot(
            size = BeeMascotSize.Hero,
            contentDescription = stringResource(R.string.onboarding_mascot_description),
        )
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        BeeHeadlineLarge(
            text = stringResource(R.string.onboarding_welcome_title),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.S))
        BeeBodyLarge(
            text = stringResource(R.string.onboarding_welcome_subtitle),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.L))
        BeeCardSection(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = BeeSpacing.S,
                            vertical = BeeSpacing.M,
                        ),
                horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
            ) {
                chapterLabels.forEachIndexed { index, labelRes ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        BeeLabelMedium(
                            text = (index + 1).toString(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        BeeBodySmall(
                            text = stringResource(labelRes),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}
