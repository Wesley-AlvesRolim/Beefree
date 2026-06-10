package com.wesley.beefree.ui.components.checkin.daily

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wesley.beefree.domain.checkin.TextWithSuggestionsStep
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.components.designsystem.BeeTextArea
import kotlin.random.Random

@Composable
fun TextWithSuggestionsStepContent(
    spec: TextWithSuggestionsStep,
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        BeeHeadlineSmall(stringByName(spec.titleKey))
        Spacer(modifier = Modifier.height(BeeSpacing.S))
        BeeBodyMedium(
            text = stringByName(spec.subtitleKey),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.L))
        val placeholderKey =
            if (spec.suggestionKeys.isNotEmpty()) {
                spec.suggestionKeys[Random.nextInt(spec.suggestionKeys.size)]
            } else {
                spec.hintKey
            }
        BeeTextArea(
            value = value,
            onValueChange = onChange,
            placeholder = { BeeBodyMedium(stringByName(placeholderKey)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
