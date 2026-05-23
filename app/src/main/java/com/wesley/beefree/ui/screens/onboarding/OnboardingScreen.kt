package com.wesley.beefree.ui.screens.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.CoreValueType
import com.wesley.beefree.domain.onboarding.ClinicalProfile
import com.wesley.beefree.domain.onboarding.NeurodivergenceAnswer
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.domain.onboarding.RiskLevel
import com.wesley.beefree.domain.onboarding.ScaleResult
import com.wesley.beefree.domain.onboarding.StepType
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.ui.components.LocalOnboardingProgress
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeButtonGhost
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.ports.OnboardingViewModelPort

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    viewModel: OnboardingViewModelPort,
) {
    var finishError by remember { mutableStateOf<Throwable?>(null) }

    finishError?.let {
        AlertDialog(
            onDismissRequest = { finishError = null },
            confirmButton = {
                BeeButtonGhost(onClick = { finishError = null }) {
                    BeeLabelMedium(stringResource(android.R.string.ok))
                }
            },
            text = {
                BeeBodyMedium(stringResource(R.string.onboarding_save_error))
            },
        )
    }

    val currentStep by viewModel.currentStep.collectAsState()
    val answers by viewModel.answers.collectAsState()
    val scaleResult by viewModel.scaleResult.collectAsState()
    val clinicalProfile by viewModel.clinicalProfile.collectAsState()

    OnboardingScreenContent(
        currentStepType = currentStep.type,
        answers = answers,
        scaleResult = scaleResult,
        clinicalProfile = clinicalProfile,
        onUpdate = viewModel::updateAnswer,
        onNext = viewModel::next,
        onBack = viewModel::previous,
        onFinish = {
            viewModel.finishOnboarding(
                onFinish = onFinish,
                onError = { finishError = it },
            )
        },
    )
}

@Composable
private fun OnboardingScreenContent(
    currentStepType: StepType,
    answers: OnboardingAnswers,
    scaleResult: ScaleResult?,
    clinicalProfile: ClinicalProfile?,
    onUpdate: (OnboardingAnswers.() -> OnboardingAnswers) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onFinish: () -> Unit,
) {
    val stepIndex = StepType.entries.indexOf(currentStepType) + 1
    val totalSteps = StepType.entries.size

    CompositionLocalProvider(LocalOnboardingProgress provides (stepIndex to totalSteps)) {
        when (currentStepType) {
            StepType.WELCOME -> OnboardingWelcomeScreen(onNext = onNext)

            StepType.PRESENTATION -> Box {}

            StepType.ASK_NAME ->
                OnboardingAskNameScreen(
                    answers = answers,
                    onUpdate = onUpdate,
                    onNext = onNext,
                )

            StepType.ADDICTION_SELECTOR -> Box {}

            StepType.GENDER ->
                OnboardingGenderScreen(
                    answers = answers,
                    onUpdate = onUpdate,
                    onNext = onNext,
                    onBack = onBack,
                )

            StepType.PPCS6_FORM ->
                OnboardingPpcs6Screen(
                    answers = answers,
                    onUpdate = onUpdate,
                    onNext = onNext,
                    onBack = onBack,
                )

            StepType.EMA_FORM ->
                OnboardingEmaScreen(
                    answers = answers,
                    onUpdate = onUpdate,
                    onNext = onNext,
                    onBack = onBack,
                )

            StepType.FREQUENCY_FORM ->
                OnboardingFrequencyScreen(
                    answers = answers,
                    onUpdate = onUpdate,
                    onNext = onNext,
                    onBack = onBack,
                )

            StepType.PGSI_FORM -> Box {}

            StepType.SYMPTOMS ->
                OnboardingSymptomsScreen(
                    answers = answers,
                    onUpdate = onUpdate,
                    onNext = onNext,
                    onBack = onBack,
                )

            StepType.NEURODIVERGENCE ->
                OnboardingNeurodivergenceScreen(
                    answers = answers,
                    onUpdate = onUpdate,
                    onNext = onNext,
                    onBack = onBack,
                )

            StepType.HOBBIES ->
                OnboardingHobbiesScreen(
                    answers = answers,
                    onUpdate = onUpdate,
                    onNext = onNext,
                    onBack = onBack,
                )

            StepType.GOALS ->
                OnboardingGoalsScreen(
                    answers = answers,
                    onUpdate = onUpdate,
                    onNext = onNext,
                    onBack = onBack,
                )

            StepType.SCORE_RESULT ->
                OnboardingScoreResultScreen(
                    scaleResult = scaleResult,
                    clinicalProfile = clinicalProfile,
                    onNext = onNext,
                )

            StepType.CORE_VALUES ->
                OnboardingCoreValuesScreen(
                    answers = answers,
                    onUpdate = onUpdate,
                    onNext = onNext,
                    onBack = onBack,
                )

            StepType.REQUEST_PERMISSIONS -> Box {}

            StepType.REQUEST_PERMISSION_MONITOR -> Box {}

            StepType.FINISH -> OnboardingFinishScreen(onFinish = onFinish)
        }
    }
}

