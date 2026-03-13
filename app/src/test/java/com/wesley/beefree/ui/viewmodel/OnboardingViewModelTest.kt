package com.wesley.beefree.ui.viewmodel

import com.wesley.beefree.ui.viewmodel.mocks.OnboardingViewModelMock
import com.wesley.beefree.ui.viewmodel.ports.AddictionCategory
import com.wesley.beefree.ui.viewmodel.ports.OnboardingStep
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OnboardingViewModelTest {
    private fun viewModel() = OnboardingViewModelMock()

    @Test
    fun `toggleAddiction should add and remove addiction from selection`() {
        val viewModel = viewModel()

        assertTrue(viewModel.selectedAddictions.value.isEmpty())

        viewModel.toggleAddiction(AddictionCategory.ADULT_CONTENT)
        assertTrue(viewModel.selectedAddictions.value.contains(AddictionCategory.ADULT_CONTENT))

        viewModel.toggleAddiction(AddictionCategory.ADULT_CONTENT)
        assertFalse(viewModel.selectedAddictions.value.contains(AddictionCategory.ADULT_CONTENT))
    }

    @Test
    fun `toggleAddiction should allow multiple selections`() {
        val viewModel = viewModel()

        viewModel.toggleAddiction(AddictionCategory.ADULT_CONTENT)
        viewModel.toggleAddiction(AddictionCategory.BETS)

        assertTrue(viewModel.selectedAddictions.value.contains(AddictionCategory.ADULT_CONTENT))
        assertTrue(viewModel.selectedAddictions.value.contains(AddictionCategory.BETS))
        assertEquals(2, viewModel.selectedAddictions.value.size)
    }

    @Test
    fun `nextStep should navigate correctly`() {
        val viewModel = viewModel()
        viewModel.moveToStep(OnboardingStep.WELCOME)

        viewModel.nextStep()
        assertEquals(OnboardingStep.HOW_IT_WORKS, viewModel.currentStep.value)

        viewModel.nextStep()
        assertEquals(OnboardingStep.ADDICTION_SELECTOR, viewModel.currentStep.value)

        viewModel.nextStep()
        assertEquals(OnboardingStep.REQUEST_PERMISSIONS, viewModel.currentStep.value)

        viewModel.nextStep()
        assertEquals(OnboardingStep.REQUEST_PERMISSION_SCREEN_MONITOR, viewModel.currentStep.value)

        viewModel.nextStep()
        assertEquals(OnboardingStep.REQUEST_PERMISSION_SCREEN_OVERLAY, viewModel.currentStep.value)

        viewModel.nextStep()
        assertEquals(OnboardingStep.FINISH, viewModel.currentStep.value)
    }

    @Test
    fun `previousStep should navigate correctly`() {
        val viewModel = viewModel()
        viewModel.moveToStep(OnboardingStep.FINISH)

        viewModel.previousStep()
        assertEquals(OnboardingStep.REQUEST_PERMISSION_SCREEN_OVERLAY, viewModel.currentStep.value)

        viewModel.previousStep()
        assertEquals(OnboardingStep.REQUEST_PERMISSION_SCREEN_MONITOR, viewModel.currentStep.value)

        viewModel.previousStep()
        assertEquals(OnboardingStep.REQUEST_PERMISSIONS, viewModel.currentStep.value)

        viewModel.previousStep()
        assertEquals(OnboardingStep.ADDICTION_SELECTOR, viewModel.currentStep.value)

        viewModel.previousStep()
        assertEquals(OnboardingStep.HOW_IT_WORKS, viewModel.currentStep.value)

        viewModel.previousStep()
        assertEquals(OnboardingStep.WELCOME, viewModel.currentStep.value)
    }
}
