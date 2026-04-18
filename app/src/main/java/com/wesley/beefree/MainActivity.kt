package com.wesley.beefree

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wesley.beefree.infrastructure.workers.DailyCheckInWorker
import com.wesley.beefree.notifications.CheckAccessibilityWorker
import com.wesley.beefree.storage.adapters.SharedPreferencesKeyValueStorage
import com.wesley.beefree.storage.repositories.KeyValueStorageRepository
import com.wesley.beefree.ui.navigation.NavBar
import com.wesley.beefree.ui.screens.onboarding.OnboardingScreen
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.OnboardingViewModelImpl

class MainActivity : ComponentActivity() {
    private lateinit var keyValueStorageRepository: KeyValueStorageRepository
    private var onboardingCompleted by mutableStateOf(false)
    private var openCheckIn by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        keyValueStorageRepository = KeyValueStorageRepository(SharedPreferencesKeyValueStorage(this))
        onboardingCompleted = keyValueStorageRepository.isOnboardingCompleted()
        openCheckIn = intent.getBooleanExtra(DailyCheckInWorker.EXTRA_OPEN_CHECK_IN, false)

        CheckAccessibilityWorker.scheduleNotificationWorker(this)
        DailyCheckInWorker.scheduleCheckInWorker(this)

        setContent {
            BeeFreeTheme {
                if (onboardingCompleted) {
                    MainContent(openCheckIn = openCheckIn)
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
}

@Composable
fun MainContent(openCheckIn: Boolean = false) {
    NavBar(openCheckIn = openCheckIn)
}
