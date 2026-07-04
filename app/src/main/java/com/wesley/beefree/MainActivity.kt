package com.wesley.beefree

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.wesley.beefree.domain.repository.ports.UserProfileRepository
import com.wesley.beefree.infrastructure.events.workers.DailyCheckInWorker
import com.wesley.beefree.infrastructure.events.workers.EmotionalRecordReminderWorker
import com.wesley.beefree.infrastructure.events.workers.HourlyRiskHobbyReminderWorker
import com.wesley.beefree.infrastructure.events.workers.MidnightRiskCalculationWorker
import com.wesley.beefree.infrastructure.storage.adapters.RoomUserProfileRepository
import com.wesley.beefree.infrastructure.storage.adapters.SharedPreferencesKeyValueStorage
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import com.wesley.beefree.infrastructure.storage.repositories.KeyValueStorageRepository
import com.wesley.beefree.ui.navigation.NavBar
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.widget.SosWidget
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var keyValueStorageRepository: KeyValueStorageRepository
    private lateinit var userProfileRepository: UserProfileRepository
    private var isOnboardingCompleted by mutableStateOf<Boolean?>(null)
    private var openCheckIn by mutableStateOf(false)
    private var openSos by mutableStateOf(false)
    private var openEmotionalRecord by mutableStateOf(false)

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestNotificationPermissionIfNeeded()

        keyValueStorageRepository = KeyValueStorageRepository(SharedPreferencesKeyValueStorage(this))
        val database = AppDatabase.getDatabase(this)
        userProfileRepository = RoomUserProfileRepository(database.userProfileDao(), database.userAddictionDao())
        openCheckIn = intent.getBooleanExtra(DailyCheckInWorker.EXTRA_OPEN_CHECK_IN, false)
        openSos = intent.getBooleanExtra(SosWidget.EXTRA_OPEN_SOS, false)
        openEmotionalRecord = intent.getBooleanExtra(EmotionalRecordReminderWorker.EXTRA_OPEN_EMOTIONAL_RECORD, false)

        DailyCheckInWorker.scheduleCheckInWorker(this)
        EmotionalRecordReminderWorker.scheduleEmotionalRecordWorker(this)
        HourlyRiskHobbyReminderWorker.scheduleHourlyRiskHobbyReminderWorker(this)
        MidnightRiskCalculationWorker.scheduleMidnightWorker(this)

        resolveOnboardingState()

        setContent {
            BeeFreeTheme {
                when (val onboardingCompleted = isOnboardingCompleted) {
                    null ->
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }

                    else ->
                        MainContent(
                            isOnboardingCompleted = onboardingCompleted,
                            onOnboardingFinished = { keyValueStorageRepository.saveOnboardingCompleted(true) },
                            openCheckIn = openCheckIn,
                            openSos = openSos,
                            openEmotionalRecord = openEmotionalRecord,
                        )
                }
            }
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        val isGranted =
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
        if (!isGranted) notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun resolveOnboardingState() {
        lifecycleScope.launch {
            val hasProfile = userProfileRepository.getAllProfiles().first().isNotEmpty()
            if (hasProfile != keyValueStorageRepository.isOnboardingCompleted()) {
                keyValueStorageRepository.saveOnboardingCompleted(hasProfile)
            }
            isOnboardingCompleted = hasProfile
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
    isOnboardingCompleted: Boolean = true,
    onOnboardingFinished: () -> Unit = {},
    openCheckIn: Boolean = false,
    openSos: Boolean = false,
    openEmotionalRecord: Boolean = false,
) {
    NavBar(
        isOnboardingCompleted = isOnboardingCompleted,
        onOnboardingFinished = onOnboardingFinished,
        openCheckIn = openCheckIn,
        openSos = openSos,
        openEmotionalRecord = openEmotionalRecord,
    )
}
