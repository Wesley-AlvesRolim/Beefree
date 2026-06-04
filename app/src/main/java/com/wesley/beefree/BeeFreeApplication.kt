package com.wesley.beefree

import android.app.Application
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import com.wesley.beefree.infrastructure.events.workers.DailyCheckInWorker
import com.wesley.beefree.infrastructure.events.workers.EmotionalRecordReminderWorker
import com.wesley.beefree.infrastructure.events.workers.HourlyRiskHobbyReminderWorker
import com.wesley.beefree.infrastructure.events.workers.MidnightRiskCalculationWorker

class BeeFreeApplication :
    Application(),
    Configuration.Provider {
    override val workManagerConfiguration: Configuration
        get() =
            Configuration
                .Builder()
                .setWorkerFactory(
                    DelegatingWorkerFactory().apply {
                        addFactory(EmotionalRecordReminderWorker.factory(this@BeeFreeApplication))
                        addFactory(DailyCheckInWorker.factory(this@BeeFreeApplication))
                        addFactory(HourlyRiskHobbyReminderWorker.factory(this@BeeFreeApplication))
                        addFactory(MidnightRiskCalculationWorker.factory(this@BeeFreeApplication))
                    },
                ).build()

    override fun onCreate() {
        super.onCreate()
    }
}
