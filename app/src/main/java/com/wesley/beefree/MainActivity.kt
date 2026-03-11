package com.wesley.beefree

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.wesley.beefree.notifications.CheckAccessibilityWorker
import com.wesley.beefree.storage.adapters.SharedPreferencesKeyValueStorage
import com.wesley.beefree.storage.repositories.KeyValueStorageRepository
import com.wesley.beefree.ui.navigation.NavBar
import com.wesley.beefree.ui.screens.onboarding.OnboardingScreen
import com.wesley.beefree.ui.theme.BeeFreeTheme

class MainActivity : ComponentActivity() {
    private lateinit var keyValueStorageRepository: KeyValueStorageRepository
    private var onboardingCompleted by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        keyValueStorageRepository = KeyValueStorageRepository(SharedPreferencesKeyValueStorage(this))
        onboardingCompleted = keyValueStorageRepository.isOnboardingCompleted()
        CheckAccessibilityWorker.scheduleNotificationWorker(this)

        setContent {
            BeeFreeTheme {
                if (onboardingCompleted) {
                    MainContent()
                } else {
                    OnboardingScreen(onFinish = {
                        onboardingCompleted = true
                    })
                }
            }
        }
    }
}

@Composable
fun MainContent() {
    NavBar()
}
