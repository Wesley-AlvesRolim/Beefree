package com.wesley.beefree.ui.screens.checkin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineMedium
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.screens.checkin.components.DailyCheckInDopamineSlider
import com.wesley.beefree.ui.screens.checkin.components.DailyCheckInMotivationSlider
import com.wesley.beefree.ui.screens.checkin.components.DailyCheckInProgressCard
import com.wesley.beefree.ui.screens.checkin.components.DailyCheckInSubmitButton
import com.wesley.beefree.ui.screens.checkin.components.DailyCheckInThoughtRecordSection
import com.wesley.beefree.ui.screens.checkin.components.DailyCheckInWeatherMoodSection
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.CheckInViewModel

@Composable
fun DailyCheckInScreen(viewModel: CheckInViewModel) {
    val weatherMood by viewModel.weatherMood.collectAsState()
    val dopamineLevel by viewModel.dopamineLevel.collectAsState()
    val mood by viewModel.mood.collectAsState()
    val anxietyLevel by viewModel.anxietyLevel.collectAsState()

    DailyCheckInScreen(
        weatherMood = weatherMood,
        dopamineLevel = dopamineLevel,
        mood = mood,
        anxietyLevel = anxietyLevel,
        onWeatherMoodChange = { viewModel.updateWeatherMood(it) },
        onDopamineLevelChange = { viewModel.updateDopamineLevel(it) },
        onMoodChange = { viewModel.updateMood(it) },
        onAnxietyLevelChange = { viewModel.updateAnxietyLevel(it) },
        onSubmit = { viewModel.submit() },
    )
}

@Composable
private fun DailyCheckInScreen(
    weatherMood: Int?,
    dopamineLevel: Int,
    mood: String,
    anxietyLevel: Int?,
    onWeatherMoodChange: (Int) -> Unit,
    onDopamineLevelChange: (Int) -> Unit,
    onMoodChange: (String) -> Unit,
    onAnxietyLevelChange: (Int?) -> Unit,
    onSubmit: () -> Unit,
) {
    Scaffold { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = BeeSpacing.L)
                    .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(BeeSpacing.XL))

            BeeHeadlineMedium(stringResource(R.string.check_in_daily_title))
            Spacer(modifier = Modifier.height(BeeSpacing.S))
            BeeBodyMedium(
                text = stringResource(R.string.check_in_subtitle),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(BeeSpacing.XL))

            DailyCheckInWeatherMoodSection(
                weatherMood = weatherMood,
                onWeatherMoodChange = onWeatherMoodChange,
            )

            Spacer(modifier = Modifier.height(BeeSpacing.L))

            DailyCheckInMotivationSlider(
                dopamineLevel = dopamineLevel,
                onDopamineLevelChange = onDopamineLevelChange,
            )

            Spacer(modifier = Modifier.height(BeeSpacing.L))

            DailyCheckInDopamineSlider(
                anxietyLevel = anxietyLevel,
                onAnxietyLevelChange = onAnxietyLevelChange,
            )

            Spacer(modifier = Modifier.height(BeeSpacing.L))

            DailyCheckInThoughtRecordSection(
                mood = mood,
                onMoodChange = onMoodChange,
            )

            Spacer(modifier = Modifier.height(BeeSpacing.L))

            DailyCheckInProgressCard()

            Spacer(modifier = Modifier.height(BeeSpacing.XL))

            DailyCheckInSubmitButton(
                enabled = weatherMood != null,
                onSubmit = onSubmit,
            )

            Spacer(modifier = Modifier.height(BeeSpacing.XL))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DailyCheckInPreview() {
    BeeFreeTheme {
        DailyCheckInScreen(
            weatherMood = 3,
            dopamineLevel = 4,
            mood = "",
            anxietyLevel = null,
            onWeatherMoodChange = {},
            onDopamineLevelChange = {},
            onMoodChange = {},
            onAnxietyLevelChange = {},
            onSubmit = {},
        )
    }
}
