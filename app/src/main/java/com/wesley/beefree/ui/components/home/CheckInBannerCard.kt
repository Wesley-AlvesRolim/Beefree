package com.wesley.beefree.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EnergySavingsLeaf
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@Composable
fun CheckInBannerCard(
    treatmentProfile: TreatmentProfile,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = BeeSpacing.M),
        shape = RoundedCornerShape(BeeSpacing.M),
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = BeeSpacing.M, vertical = BeeSpacing.S),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
        ) {
            Surface(
                modifier = Modifier.size(BeeSpacing.XL),
                shape = RoundedCornerShape(BeeSpacing.M),
                color = MaterialTheme.colorScheme.primary,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.EnergySavingsLeaf,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(BeeSpacing.L),
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                BeeLabelMedium(
                    text = "${stringResource(R.string.check_in_daily_title)} - ${treatmentProfile.name}",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                BeeBodySmall(
                    text = stringResource(R.string.home_checkin_banner_sub),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }

            BeeLabelMedium(
                text = stringResource(R.string.home_checkin_banner_cta),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}
