package com.wesley.beefree.ui.components.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wesley.beefree.R

@Composable
fun BeeSectionProgress(
    activeSection: Int,
    modifier: Modifier = Modifier,
) {
    val labels =
        listOf(
            stringResource(R.string.onboarding_section_sobre_voce),
            stringResource(R.string.onboarding_section_seu_padrao),
            stringResource(R.string.onboarding_section_sua_perspectiva),
            stringResource(R.string.onboarding_section_seu_contexto),
            stringResource(R.string.onboarding_section_sua_direcao),
        )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            labels.forEachIndexed { index, _ ->
                val sectionNumber = index + 1
                val isComplete = sectionNumber < activeSection
                val isActive = sectionNumber == activeSection
                val segmentColor =
                    when {
                        isComplete -> MaterialTheme.colorScheme.primary
                        isActive -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        else -> MaterialTheme.colorScheme.surfaceContainerHigh
                    }
                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(6.dp)
                            .background(segmentColor, RoundedCornerShape(3.dp)),
                )
            }
        }
    }
}
