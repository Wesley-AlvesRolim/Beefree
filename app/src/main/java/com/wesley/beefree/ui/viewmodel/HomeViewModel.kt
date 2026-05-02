package com.wesley.beefree.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wesley.beefree.domain.checkin.usecases.DetermineCheckInTypeUseCase
import com.wesley.beefree.domain.checkin.usecases.HasCompletedTodaysCheckInUseCase
import com.wesley.beefree.domain.entities.EmotionRecord
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.entities.RelapseRecord
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.entities.WeeklyCheckIn
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.onboarding.usecases.ComputeRelapseSuccessRateUseCase
import com.wesley.beefree.infrastructure.storage.adapters.RoomAddictionRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomCheckInRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomLessonRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomMetricsRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomUserProfileRepository
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import com.wesley.beefree.infrastructure.storage.ports.AddictionRepository
import com.wesley.beefree.infrastructure.storage.ports.CheckInRepository
import com.wesley.beefree.infrastructure.storage.ports.MetricsRepository
import com.wesley.beefree.infrastructure.storage.ports.UserProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

sealed class HomeNavigationDestination {
    object CheckIn : HomeNavigationDestination()

    object RecoveryTrajectory : HomeNavigationDestination()

    object FeelingDetails : HomeNavigationDestination()

    object HelpModal : HomeNavigationDestination()

    object TriggerMap : HomeNavigationDestination()
}

data class HomeUiState(
    val user: UserProfile = UserProfile(profileName = "", createdAt = 0L, updatedAt = 0L),
    val psychoeducationMessage: String = "",
    val relapseHistory: List<RelapseRecord> = emptyList(),
    val relapseSuccessRate: Float = 1f,
    val emotionRecords: List<EmotionRecord> = emptyList(),
    val hasCheckedInToday: Boolean = false,
    val treatmentProfile: TreatmentProfile = TreatmentProfile.ACT,
    val anxietySeries: List<Float> = emptyList(),
    val satisfactionSeries: List<Float> = emptyList(),
    val alignedDays: Int = 30,
    val relapseDays: Int = 0,
    val topTriggers: List<Pair<FeelingType, Int>> = emptyList(),
    val anxietyDelta: Int = 0,
    val satisfactionDelta: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
)

