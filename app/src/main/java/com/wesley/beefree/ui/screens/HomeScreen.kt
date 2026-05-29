package com.wesley.beefree.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.RelapseRecord
import com.wesley.beefree.domain.entities.RiskAssessment
import com.wesley.beefree.domain.entities.RiskTrigger
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeButtonGhost
import com.wesley.beefree.ui.components.designsystem.BeeFAB
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.components.home.CheckInBannerCard
import com.wesley.beefree.ui.components.home.EvolutionCalendarCard
import com.wesley.beefree.ui.components.home.GreetingSection
import com.wesley.beefree.ui.components.home.PsychoeducationCard
import com.wesley.beefree.ui.components.home.RiskAssessmentTodayCard
import com.wesley.beefree.ui.components.home.TriggersThisWeekCard
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.HomeViewModel
import java.util.Calendar

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.onHomeVisible()
    }

    val lifecycleOwner = rememberUpdatedState(newValue = LocalLifecycleOwner.current)
    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) viewModel.refresh()
            }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    uiState.error?.let { uiText ->
        AlertDialog(
            onDismissRequest = { viewModel.resetError() },
            confirmButton = {
                BeeButtonGhost(onClick = { viewModel.resetError() }) {
                    BeeLabelMedium(stringResource(android.R.string.ok))
                }
            },
            text = {
                BeeBodyMedium(uiText)
            },
        )
    }

    HomeScreenContent(
        user = uiState.user,
        psychoeducationMessage = uiState.psychoeducationMessage,
        relapseHistory = uiState.relapseHistory,
        relapseSuccessRate = uiState.relapseSuccessRate,
        hasCheckedInToday = uiState.hasCheckedInToday,
        treatmentProfile = uiState.treatmentProfile,
        todayRiskAssessments = uiState.todayRiskAssessments,
        alignedDays = uiState.alignedDays,
        relapseDays = uiState.relapseDays,
        topTriggers = uiState.topTriggers,
        onOpenCheckIn = viewModel::navigateToCheckIn,
        onOpenHelpIntervention = viewModel::navigateToHelpIntervention,
    )
}

@Composable
fun HomeScreenContent(
    user: UserProfile,
    psychoeducationMessage: String?,
    relapseHistory: List<RelapseRecord>,
    relapseSuccessRate: Float,
    hasCheckedInToday: Boolean = false,
    treatmentProfile: TreatmentProfile = TreatmentProfile.ACT,
    todayRiskAssessments: List<RiskAssessment> = emptyList(),
    alignedDays: Int = 30,
    relapseDays: Int = 0,
    topTriggers: List<Pair<RiskTrigger, Double>> = emptyList(),
    onOpenCheckIn: () -> Unit = {},
    onOpenHelpIntervention: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
        ) {
            item { GreetingSection(name = user.profileName) }

            if (hasCheckedInToday && psychoeducationMessage != null) {
                item {
                    PsychoeducationCard(psychoeducationMessage)
                }
            }

            if (!hasCheckedInToday) {
                item {
                    CheckInBannerCard(
                        treatmentProfile = treatmentProfile,
                        onClick = onOpenCheckIn,
                    )
                }
            }

            item {
                EvolutionCalendarCard(
                    relapseHistory = relapseHistory,
                    relapseSuccessRate = relapseSuccessRate,
                    alignedDays = alignedDays,
                    setbackDays = relapseDays,
                )
            }

            item {
                RiskAssessmentTodayCard(
                    assessments = todayRiskAssessments,
                )
            }

            item {
                TriggersThisWeekCard(
                    topTriggers = topTriggers,
                )
            }
        }

        BeeFAB(
            onClick = onOpenHelpIntervention,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(BeeSpacing.M),
        ) {
            Icon(
                imageVector = Icons.Default.Sos,
                contentDescription = stringResource(R.string.quick_help_button_description),
                modifier = Modifier.fillMaxSize(0.5f),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val now = System.currentTimeMillis()
    val dayMillis = 86_400_000L

    BeeFreeTheme {
        HomeScreenContent(
            user =
                UserProfile(
                    profileName = "Wesley",
                    createdAt = now - dayMillis * 15,
                    updatedAt = 0L,
                ),
            psychoeducationMessage = "Você não precisa ser perfeito, só precisa continuar.",
            relapseHistory =
                listOf(
                    RelapseRecord(
                        addictionCategoryId = 1,
                        keywordDetected = "test",
                        createdAt = now - dayMillis * 5,
                    ),
                ),
            relapseSuccessRate = 0.84f,
            hasCheckedInToday = true,
            treatmentProfile = TreatmentProfile.ACT,
            todayRiskAssessments =
                listOf(
                    RiskAssessment(
                        userProfileId = 1,
                        riskScore = 75,
                        timeWindow =
                            Calendar
                                .getInstance()
                                .apply {
                                    set(Calendar.HOUR_OF_DAY, 6)
                                }.timeInMillis
                                .toString(),
                        createdAt = now - dayMillis / 2,
                    ),
                    RiskAssessment(
                        userProfileId = 1,
                        riskScore = 45,
                        timeWindow =
                            Calendar
                                .getInstance()
                                .apply {
                                    set(Calendar.HOUR_OF_DAY, 12)
                                }.timeInMillis
                                .toString(),
                        createdAt = now - dayMillis / 4,
                    ),
                    RiskAssessment(
                        userProfileId = 1,
                        riskScore = 20,
                        timeWindow =
                            Calendar
                                .getInstance()
                                .apply {
                                    set(Calendar.HOUR_OF_DAY, 18)
                                }.timeInMillis
                                .toString(),
                        createdAt = now - dayMillis / 8,
                    ),
                ),
            alignedDays = 25,
            relapseDays = 1,
            topTriggers =
                listOf(
                    RiskTrigger.CRAVING to 90.0,
                    RiskTrigger.STRESS to 70.0,
                    RiskTrigger.BOREDOM to 50.0,
                ),
        )
    }
}
