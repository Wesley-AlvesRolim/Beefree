package com.wesley.beefree.infrastructure.storage.adapters.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wesley.beefree.infrastructure.storage.adapters.db.converters.DateConverter
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.*
import com.wesley.beefree.infrastructure.storage.adapters.db.seeds.seed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        AddictionTypeEntity::class,
        AddictionKeywordEntity::class,
        RelapseHistoryEntity::class,
        UserProfileEntity::class,
        UserProfileAddictionEntity::class,
        AppRestrictionEntity::class,
        AppUseEntity::class,
        SupportContactEntity::class,
        BlockScreenConfigEntity::class,
        MotivationalMessageEntity::class,
        OnboardingScaleAnswerEntity::class,
        UserProfileOnboardingResultEntity::class,
        UserCoreValueEntity::class,
        UserHobbyEntity::class,
        UserObjectiveEntity::class,
        UserSymptomEntity::class,
        DailyCheckInEntity::class,
        WeeklyCheckInEntity::class,
        TriggerMappingEntity::class,
        InterventionLogEntity::class,
        ThoughtRecordEntity::class,
        UrgeSurfingSessionEntity::class,
        MicroActivityEntity::class,
        DailyMicroActivityLogEntity::class,
        DailyLessonEntity::class,
        UserLessonProgressEntity::class,
        HolisticMetricsEntity::class,
        RiskPredictionEntity::class,
        NotificationLogEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun addictionTypeDao(): AddictionTypeDAO

    abstract fun addictionKeywordDao(): AddictionKeywordDAO

    abstract fun relapseHistoryDao(): RelapseHistoryDAO

    abstract fun userProfileDao(): UserProfileDAO

    abstract fun userProfileAddictionDao(): UserProfileAddictionDAO

    abstract fun appRestrictionDao(): AppRestrictionDAO

    abstract fun appUseDao(): AppUseDAO

    abstract fun supportContactDao(): SupportContactDAO

    abstract fun blockScreenConfigDao(): BlockScreenConfigDAO

    abstract fun motivationalMessageDao(): MotivationalMessageDAO

    abstract fun onboardingScaleAnswerDao(): OnboardingScaleAnswerDAO

    abstract fun userProfileOnboardingResultDao(): UserProfileOnboardingResultDAO

    abstract fun userCoreValueDao(): UserCoreValueDAO

    abstract fun userHobbyDao(): UserHobbyDAO

    abstract fun userObjectiveDao(): UserObjectiveDAO

    abstract fun userSymptomDao(): UserSymptomDAO

    abstract fun dailyCheckInDao(): DailyCheckInDAO

    abstract fun weeklyCheckInDao(): WeeklyCheckInDAO

    abstract fun triggerMappingDao(): TriggerMappingDAO

    abstract fun interventionLogDao(): InterventionLogDAO

    abstract fun thoughtRecordDao(): ThoughtRecordDAO

    abstract fun urgeSurfingSessionDao(): UrgeSurfingSessionDAO

    abstract fun microActivityDao(): MicroActivityDAO

    abstract fun dailyMicroActivityLogDao(): DailyMicroActivityLogDAO

    abstract fun dailyLessonDao(): DailyLessonDAO

    abstract fun userLessonProgressDao(): UserLessonProgressDAO

    abstract fun holisticMetricsDao(): HolisticMetricsDAO

    abstract fun riskPredictionDao(): RiskPredictionDAO

    abstract fun notificationLogDao(): NotificationLogDAO

    companion object {
        @Volatile
        private var databaseInstance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            databaseInstance ?: synchronized(this) {
                val instance =
                    Room
                        .databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "bee_free_db",
                        ).build()
                databaseInstance = instance
                CoroutineScope(Dispatchers.IO).launch {
                    seed(instance)
                }
                instance
            }
    }
}
