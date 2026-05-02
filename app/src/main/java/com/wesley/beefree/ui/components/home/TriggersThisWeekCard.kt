package com.wesley.beefree.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeCardSection
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@Composable
fun TriggersThisWeekCard(
    topTriggers: List<Pair<FeelingType, Int>>,
    onSeeAll: () -> Unit,
) {
    BeeCardSection(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = BeeSpacing.M),
    ) {
        Column(modifier = Modifier.padding(BeeSpacing.M)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BeeHeadlineSmall(stringResource(R.string.home_triggers_week_title))
                Surface(onClick = onSeeAll, color = Color.Transparent) {
                    BeeLabelMedium(
                        text = stringResource(R.string.home_triggers_see_all),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            if (topTriggers.isEmpty()) {
                Spacer(Modifier.height(BeeSpacing.S))
                BeeBodySmall(
                    text = stringResource(R.string.triggers_empty),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Spacer(Modifier.height(BeeSpacing.S))
                Column(verticalArrangement = Arrangement.spacedBy(BeeSpacing.S)) {
                    val maxCount = topTriggers.firstOrNull()?.second ?: 1
                    topTriggers.forEach { (feelingType, count) ->
                        TriggerEmotionRow(
                            feelingType = feelingType,
                            count = count,
                            maxCount = maxCount,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TriggerEmotionRow(
    feelingType: FeelingType,
    count: Int,
    maxCount: Int,
) {
    val icon =
        when (feelingType) {
            FeelingType.ANXIETY -> Icons.Default.Psychology
            FeelingType.STRESS -> Icons.Default.Whatshot
            FeelingType.LONELINESS -> Icons.Default.FavoriteBorder
            FeelingType.BOREDOM -> Icons.Default.AccessTime
        }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
    ) {
        Surface(
            modifier = Modifier.size(30.dp),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.tertiaryContainer,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.size(14.dp),
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                BeeLabelMedium(feelingType.name.lowercase().replaceFirstChar { it.uppercase() })
                BeeBodySmall(
                    text = "$count×",
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Spacer(Modifier.height(BeeSpacing.XS))
            val fraction = (count.toFloat() / maxCount).coerceIn(0f, 1f)
            val barColor =
                when {
                    fraction > 0.7f -> MaterialTheme.colorScheme.primary
                    fraction > 0.4f -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.tertiaryContainer
                }
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(fraction)
                            .height(4.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(barColor),
                )
            }
        }
    }
}
