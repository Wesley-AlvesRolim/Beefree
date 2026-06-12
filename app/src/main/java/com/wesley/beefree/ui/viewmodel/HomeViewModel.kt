package com.wesley.beefree.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wesley.beefree.domain.entities.EmotionRecord
import com.wesley.beefree.domain.entities.RelapseRecord
import com.wesley.beefree.domain.entities.RiskAssessment
import com.wesley.beefree.domain.entities.RiskTrigger
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.usecases.checkin.HasCompletedTodaysCheckInUseCase
import com.wesley.beefree.domain.usecases.home.ComputeRelapseSuccessRateUseCase
import com.wesley.beefree.domain.usecases.home.HomeData
import com.wesley.beefree.domain.usecases.home.LoadHomeDataUseCase
import com.wesley.beefree.infrastructure.logging.AndroidLogger
import com.wesley.beefree.infrastructure.logging.Logger
import com.wesley.beefree.infrastructure.storage.adapters.KeyValueRiskWeightsRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomAddictionRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomCheckInRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomLessonRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomMetricsRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomUserProfileRepository
import com.wesley.beefree.infrastructure.storage.adapters.SharedPreferencesKeyValueStorage
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class HomeNavigationDestination {
    object CheckIn : HomeNavigationDestination()

    data class HelpIntervention(
        val source: HelpInterventionSource = HelpInterventionSource.FAB,
    ) : HomeNavigationDestination()

    object Onboarding : HomeNavigationDestination()
}

data class HomeUiState(
    val user: UserProfile = UserProfile(profileName = "", createdAt = 0L, updatedAt = 0L),
    val psychoeducationMessage: String? = null,
    val relapseHistory: List<RelapseRecord> = emptyList(),
    val relapseSuccessRate: Float = 1f,
    val emotionRecords: List<EmotionRecord> = emptyList(),
    val hasCheckedInToday: Boolean = false,
    val treatmentProfile: TreatmentProfile = TreatmentProfile.ACT,
    val todayRiskAssessments: List<RiskAssessment> = emptyList(),
    val alignedDays: Int = 30,
    val relapseDays: Int = 0,
    val topTriggers: List<Pair<RiskTrigger, Double>> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

class HomeViewModel(
    private val loadHomeDataUseCase: LoadHomeDataUseCase,
    private val logger: Logger = AndroidLogger,
) : ViewModel() {
    private var isHomeVisible = false
    private var pendingOnboardingRedirect = false

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _navigationEvents =
        MutableSharedFlow<HomeNavigationDestination>(extraBufferCapacity = 1)
    val navigationEvents: SharedFlow<HomeNavigationDestination> = _navigationEvents.asSharedFlow()

    fun navigateToCheckIn() {
        _navigationEvents.tryEmit(HomeNavigationDestination.CheckIn)
    }

    fun navigateToHelpIntervention(source: HelpInterventionSource = HelpInterventionSource.FAB) {
        _navigationEvents.tryEmit(HomeNavigationDestination.HelpIntervention(source))
    }

    fun onHomeVisible() {
        isHomeVisible = true
        if (pendingOnboardingRedirect) {
            pendingOnboardingRedirect = false
            _navigationEvents.tryEmit(HomeNavigationDestination.Onboarding)
        }
    }

    init {
        refresh()
    }

    fun refresh() {
        pendingOnboardingRedirect = false
        viewModelScope.launch {
            loadData()
        }
    }

    fun resetError() {
        _uiState.update { it.copy(error = null) }
    }

    private suspend fun loadData() =
        coroutineScope {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                when (val result = loadHomeDataUseCase.execute()) {
                    is HomeData.OnboardingRequired -> {
                        pendingOnboardingRedirect = true
                        if (isHomeVisible) {
                            pendingOnboardingRedirect = false
                            _navigationEvents.emit(HomeNavigationDestination.Onboarding)
                        }
                        _uiState.update { it.copy(isLoading = false) }
                    }
                    is HomeData.Success -> {
                        _uiState.update {
                            it.copy(
                                user = result.user,
                                psychoeducationMessage = result.psychoeducationMessage,
                                relapseHistory = result.relapseHistory,
                                relapseSuccessRate = result.relapseSuccessRate,
                                emotionRecords = result.emotionRecords,
                                hasCheckedInToday = result.hasCheckedInToday,
                                treatmentProfile = result.treatmentProfile,
                                todayRiskAssessments = result.todayRiskAssessments,
                                alignedDays = result.alignedDays,
                                relapseDays = 30 - result.alignedDays,
                                topTriggers = result.topTriggers,
                                isLoading = false,
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                logger.e(TAG, "Failed to load home data", e)
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
            }
        }

    companion object {
        private const val TAG = "HomeViewModel"

        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val database = AppDatabase.getDatabase(context)
                    val checkInRepository =
                        RoomCheckInRepository(
                            database.dailyCheckInDao(),
                            database.weeklyCheckInDao(),
                        )
                    val keyValueStorage = SharedPreferencesKeyValueStorage(context)
                    @Suppress("UNCHECKED_CAST")
                    return HomeViewModel(
                        loadHomeDataUseCase =
                            LoadHomeDataUseCase(
                                lessonRepository =
                                    RoomLessonRepository(
                                        database.psychoeducationContentDao(),
                                    ),
                                addictionRepository =
                                    RoomAddictionRepository(
                                        database.addictionCategoryDao(),
                                        database.relapseRecordDao(),
                                    ),
                                metricsRepository =
                                    RoomMetricsRepository(
                                        database.emotionRecordDao(),
                                        database.riskFeatureSnapshotDao(),
                                        database.riskAssessmentDao(),
                                    ),
                                userProfileRepository =
                                    RoomUserProfileRepository(
                                        database.userProfileDao(),
                                        database.userAddictionDao(),
                                    ),
                                checkInRepository = checkInRepository,
                                riskWeightsRepository = KeyValueRiskWeightsRepository(keyValueStorage),
                                computeRelapseSuccessRateUseCase =
                                    ComputeRelapseSuccessRateUseCase(),
                                hasCompletedTodaysCheckInUseCase =
                                    HasCompletedTodaysCheckInUseCase(
                                        checkInRepository,
                                    ),
                            ),
                    ) as T
                }
            }
    }
}
