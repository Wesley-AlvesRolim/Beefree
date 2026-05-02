package com.wesley.beefree.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeMascot
import com.wesley.beefree.ui.components.designsystem.BeeMascotSize
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@Composable()
fun PsychoeducationCard(psychoeducationMessage: String) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = BeeSpacing.M, vertical = BeeSpacing.XS),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BeeSpacing.M),
    ) {
        BeeMascot(size = BeeMascotSize.Small)
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(BeeSpacing.M))
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    .padding(BeeSpacing.S),
        ) {
            BeeBodySmall(text = psychoeducationMessage)
        }
    }
}
