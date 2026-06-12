package com.wesley.beefree.ui.screens.checkin.daily

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeButtonPrimary
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineMedium
import com.wesley.beefree.ui.components.designsystem.BeeLabelLarge
import com.wesley.beefree.ui.components.designsystem.BeeMascot
import com.wesley.beefree.ui.components.designsystem.BeeMascotEmotion
import com.wesley.beefree.ui.components.designsystem.BeeMascotSize
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.theme.BeeFreeTheme

@Composable
fun DailyCheckInDoneScreen(onClose: () -> Unit) {
    Scaffold { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = BeeSpacing.L),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxHeight(0.9f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Spacer(modifier = Modifier.height(BeeSpacing.XXL))
                BeeMascot(emotion = BeeMascotEmotion.Excited, size = BeeMascotSize.Hero)
                Spacer(modifier = Modifier.height(BeeSpacing.L))
                BeeHeadlineMedium(
                    text = stringResource(R.string.daily_checkin_done_title),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(BeeSpacing.S))
                BeeBodyMedium(
                    text = stringResource(R.string.daily_checkin_done_subtitle),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
            BeeButtonPrimary(
                onClick = onClose,
                modifier = Modifier.fillMaxWidth(),
            ) {
                BeeLabelLarge(
                    text = stringResource(R.string.daily_checkin_done_back),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

@Preview()
@Composable()
fun DailyCheckInDoneScreenPreview() {
    BeeFreeTheme {
        DailyCheckInDoneScreen(onClose = {})
    }
}
