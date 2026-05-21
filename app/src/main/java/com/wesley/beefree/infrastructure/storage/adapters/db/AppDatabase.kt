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
        UserProfileEntity::class,
        AddictionCategoryEntity::class,
        UserAddictionEntity::class,
        UserOnboardingSessionEntity::class,
        UserCoreValueEntity::class,
        UserHobbyEntity::class,
        UserObjectiveEntity::class,
        UserSymptomEntity::class,
        AppUsageSessionEntity::class,
        RelapseRecordEntity::class,
        EmotionRecordEntity::class,
        DailyCheckInEntity::class,
        WeeklyCheckInEntity::class,
        InterventionRecordEntity::class,
        CognitiveThoughtRecordEntity::class,
        InterventionValueLinkEntity::class,
        PsychoeducationContentEntity::class,
        RiskFeatureSnapshotEntity::class,
        RiskAssessmentEntity::class,
        UserSupportContactEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDAO

    abstract fun addictionCategoryDao(): AddictionCategoryDAO

    abstract fun userAddictionDao(): UserAddictionDAO

    abstract fun userOnboardingSessionDao(): UserOnboardingSessionDAO

    abstract fun userCoreValueDao(): UserCoreValueDAO

    abstract fun userHobbyDao(): UserHobbyDAO

    abstract fun userObjectiveDao(): UserObjectiveDAO

    abstract fun userSymptomDao(): UserSymptomDAO

    abstract fun appUsageSessionDao(): AppUsageSessionDAO

    abstract fun relapseRecordDao(): RelapseRecordDAO

    abstract fun emotionRecordDao(): EmotionRecordDAO

    abstract fun dailyCheckInDao(): DailyCheckInDAO

    abstract fun weeklyCheckInDao(): WeeklyCheckInDAO

    abstract fun interventionRecordDao(): InterventionRecordDAO

    abstract fun cognitiveThoughtRecordDao(): CognitiveThoughtRecordDAO

    abstract fun interventionValueLinkDao(): InterventionValueLinkDAO

    abstract fun psychoeducationContentDao(): PsychoeducationContentDAO

    abstract fun riskFeatureSnapshotDao(): RiskFeatureSnapshotDAO

    abstract fun riskAssessmentDao(): RiskAssessmentDAO

    abstract fun userSupportContactDao(): UserSupportContactDAO

    companion object {
        @Volatile
        private var databaseInstance: AppDatabase? = null

        @JvmStatic
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
