package com.wesley.beefree.ui.screens.checkin.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeCardPrimary
import com.wesley.beefree.ui.components.designsystem.BeeLabelLarge
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@Composable
fun DailyCheckInProgressCard() {
    BeeCardPrimary(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(BeeSpacing.L),
            horizontalArrangement = Arrangement.spacedBy(BeeSpacing.M),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
            )
            Column(modifier = Modifier.weight(1f)) {
                BeeLabelLarge(
                    text = stringResource(R.string.check_in_progress_title),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Spacer(modifier = Modifier.height(BeeSpacing.XS))
                BeeBodySmall(
                    text = stringResource(R.string.check_in_progress_body),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}
