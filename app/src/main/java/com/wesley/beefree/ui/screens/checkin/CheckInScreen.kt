package com.wesley.beefree.ui.screens.checkin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.wesley.beefree.domain.checkin.CheckInType
import com.wesley.beefree.ui.viewmodel.CheckInViewModel

@Composable
fun CheckInScreen(
    viewModel: CheckInViewModel,
    onDone: () -> Unit,
) {
    val checkInType by viewModel.checkInType.collectAsState()
    val isCompleted by viewModel.isCompleted.collectAsState()

    LaunchedEffect(isCompleted) {
        if (isCompleted) onDone()
    }

    when (checkInType) {
        CheckInType.DAILY -> DailyCheckInScreen(viewModel = viewModel)
        CheckInType.WEEKLY -> WeeklyCheckInScreen(viewModel = viewModel)
    }
}
