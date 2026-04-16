package com.wesley.beefree.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.wesley.beefree.domain.intervention.ClinicalProfileStrategyFactory
import com.wesley.beefree.domain.intervention.EmiTool
import com.wesley.beefree.domain.intervention.ports.ClinicalProfileStrategy
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.storage.adapters.db.AppDatabase
import com.wesley.beefree.ui.components.EMIInterventionUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

const val INTERVENTION_REASON = "INTERVENTION_REASON"

class InterventionActivity : ComponentActivity() {
    companion object {
        var isRunning = false
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onStart() {
        super.onStart()
        isRunning = true
    }

    override fun onStop() {
        isRunning = false
        super.onStop()
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        loadAndDisplay()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        loadAndDisplay()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    private fun loadAndDisplay() {
        val reason = intent.getStringExtra(INTERVENTION_REASON) ?: ""
        showEMI(reason, emptyList(), EmiTool.THOUGHT_RECORD)

        scope.launch {
            val db = AppDatabase.getDatabase(this@InterventionActivity)
            val userId = resolveUserId(db) ?: return@launch
            val result = db.userProfileOnboardingResultDao().getByUser(userId)
            val strategy = resolveStrategy(result?.clinicalProfile)
            val coreValues =
                if (strategy.showsCoreValuesInEmi) {
                    loadCoreValues(db, userId)
                } else {
                    emptyList()
                }
            showEMI(reason, coreValues, strategy.emiTool)
        }
    }

    private suspend fun resolveUserId(db: AppDatabase): Int? =
        runCatching {
            db
                .userProfileDao()
                .getAll()
                .first()
                .firstOrNull()
                ?.id
        }.getOrNull()

    private fun resolveStrategy(profileName: String?): ClinicalProfileStrategy {
        val profile =
            profileName?.let {
                runCatching { TreatmentProfile.valueOf(it) }.getOrNull()
            } ?: TreatmentProfile.PREVENTION
        return ClinicalProfileStrategyFactory.from(profile)
    }

    private suspend fun loadCoreValues(
        db: AppDatabase,
        userId: Int,
    ): List<String> =
        runCatching {
            db
                .userCoreValueDao()
                .getAllByUser(userId)
                .first()
                .map { it.valueName }
        }.getOrDefault(emptyList())

    private fun showEMI(
        reason: String,
        coreValues: List<String>,
        emiTool: EmiTool,
    ) {
        setContent {
            EMIInterventionUI(
                reason = reason,
                coreValues = coreValues,
                emiTool = emiTool,
                onCloseRequest = { finish() },
            )
        }
    }
}
