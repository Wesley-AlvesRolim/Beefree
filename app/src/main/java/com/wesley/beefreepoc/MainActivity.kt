package com.wesley.beefreepoc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.wesley.beefreepoc.notifications.CheckAccessibilityWorker
import com.wesley.beefreepoc.storage.adapters.SharedPreferencesKeyValueStorage
import com.wesley.beefreepoc.storage.repositories.KeyValueStorageRepository
import com.wesley.beefreepoc.ui.navigation.NavBar
import com.wesley.beefreepoc.ui.screens.OnboardingScreen
import com.wesley.beefreepoc.ui.theme.BeeFreePOCTheme

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
            BeeFreePOCTheme {
                if (onboardingCompleted) {
                    MainContent()
                } else {
                    OnboardingScreen(onFinish = {
                        keyValueStorageRepository.saveOnboardingCompleted(true)
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
