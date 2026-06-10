package com.wesley.beefree.ui.screens.checkin

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.wesley.beefree.ui.screens.checkin.daily.DailyCheckInRouter
import com.wesley.beefree.ui.viewmodel.CheckInViewModel

@Composable
fun CheckInScreen(
    viewModel: CheckInViewModel,
    onDone: () -> Unit,
) {
    val closeCheckIn: () -> Unit = {
        viewModel.resetDailyFlowState()
        onDone()
    }

    BackHandler(onBack = closeCheckIn)

    DailyCheckInRouter(viewModel = viewModel, onClose = closeCheckIn)
}
