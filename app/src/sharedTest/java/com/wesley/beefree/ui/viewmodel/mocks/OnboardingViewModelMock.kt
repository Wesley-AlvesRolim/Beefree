package com.wesley.beefree.ui.viewmodel.mocks

import com.wesley.beefree.domain.onboarding.ClinicalProfile
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.domain.onboarding.ScaleResult
import com.wesley.beefree.domain.onboarding.engine.CompositeOnboardingFlowEngine
import com.wesley.beefree.domain.onboarding.engine.OnboardingFlowFactory
import com.wesley.beefree.ui.viewmodel.ports.OnboardingViewModelPort
import kotlinx.coroutines.flow.MutableStateFlow

class OnboardingViewModelMock : OnboardingViewModelPort {
    private val engine = CompositeOnboardingFlowEngine(OnboardingFlowFactory.factory())

    override val currentStep = MutableStateFlow(engine.currentStep)
    override val answers = MutableStateFlow(OnboardingAnswers())
    override val scaleResult = MutableStateFlow<ScaleResult?>(null)
    override val clinicalProfile = MutableStateFlow<ClinicalProfile?>(null)

    var nextCalled = 0
    var previousCalled = 0
    var finishCalled = 0

    override fun updateAnswer(update: OnboardingAnswers.() -> OnboardingAnswers) {
        answers.value = answers.value.update()
    }

    override fun next() {
        nextCalled++
        engine.next(answers.value)
        currentStep.value = engine.currentStep
    }

    override fun previous() {
        previousCalled++
        engine.previous()
        currentStep.value = engine.currentStep
    }

    override fun finishOnboarding(
        onFinish: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        finishCalled++
        onFinish()
    }
}
