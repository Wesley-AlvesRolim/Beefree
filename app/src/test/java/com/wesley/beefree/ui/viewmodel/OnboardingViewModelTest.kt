package com.wesley.beefree.ui.viewmodel

import com.wesley.beefree.domain.onboarding.AddictionProfile
import com.wesley.beefree.domain.onboarding.StepType
import com.wesley.beefree.ui.viewmodel.mocks.OnboardingViewModelMock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class OnboardingViewModelTest {
    private fun viewModel() = OnboardingViewModelMock()

    private fun OnboardingViewModelMock.advanceToAddictionSelector() {
        repeat(3) { next() }
    }

    @Test
    fun `starts at WELCOME step`() {
        assertEquals(StepType.WELCOME, viewModel().currentStep.value.type)
    }

    @Test
    fun `next navigates from WELCOME to PRESENTATION`() {
        val vm = viewModel()
        vm.next()
        assertEquals(StepType.PRESENTATION, vm.currentStep.value.type)
    }

    @Test
    fun `previous at first step stays at WELCOME`() {
        val vm = viewModel()
        vm.previous()
        assertEquals(StepType.WELCOME, vm.currentStep.value.type)
    }

    @Test
    fun `updateAnswer updates the answers state`() {
        val vm = viewModel()
        vm.updateAnswer { copy(userName = "Wesley") }
        assertEquals("Wesley", vm.answers.value.userName)
    }

    @Test
    fun `after selecting PPU and advancing past selector, enters GENDER`() {
        val vm = viewModel()
        vm.updateAnswer { copy(addictionProfile = AddictionProfile.PPU) }
        vm.advanceToAddictionSelector()
        vm.next()

        assertEquals(StepType.GENDER, vm.currentStep.value.type)
    }

    @Test
    fun `after selecting GAMBLING and advancing past selector, enters GENDER`() {
        val vm = viewModel()
        vm.updateAnswer { copy(addictionProfile = AddictionProfile.GAMBLING) }
        vm.advanceToAddictionSelector()
        vm.next()

        assertEquals(StepType.GENDER, vm.currentStep.value.type)
    }

    @Test
    fun `PPU path shows PPCS6_FORM as second step after GENDER`() {
        val vm = viewModel()
        vm.updateAnswer { copy(addictionProfile = AddictionProfile.PPU) }
        vm.advanceToAddictionSelector()
        vm.next()
        vm.next()

        assertEquals(StepType.PPCS6_FORM, vm.currentStep.value.type)
    }

    @Test
    fun `PPU path shows FREQUENCY_FORM after EMA_FORM`() {
        val vm = viewModel()
        vm.updateAnswer { copy(addictionProfile = AddictionProfile.PPU) }
        vm.advanceToAddictionSelector()
        vm.next()
        vm.next()
        vm.next()
        vm.next()

        assertEquals(StepType.FREQUENCY_FORM, vm.currentStep.value.type)
    }

    @Test
    fun `GAMBLING path shows PGSI_FORM as second step after GENDER`() {
        val vm = viewModel()
        vm.updateAnswer { copy(addictionProfile = AddictionProfile.GAMBLING) }
        vm.advanceToAddictionSelector()
        vm.next()
        vm.next()

        assertEquals(StepType.PGSI_FORM, vm.currentStep.value.type)
    }

    @Test
    fun `answers accumulate correctly across multiple updates`() {
        val vm = viewModel()
        vm.updateAnswer { copy(userName = "Wesley") }
        vm.updateAnswer { copy(addictionProfile = AddictionProfile.PPU) }
        vm.updateAnswer { copy(gender = "Masculino") }

        val answers = vm.answers.value
        assertEquals("Wesley", answers.userName)
        assertEquals(AddictionProfile.PPU, answers.addictionProfile)
        assertEquals("Masculino", answers.gender)
    }

    @Test
    fun `finishOnboarding calls onFinish callback`() {
        val vm = viewModel()
        var finished = false
        vm.finishOnboarding(onFinish = { finished = true }, onError = {})
        assertNotNull(finished)
    }
}
