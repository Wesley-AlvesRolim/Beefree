package com.wesley.beefree

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import androidx.work.WorkManager
import com.wesley.beefree.infrastructure.events.workers.DailyCheckInWorker
import com.wesley.beefree.infrastructure.events.workers.EmotionalRecordReminderWorker
import com.wesley.beefree.infrastructure.storage.adapters.SharedPreferencesKeyValueStorage
import com.wesley.beefree.infrastructure.storage.repositories.KeyValueStorageRepository
import com.wesley.beefree.ui.navigation.NavBar
import com.wesley.beefree.ui.screens.onboarding.OnboardingScreen
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.OnboardingViewModelImpl
import com.wesley.beefree.ui.widget.SosWidget

class MainActivity : ComponentActivity() {
    private lateinit var keyValueStorageRepository: KeyValueStorageRepository
    private var onboardingCompleted by mutableStateOf(false)
    private var openCheckIn by mutableStateOf(false)
    private var openSos by mutableStateOf(false)
    private var openEmotionalRecord by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        keyValueStorageRepository = KeyValueStorageRepository(SharedPreferencesKeyValueStorage(this))
        onboardingCompleted = keyValueStorageRepository.isOnboardingCompleted()
        openCheckIn = intent.getBooleanExtra(DailyCheckInWorker.EXTRA_OPEN_CHECK_IN, false)
        openSos = intent.getBooleanExtra(SosWidget.EXTRA_OPEN_SOS, false)
        openEmotionalRecord = intent.getBooleanExtra(EmotionalRecordReminderWorker.EXTRA_OPEN_EMOTIONAL_RECORD, false)

        val workerFactory =
            DelegatingWorkerFactory().apply {
                addFactory(EmotionalRecordReminderWorker.factory(application))
                addFactory(DailyCheckInWorker.factory(application))
            }
        WorkManager.initialize(
            this,
            Configuration
                .Builder()
                .setWorkerFactory(workerFactory)
                .build(),
        )
        DailyCheckInWorker.scheduleCheckInWorker(this)
        EmotionalRecordReminderWorker.scheduleEmotionalRecordWorker(this)

        setContent {
            BeeFreeTheme {
                if (onboardingCompleted) {
                    MainContent(openCheckIn = openCheckIn, openSos = openSos, openEmotionalRecord = openEmotionalRecord)
                } else {
                    val onboardingViewModel: OnboardingViewModelImpl =
                        viewModel(factory = OnboardingViewModelImpl.factory(application))
                    OnboardingScreen(
                        onFinish = { onboardingCompleted = true },
                        viewModel = onboardingViewModel,
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        openCheckIn = intent.getBooleanExtra(DailyCheckInWorker.EXTRA_OPEN_CHECK_IN, false)
        openSos = intent.getBooleanExtra(SosWidget.EXTRA_OPEN_SOS, false)
        openEmotionalRecord = intent.getBooleanExtra(EmotionalRecordReminderWorker.EXTRA_OPEN_EMOTIONAL_RECORD, false)
    }
}

@Composable
fun MainContent(
    openCheckIn: Boolean = false,
    openSos: Boolean = false,
    openEmotionalRecord: Boolean = false,
) {
    NavBar(openCheckIn = openCheckIn, openSos = openSos, openEmotionalRecord = openEmotionalRecord)
}
