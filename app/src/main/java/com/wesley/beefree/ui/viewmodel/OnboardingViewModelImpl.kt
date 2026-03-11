package com.wesley.beefree.ui.viewmodel

import android.app.Application
import android.content.Context
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wesley.beefree.data.keywords.getBetsKeyWords
import com.wesley.beefree.data.keywords.getPornKeywords
import com.wesley.beefree.domain.entities.AddictionKeyword
import com.wesley.beefree.domain.entities.AddictionType
import com.wesley.beefree.infrastructure.services.AccessibilityServiceActivity
import com.wesley.beefree.storage.adapters.RoomAddictionRepository
import com.wesley.beefree.storage.adapters.SharedPreferencesKeyValueStorage
import com.wesley.beefree.storage.adapters.db.AppDatabase
import com.wesley.beefree.storage.repositories.KeyValueStorageRepository
import com.wesley.beefree.ui.viewmodel.ports.AddictionCategory
import com.wesley.beefree.ui.viewmodel.ports.OnboardingStep
import com.wesley.beefree.ui.viewmodel.ports.OnboardingViewModelPort
import com.wesley.beefree.utils.AccessibilityUtils
import com.wesley.beefree.utils.OverlayUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class OnboardingViewModelImpl(
    application: Application,
) : AndroidViewModel(application),
    OnboardingViewModelPort {
    private val keyValueStorageRepository: KeyValueStorageRepository =
        KeyValueStorageRepository(SharedPreferencesKeyValueStorage(application))

    private val addictionRepository: RoomAddictionRepository

    init {
        val database = AppDatabase.getDatabase(application)
        addictionRepository =
            RoomAddictionRepository(
                database.addictionTypeDao(),
                database.addictionKeywordDao(),
                database.relapseHistoryDao(),
            )
    }

    private val _currentStep = MutableStateFlow(OnboardingStep.WELCOME)
    override val currentStep: StateFlow<OnboardingStep> = _currentStep.asStateFlow()

    private val _selectedAddictions = MutableStateFlow<Set<AddictionCategory>>(emptySet())

    override val selectedAddictions: StateFlow<Set<AddictionCategory>> = _selectedAddictions.asStateFlow()

    open val openIsAccessibilityEnabled = MutableStateFlow(false)
    override val isAccessibilityEnabled: StateFlow<Boolean> = openIsAccessibilityEnabled.asStateFlow()

    open val openIsOverlayEnabled = MutableStateFlow(false)
    override val isOverlayEnabled: StateFlow<Boolean> = openIsOverlayEnabled.asStateFlow()

    override fun updatePermissions(context: Context) {
        openIsAccessibilityEnabled.value =
            AccessibilityUtils.isAccessibilityServiceEnabledAlternative(
                context,
                AccessibilityServiceActivity::class.java,
            )
        openIsOverlayEnabled.value = Settings.canDrawOverlays(context)
    }

    override fun openAccessibilitySettings(context: Context) {
        AccessibilityUtils.openAccessibilitySettings(context)
    }

    override fun openOverlaySettings(context: Context) {
        OverlayUtils.openSettingsToEnableTheOverlayPermission(context)
    }

    override fun toggleAddiction(category: AddictionCategory) {
        val current = _selectedAddictions.value
        if (current.contains(category)) {
            _selectedAddictions.value = current - category
        } else {
            _selectedAddictions.value = current + category
        }
    }

    override fun moveToStep(step: OnboardingStep) {
        _currentStep.value = step
    }

    override fun nextStep() {
        val next =
            when (_currentStep.value) {
                OnboardingStep.WELCOME -> OnboardingStep.HOW_IT_WORKS
                OnboardingStep.HOW_IT_WORKS -> OnboardingStep.ADDICTION_SELECTOR
                OnboardingStep.ADDICTION_SELECTOR -> OnboardingStep.REQUEST_PERMISSIONS
                OnboardingStep.REQUEST_PERMISSIONS -> OnboardingStep.REQUEST_PERMISSION_SCREEN_MONITOR
                OnboardingStep.REQUEST_PERMISSION_SCREEN_MONITOR -> OnboardingStep.REQUEST_PERMISSION_SCREEN_OVERLAY
                OnboardingStep.REQUEST_PERMISSION_SCREEN_OVERLAY -> OnboardingStep.FINISH
                OnboardingStep.FINISH -> OnboardingStep.FINISH
            }
        _currentStep.value = next
    }

    override fun previousStep() {
        val prev =
            when (_currentStep.value) {
                OnboardingStep.WELCOME -> OnboardingStep.WELCOME
                OnboardingStep.HOW_IT_WORKS -> OnboardingStep.WELCOME
                OnboardingStep.ADDICTION_SELECTOR -> OnboardingStep.HOW_IT_WORKS
                OnboardingStep.REQUEST_PERMISSIONS -> OnboardingStep.ADDICTION_SELECTOR
                OnboardingStep.REQUEST_PERMISSION_SCREEN_MONITOR -> OnboardingStep.REQUEST_PERMISSIONS
                OnboardingStep.REQUEST_PERMISSION_SCREEN_OVERLAY -> OnboardingStep.REQUEST_PERMISSION_SCREEN_MONITOR
                OnboardingStep.FINISH -> OnboardingStep.REQUEST_PERMISSION_SCREEN_OVERLAY
            }
        _currentStep.value = prev
    }

    override fun finishOnboarding(onFinish: () -> Unit) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            _selectedAddictions.value.forEach { category ->
                val (name, keywords) =
                    when (category) {
                        AddictionCategory.ADULT_CONTENT -> "Adult Content" to getPornKeywords()
                        AddictionCategory.BETS -> "Bets" to getBetsKeyWords()
                        AddictionCategory.OTHERS -> "Others" to emptyList()
                    }

                val addictionTypeId =
                    addictionRepository.insertAddictionType(
                        AddictionType(
                            name = name,
                            isMonitoringEnabled = true,
                            createdAt = now,
                            updatedAt = now,
                        ),
                    )

                keywords.forEach { keyword ->
                    addictionRepository.insertKeyword(
                        AddictionKeyword(
                            addictionTypeId = addictionTypeId.toInt(),
                            keyword = keyword,
                        ),
                    )
                }
            }
            keyValueStorageRepository.saveOnboardingCompleted(true)
            onFinish()
        }
    }
}
