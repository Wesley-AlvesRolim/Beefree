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
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.entities.RelapseRecord
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeButtonGhost
import com.wesley.beefree.ui.components.designsystem.BeeFAB
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.components.home.CheckInBannerCard
import com.wesley.beefree.ui.components.home.EvolutionCalendarCard
import com.wesley.beefree.ui.components.home.FeelingEvolutionChartCard
import com.wesley.beefree.ui.components.home.GreetingSection
import com.wesley.beefree.ui.components.home.PsychoeducationCard
import com.wesley.beefree.ui.components.home.TriggersThisWeekCard
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.HomeViewModel

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
        anxietySeries = uiState.anxietySeries,
        satisfactionSeries = uiState.satisfactionSeries,
        alignedDays = uiState.alignedDays,
        relapseDays = uiState.relapseDays,
        topTriggers = uiState.topTriggers,
        anxietyDelta = uiState.anxietyDelta,
        satisfactionDelta = uiState.satisfactionDelta,
        onOpenCheckIn = viewModel::navigateToCheckIn,
        onOpenRecoveryTrajectory = viewModel::navigateToRecoveryTrajectory,
        onOpenTheFeelingDetails = viewModel::navigateToFeelingDetails,
        onOpenHelpIntervention = viewModel::navigateToHelpIntervention,
        onOpenTriggerMap = viewModel::navigateToTriggerMap,
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
    anxietySeries: List<Float> = emptyList(),
    satisfactionSeries: List<Float> = emptyList(),
    alignedDays: Int = 30,
    relapseDays: Int = 0,
    topTriggers: List<Pair<FeelingType, Int>> = emptyList(),
    anxietyDelta: Int = 0,
    satisfactionDelta: Int = 0,
    onOpenCheckIn: () -> Unit = {},
    onOpenRecoveryTrajectory: () -> Unit = {},
    onOpenTheFeelingDetails: () -> Unit = {},
    onOpenHelpIntervention: () -> Unit = {},
    onOpenTriggerMap: () -> Unit = {},
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
                    onClick = onOpenRecoveryTrajectory,
                )
            }

            item {
                FeelingEvolutionChartCard(
                    anxietySeries = anxietySeries,
                    satisfactionSeries = satisfactionSeries,
                    anxietyDelta = anxietyDelta,
                    satisfactionDelta = satisfactionDelta,
                    onClick = onOpenTheFeelingDetails,
                )
            }

            item {
                TriggersThisWeekCard(
                    topTriggers = topTriggers,
                    onSeeAll = onOpenTriggerMap,
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
            anxietySeries = listOf(78f, 70f, 66f, 60f, 52f, 48f, 42f, 36f),
            satisfactionSeries = listOf(22f, 28f, 35f, 44f, 50f, 58f, 64f, 70f),
            alignedDays = 25,
            relapseDays = 1,
            topTriggers = listOf(FeelingType.CRAVING to 3, FeelingType.STRESS to 2, FeelingType.BOREDOM to 1),
            anxietyDelta = -53,
            satisfactionDelta = 218,
        )
    }
}
