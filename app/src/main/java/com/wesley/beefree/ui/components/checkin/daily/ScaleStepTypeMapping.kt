package com.wesley.beefree.ui.components.checkin.daily

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.wesley.beefree.R
import com.wesley.beefree.domain.checkin.ScaleStepType
import com.wesley.beefree.ui.theme.Secondary
import com.wesley.beefree.ui.theme.Tertiary

data class ScaleStepTypeUI(
    val color: Color,
    val icon: ImageVector,
    val labelRes: Int,
    val hintRes: Int,
)

fun mapScaleStepType(type: ScaleStepType): ScaleStepTypeUI =
    when (type) {
        ScaleStepType.CRAVING ->
            ScaleStepTypeUI(
                color = Secondary,
                icon = Icons.Filled.EmojiEmotions,
                labelRes = R.string.scale_step_craving_label,
                hintRes = R.string.scale_step_craving_hint,
            )
        ScaleStepType.ACCEPTANCE ->
            ScaleStepTypeUI(
                color = Tertiary,
                icon = Icons.Filled.EmojiEmotions,
                labelRes = R.string.scale_step_acceptance_label,
                hintRes = R.string.scale_step_acceptance_hint,
            )
        ScaleStepType.PRESENCE ->
            ScaleStepTypeUI(
                color = Secondary,
                icon = Icons.Filled.EmojiEmotions,
                labelRes = R.string.scale_step_presence_label,
                hintRes = R.string.scale_step_presence_hint,
            )
    }