@Composable
private fun OnboardingStepPreview(stepType: StepType) {
    OnboardingScreenContent(
        currentStepType = stepType,
        answers =
            OnboardingAnswers(
                userName = "Wesley",
                gender = "Masculino",
                ppcs6Answers = List(6) { 4 },
                emaAnswers = listOf(3),
                frequencyAnswer = 3,
                pgsiAnswers = List(9) { 1 },
                symptoms = listOf("Ansiedade"),
                neurodivergenceAnswer = NeurodivergenceAnswer.YES,
                hobbies = listOf("Leitura", "Música"),
                goals = listOf("Dormir melhor"),
                coreValues =
                    listOf(
                        CoreValueType.FAMILY.name,
                        CoreValueType.HEALTH.name,
                        CoreValueType.RELATIONSHIPS.name,
                    ),
            ),
        scaleResult = ScaleResult(raw = 24, level = RiskLevel.MODERATE),
        clinicalProfile =
            ClinicalProfile(
                incongruenceLevel = null,
                treatmentProfile = TreatmentProfile.HYBRID,
            ),
        onUpdate = {},
        onNext = {},
        onBack = {},
        onFinish = {},
    )
}

@Preview(showBackground = true, name = "Welcome")
@Composable
fun OnboardingWelcomePreview() {
    BeeFreeTheme {
        OnboardingStepPreview(StepType.WELCOME)
    }
}

@Preview(showBackground = true, name = "Ask Name")
@Composable
fun OnboardingAskNamePreview() {
    BeeFreeTheme {
        OnboardingStepPreview(StepType.ASK_NAME)
    }
}

@Preview(showBackground = true, name = "Gender")
@Composable
fun OnboardingGenderPreview() {
    BeeFreeTheme {
        OnboardingStepPreview(StepType.GENDER)
    }
}

@Preview(showBackground = true, name = "PPCS-6")
@Composable
fun OnboardingPpcs6Preview() {
    BeeFreeTheme {
        OnboardingStepPreview(StepType.PPCS6_FORM)
    }
}

@Preview(showBackground = true, name = "EMA")
@Composable
fun OnboardingEmaPreview() {
    BeeFreeTheme {
        OnboardingStepPreview(StepType.EMA_FORM)
    }
}

@Preview(showBackground = true, name = "Frequency")
@Composable
fun OnboardingFrequencyPreview() {
    BeeFreeTheme {
        OnboardingStepPreview(StepType.FREQUENCY_FORM)
    }
}

@Preview(showBackground = true, name = "Symptoms")
@Composable
fun OnboardingSymptomsPreview() {
    BeeFreeTheme {
        OnboardingStepPreview(StepType.SYMPTOMS)
    }
}

@Preview(showBackground = true, name = "Neurodivergence")
@Composable
fun OnboardingNeurodivergencePreview() {
    BeeFreeTheme {
        OnboardingStepPreview(StepType.NEURODIVERGENCE)
    }
}

@Preview(showBackground = true, name = "Hobbies")
@Composable
fun OnboardingHobbiesPreview() {
    BeeFreeTheme {
        OnboardingStepPreview(StepType.HOBBIES)
    }
}

@Preview(showBackground = true, name = "Goals")
@Composable
fun OnboardingGoalsPreview() {
    BeeFreeTheme {
        OnboardingStepPreview(StepType.GOALS)
    }
}

@Preview(showBackground = true, name = "Score Result")
@Composable
fun OnboardingScoreResultPreview() {
    BeeFreeTheme {
        OnboardingStepPreview(StepType.SCORE_RESULT)
    }
}

@Preview(showBackground = true, name = "Core Values")
@Composable
fun OnboardingCoreValuesPreview() {
    BeeFreeTheme {
        OnboardingStepPreview(StepType.CORE_VALUES)
    }
}

@Preview(showBackground = true, name = "Finish")
@Composable
fun OnboardingFinishPreview() {
    BeeFreeTheme {
        OnboardingStepPreview(StepType.FINISH)
    }
}
