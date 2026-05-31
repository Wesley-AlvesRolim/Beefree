package com.wesley.beefree.domain.home.usecases

import com.wesley.beefree.domain.checkin.usecases.HasCompletedTodaysCheckInUseCase
import com.wesley.beefree.domain.entities.EmotionRecord
import com.wesley.beefree.domain.entities.RelapseRecord
import com.wesley.beefree.domain.entities.RiskAssessment
import com.wesley.beefree.domain.entities.RiskTrigger
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.entities.toWeightedRankedTriggers
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.repository.ports.AddictionRepository
import com.wesley.beefree.domain.repository.ports.CheckInRepository
import com.wesley.beefree.domain.repository.ports.LessonRepository
import com.wesley.beefree.domain.repository.ports.MetricsRepository
import com.wesley.beefree.domain.repository.ports.RiskWeightsRepository
import com.wesley.beefree.domain.repository.ports.UserProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import java.util.Calendar
import java.util.concurrent.TimeUnit

sealed class HomeData {
    object OnboardingRequired : HomeData()

    data class Success(
        val user: UserProfile,
        val psychoeducationMessage: String,
        val relapseHistory: List<RelapseRecord>,
        val relapseSuccessRate: Float,
        val emotionRecords: List<EmotionRecord>,
        val hasCheckedInToday: Boolean,
        val treatmentProfile: TreatmentProfile,
        val todayRiskAssessments: List<RiskAssessment>,
        val alignedDays: Int,
        val topTriggers: List<Pair<RiskTrigger, Double>>,
    ) : HomeData()
}

class LoadHomeDataUseCase(
    private val lessonRepository: LessonRepository,
    private val addictionRepository: AddictionRepository,
    private val metricsRepository: MetricsRepository,
    private val userProfileRepository: UserProfileRepository,
    private val checkInRepository: CheckInRepository,
    private val riskWeightsRepository: RiskWeightsRepository,
    private val computeRelapseSuccessRateUseCase: ComputeRelapseSuccessRateUseCase,
    private val hasCompletedTodaysCheckInUseCase: HasCompletedTodaysCheckInUseCase,
) {
    suspend fun execute(): HomeData =
        coroutineScope {
            val user = resolveUser() ?: throw IllegalStateException("User profile not found")
            val userId = user.id ?: throw IllegalStateException("User ID not found")
            val userAddiction = userProfileRepository.getAddictionsByUserId(userId).first().firstOrNull()
            if (userAddiction == null) {
                return@coroutineScope HomeData.OnboardingRequired
            }

            val allPsychoeducationMessagesDeferred =
                async(Dispatchers.IO) {
                    lessonRepository
                        .getContentByCategory(
                            userAddiction.addictionCategoryId,
                        ).first()
                }
            val allRelapsesDeferred =
                async(Dispatchers.IO) { addictionRepository.getRelapseHistory().first() }
            val metricsDeferred =
                async(Dispatchers.IO) { metricsRepository.getEmotionRecords(userId).first() }
            val riskAssessmentsDeferred =
                async(Dispatchers.IO) { metricsRepository.getRiskAssessments(userId).first() }
            val dailyCheckInsDeferred =
                async(Dispatchers.IO) { checkInRepository.getDailyCheckIns(userId).first() }
            val latestSnapshotDeferred =
                async(Dispatchers.IO) { metricsRepository.getLatestRiskFeatureSnapshot(userId) }

            val allPsychoeducationMessages = allPsychoeducationMessagesDeferred.await()
            val allRelapses = allRelapsesDeferred.await()
            val metrics = metricsDeferred.await()
            val riskAssessments = riskAssessmentsDeferred.await()
            val dailyCheckIns = dailyCheckInsDeferred.await()
            val latestSnapshot = latestSnapshotDeferred.await()

            val psychoeducationMessage = allPsychoeducationMessages.randomOrNull()?.contentText ?: ""
            val thirtyDaysAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)
            val relapses =
                allRelapses
                    .filter { it.createdAt >= thirtyDaysAgo }
                    .sortedByDescending { it.createdAt }
            val relapseRate = computeRelapseSuccessRateUseCase.execute(relapses)

            val hasCheckedIn =
                hasCompletedTodaysCheckInUseCase.execute(userId, user.createdAt)

            val treatmentProfile =
                dailyCheckIns.lastOrNull()?.treatmentProfile ?: TreatmentProfile.ACT
            val todayStart =
                Calendar
                    .getInstance()
                    .apply {
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.timeInMillis
            val todayRiskAssessments = riskAssessments.filter { it.createdAt >= todayStart }
            val alignedDays = computeAlignedDays(allRelapses)
            val riskWeights = riskWeightsRepository.getWeights(userId)
            val topTriggers = latestSnapshot?.toWeightedRankedTriggers(riskWeights) ?: emptyList()

            HomeData.Success(
                user = user,
                psychoeducationMessage = psychoeducationMessage,
                relapseHistory = relapses,
                relapseSuccessRate = relapseRate,
                emotionRecords = metrics,
                hasCheckedInToday = hasCheckedIn,
                treatmentProfile = treatmentProfile,
                todayRiskAssessments = todayRiskAssessments,
                alignedDays = alignedDays,
                topTriggers = topTriggers,
            )
        }

    private suspend fun resolveUser(): UserProfile? =
        userProfileRepository
            .getAllProfiles()
            .first()
            .firstOrNull()

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
}
