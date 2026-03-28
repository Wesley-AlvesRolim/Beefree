package com.wesley.beefree.ui.screens.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.wesley.beefree.R
import com.wesley.beefree.domain.onboarding.ClinicalProfile
import com.wesley.beefree.domain.onboarding.RiskLevel
import com.wesley.beefree.domain.onboarding.ScaleResult
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.ui.components.OnboardingLayout
import com.wesley.beefree.ui.components.OnboardingNavigationRow
import com.wesley.beefree.ui.components.OnboardingTitle
import com.wesley.beefree.ui.components.designsystem.*

private const val PPCS6_MIN = 6
private const val PPCS6_MAX = 42

@Composable
fun OnboardingScoreResultScreen(
    scaleResult: ScaleResult?,
    clinicalProfile: ClinicalProfile?,
    onNext: () -> Unit,
) {
    OnboardingLayout(showTopBar = false) {
        OnboardingTitle(stringResource(R.string.onboarding_score_result_title))
        Spacer(modifier = Modifier.height(BeeSpacing.S))
        BeeBodyMedium(
            text = stringResource(R.string.onboarding_score_result_intro),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.XL))

        if (scaleResult != null) {
            Ppcs6ScoreCard(scaleResult)
            Spacer(modifier = Modifier.height(BeeSpacing.M))
            BeeBodyLarge(
                text = riskLevelMessage(scaleResult.level),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
        }

        if (clinicalProfile != null) {
            Spacer(modifier = Modifier.height(BeeSpacing.XL))
            TreatmentApproachCard(clinicalProfile.treatmentProfile)
        }

        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        OnboardingNavigationRow(onNext)
    }
}

@Composable
private fun Ppcs6ScoreCard(scaleResult: ScaleResult) {
    val progress =
        ((scaleResult.raw - PPCS6_MIN).toFloat() / (PPCS6_MAX - PPCS6_MIN).toFloat())
            .coerceIn(0f, 1f)

    BeeCardSection(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(BeeSpacing.M)) {
            BeeLabelSmall(
                text = stringResource(R.string.onboarding_score_result_scale_label),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(BeeSpacing.M))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            )
            Spacer(modifier = Modifier.height(BeeSpacing.XS))
            Row(modifier = Modifier.fillMaxWidth()) {
                BeeBodySmall(
                    text = stringResource(R.string.onboarding_score_result_scale_low),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
                BeeBodySmall(
                    text = stringResource(R.string.onboarding_score_result_score_of, scaleResult.raw),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
                BeeBodySmall(
                    text = stringResource(R.string.onboarding_score_result_scale_high),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun TreatmentApproachCard(profile: TreatmentProfile) {
    BeeCardSection(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(BeeSpacing.M)) {
            BeeLabelSmall(
                text = stringResource(R.string.onboarding_score_result_approach_label),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(BeeSpacing.S))
            BeeHeadlineSmall(
                text = treatmentProfileLabel(profile),
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(BeeSpacing.XS))
            BeeBodyMedium(
                text = treatmentProfileDescription(profile),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun riskLevelMessage(level: RiskLevel): String =
    when (level) {
        RiskLevel.LOW, RiskLevel.MODERATE -> stringResource(R.string.onboarding_score_result_message_low)
        RiskLevel.HIGH, RiskLevel.VERY_HIGH -> stringResource(R.string.onboarding_score_result_message_high)
    }

@Composable
private fun treatmentProfileLabel(profile: TreatmentProfile): String =
    when (profile) {
        TreatmentProfile.ACT -> stringResource(R.string.onboarding_treatment_act)
        TreatmentProfile.TCC -> stringResource(R.string.onboarding_treatment_tcc)
        TreatmentProfile.ACT_AND_TCC -> stringResource(R.string.onboarding_treatment_act_and_tcc)
        TreatmentProfile.PREVENTION -> stringResource(R.string.onboarding_treatment_prevention)
        TreatmentProfile.HYBRID_TCC_FOCUS -> stringResource(R.string.onboarding_treatment_hybrid_tcc)
        TreatmentProfile.HYBRID_ACT_FOCUS -> stringResource(R.string.onboarding_treatment_hybrid_act)
    }

@Composable
private fun treatmentProfileDescription(profile: TreatmentProfile): String =
    when (profile) {
        TreatmentProfile.ACT -> stringResource(R.string.onboarding_treatment_desc_act)
        TreatmentProfile.TCC -> stringResource(R.string.onboarding_treatment_desc_tcc)
        TreatmentProfile.ACT_AND_TCC -> stringResource(R.string.onboarding_treatment_desc_act_and_tcc)
        TreatmentProfile.PREVENTION -> stringResource(R.string.onboarding_treatment_desc_prevention)
        TreatmentProfile.HYBRID_TCC_FOCUS -> stringResource(R.string.onboarding_treatment_desc_hybrid_tcc)
        TreatmentProfile.HYBRID_ACT_FOCUS -> stringResource(R.string.onboarding_treatment_desc_hybrid_act)
    }
