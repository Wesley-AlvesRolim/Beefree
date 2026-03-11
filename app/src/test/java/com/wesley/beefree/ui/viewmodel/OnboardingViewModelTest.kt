package com.wesley.beefree.ui.viewmodel

import android.app.Application
import android.content.SharedPreferences
import com.wesley.beefree.ui.viewmodel.ports.AddictionCategory
import com.wesley.beefree.ui.viewmodel.ports.OnboardingStep
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class OnboardingViewModelTest {
    private lateinit var application: Application
    private lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setup() {
        sharedPreferences =
            mock {
                on { getString(anyString(), anyString()) } doReturn ""
                on { getInt(anyString(), anyInt()) } doReturn 0
                on { getLong(anyString(), anyInt().toLong()) } doReturn 0L
                on { getBoolean(anyString(), org.mockito.kotlin.any()) } doReturn false
            }
        application =
            mock {
                on { getSharedPreferences(anyString(), anyInt()) } doReturn sharedPreferences
                on { applicationContext } doReturn it
            }
    }

    @Test
    fun `toggleAddiction should add and remove addiction from selection`() {
        val viewModel = OnboardingViewModelImpl(application)

        assertTrue(viewModel.selectedAddictions.value.isEmpty())

        viewModel.toggleAddiction(AddictionCategory.ADULT_CONTENT)
        assertTrue(viewModel.selectedAddictions.value.contains(AddictionCategory.ADULT_CONTENT))

        viewModel.toggleAddiction(AddictionCategory.ADULT_CONTENT)
        assertFalse(viewModel.selectedAddictions.value.contains(AddictionCategory.ADULT_CONTENT))
    }

    @Test
    fun `toggleAddiction should allow multiple selections`() {
        val viewModel = OnboardingViewModelImpl(application)

        viewModel.toggleAddiction(AddictionCategory.ADULT_CONTENT)
        viewModel.toggleAddiction(AddictionCategory.BETS)

        assertTrue(viewModel.selectedAddictions.value.contains(AddictionCategory.ADULT_CONTENT))
        assertTrue(viewModel.selectedAddictions.value.contains(AddictionCategory.BETS))
        assertEquals(2, viewModel.selectedAddictions.value.size)
    }

    @Test
    fun `nextStep should navigate correctly`() {
        val viewModel = OnboardingViewModelImpl(application)
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
        val viewModel = OnboardingViewModelImpl(application)
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