class HomeViewModel(
    private val lessonRepository: RoomLessonRepository,
    private val addictionRepository: AddictionRepository,
    private val metricsRepository: MetricsRepository,
    private val userProfileRepository: UserProfileRepository,
    private val checkInRepository: CheckInRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _navigationEvents =
        MutableSharedFlow<HomeNavigationDestination>(extraBufferCapacity = 1)
    val navigationEvents: SharedFlow<HomeNavigationDestination> = _navigationEvents.asSharedFlow()

    fun navigateToCheckIn() {
        _navigationEvents.tryEmit(HomeNavigationDestination.CheckIn)
    }

    fun navigateToRecoveryTrajectory() {
        _navigationEvents.tryEmit(HomeNavigationDestination.RecoveryTrajectory)
    }

    fun navigateToFeelingDetails() {
        _navigationEvents.tryEmit(HomeNavigationDestination.FeelingDetails)
    }

    fun navigateToHelpModal() {
        _navigationEvents.tryEmit(HomeNavigationDestination.HelpModal)
    }

    fun navigateToTriggerMap() {
        _navigationEvents.tryEmit(HomeNavigationDestination.TriggerMap)
    }

    init {
        refresh()
    }

    fun refresh() {
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
                val user = resolveUser() ?: throw IllegalStateException("User profile not found")
                val userId = user.id ?: throw IllegalStateException("User ID not found")
                val userAddiction = userProfileRepository.getAddictionsByUserId(userId).first()
                if (userAddiction.isEmpty()) throw IllegalStateException("User don't have a addiction profile")

                val allPsychoeducationMessagesDeferred =
                    async(Dispatchers.IO) {
                        lessonRepository
                            .getContentByCategory(
                                userAddiction[0].addictionCategoryId,
                            ).first()
                    }
                val allRelapsesDeferred =
                    async(Dispatchers.IO) { addictionRepository.getRelapseHistory().first() }
                val metricsDeferred =
                    async(Dispatchers.IO) { metricsRepository.getEmotionRecords(userId).first() }
                val weeklyCheckInsDeferred =
                    async(Dispatchers.IO) { checkInRepository.getWeeklyCheckIns(userId).first() }
                val dailyCheckInsDeferred =
                    async(Dispatchers.IO) { checkInRepository.getDailyCheckIns(userId).first() }

                val allPsychoeducationMessages = allPsychoeducationMessagesDeferred.await()
                val allRelapses = allRelapsesDeferred.await()
                val metrics = metricsDeferred.await()
                val weeklyCheckIns = weeklyCheckInsDeferred.await()
                val dailyCheckIns = dailyCheckInsDeferred.await()

                val psychoeducationMessage = allPsychoeducationMessages.random().contentText
                val thirtyDaysAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)
                val relapses =
                    allRelapses
                        .filter { it.createdAt >= thirtyDaysAgo }
                        .sortedByDescending { it.createdAt }
                val relapseRate = ComputeRelapseSuccessRateUseCase().execute(relapses)

                val hasCheckedIn =
                    HasCompletedTodaysCheckInUseCase(
                        checkInRepository,
                        DetermineCheckInTypeUseCase(),
                    ).execute(userId, user.createdAt)

                val treatmentProfile =
                    dailyCheckIns.lastOrNull()?.treatmentProfile ?: TreatmentProfile.ACT
                val anxietySeries = computeAnxietySeries(metrics)
                val satisfactionSeries = computeSatisfactionSeries(weeklyCheckIns)
                val alignedDays = computeAlignedDays(allRelapses)
                val topTriggers = computeTopTriggers(metrics)
                val anxietyDelta = computeSeriesDelta(anxietySeries)
                val satisfactionDelta = computeSeriesDelta(satisfactionSeries)

                _uiState.update {
                    it.copy(
                        user = user,
                        psychoeducationMessage = psychoeducationMessage,
                        relapseHistory = relapses,
                        relapseSuccessRate = relapseRate,
                        emotionRecords = metrics,
                        hasCheckedInToday = hasCheckedIn,
                        treatmentProfile = treatmentProfile,
                        anxietySeries = anxietySeries,
                        satisfactionSeries = satisfactionSeries,
                        alignedDays = alignedDays,
                        relapseDays = 30 - alignedDays,
                        topTriggers = topTriggers,
                        anxietyDelta = anxietyDelta,
                        satisfactionDelta = satisfactionDelta,
                        isLoading = false,
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
            }
        }

    private suspend fun resolveUser(): UserProfile? =
        userProfileRepository
            .getAllProfiles()
            .first()
            .firstOrNull()

    private fun computeAnxietySeries(emotionRecords: List<EmotionRecord>): List<Float> {
        val weeks: Long = 7
        val now = System.currentTimeMillis()
        val weekMs = TimeUnit.DAYS.toMillis(weeks)
        val maxIntensityLevel = 5f
        return (weeks downTo 0).map { weeksAgo ->
            val weekEnd = now - weeksAgo * weekMs
            val weekStart = weekEnd - weekMs
            val records =
                emotionRecords.filter { it.feelingType == FeelingType.ANXIETY && it.createdAt in weekStart..weekEnd }
            if (records.isEmpty()) {
                0f
            } else {
                (
                    records
                        .map { it.intensity }
                        .average()
                        .toFloat() / maxIntensityLevel
                ) * 100f
            }
        }
    }

    private fun computeAlignedDays(relapses: List<RelapseRecord>): Int {
        val calendar = Calendar.getInstance()
        val today = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -30)
        val thirtyDaysAgo = calendar.timeInMillis
        val relapseDays =
            relapses
                .filter { it.createdAt in thirtyDaysAgo..today }
                .map {
                    val c = Calendar.getInstance().apply { timeInMillis = it.createdAt }
                    "${c.get(Calendar.DAY_OF_YEAR)}-${c.get(Calendar.YEAR)}"
                }.toSet()
        return (30 - relapseDays.size).coerceAtLeast(0)
    }

    private fun computeTopTriggers(emotionRecords: List<EmotionRecord>): List<Pair<FeelingType, Int>> =
        emotionRecords
            .groupBy { it.feelingType }
            .entries
            .sortedByDescending { it.value.size }
            .take(3)
            .map { it.key to it.value.size }

    private fun computeSeriesDelta(series: List<Float>): Int {
        if (series.size < 2) return 0
        val first = series.firstOrNull { it > 0f } ?: return 0
        val last = series.last()
        return ((last - first) / first * 100).toInt()
    }

    private fun computeSatisfactionSeries(weeklyCheckIns: List<WeeklyCheckIn>): List<Float> {
        val weeks: Long = 7
        val now = System.currentTimeMillis()
        val weekMs = TimeUnit.DAYS.toMillis(weeks)
        val maxRealConnectionLevel = 10f
        return (weeks downTo 0).map { weeksAgo ->
            val weekEnd = now - weeksAgo * weekMs
            val weekStart = weekEnd - weekMs
            val checkIn = weeklyCheckIns.firstOrNull { it.weekStartDate in weekStart..weekEnd }
            val energy = checkIn?.realConnectionEnergy ?: return@map 0f
            (energy / maxRealConnectionLevel) * 100f
        }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val database = AppDatabase.getDatabase(context)
                    @Suppress("UNCHECKED_CAST")
                    return HomeViewModel(
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
                        checkInRepository =
                            RoomCheckInRepository(
                                database.dailyCheckInDao(),
                                database.weeklyCheckInDao(),
                            ),
                    ) as T
                }
            }
    }
}
