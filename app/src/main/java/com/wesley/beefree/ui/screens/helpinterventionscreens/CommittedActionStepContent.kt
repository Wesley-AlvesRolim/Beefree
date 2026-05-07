package com.wesley.beefree.ui.screens.helpinterventionscreens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.domain.intervention.ActionSuggestion
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeCardSection
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineLarge
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeech
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeechTone
import com.wesley.beefree.ui.components.designsystem.BeeSelectableOption
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.components.designsystem.BeeTextField
import com.wesley.beefree.utils.getResIdOrFallback

@Composable
fun CommittedActionStepContent(
    titleKey: String,
    suggestions: List<ActionSuggestion>,
    selectedValue: String,
    customValue: String,
    speechTone: BeeMascotSpeechTone = BeeMascotSpeechTone.Primary,
    speechKey: String? = null,
    onSelectedChange: (String) -> Unit,
    onCustomValueChange: (String) -> Unit,
    onAnswerChange: (Any) -> Unit,
) {
    val context = LocalContext.current
    val groupedSuggestions = remember(suggestions) { suggestions.groupBy { it.category } }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = BeeSpacing.M),
        verticalArrangement = Arrangement.spacedBy(BeeSpacing.L),
    ) {
        ActionStepHeader(
            title = stringResource(getResIdOrFallback(context, titleKey)),
            speechKey = speechKey,
            speechTone = speechTone,
            context = context,
        )

        Column(verticalArrangement = Arrangement.spacedBy(BeeSpacing.M)) {
            groupedSuggestions.forEach { (category, items) ->
                ActionCategoryCard(
                    category = category,
                    items = items,
                    selectedValue = selectedValue,
                    context = context,
                    onOptionSelected = { labelKey ->
                        onSelectedChange(labelKey)
                        onCustomValueChange("")
                        onAnswerChange(labelKey)
                    },
                )
            }

            BeeTextField(
                value = customValue,
                onValueChange = {
                    onCustomValueChange(it)
                    onAnswerChange(it)
                },
                placeholder = {
                    BeeBodySmall(stringResource(R.string.help_intervention_custom_action_other))
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun ActionStepHeader(
    title: String,
    speechKey: String?,
    speechTone: BeeMascotSpeechTone,
    context: Context,
) {
    Column(verticalArrangement = Arrangement.spacedBy(BeeSpacing.M)) {
        BeeHeadlineLarge(title)

        speechKey?.let {
            BeeMascotSpeech(
                speechText = stringResource(getResIdOrFallback(context, it)),
                tone = speechTone,
            )
        }
    }
}

@Composable
private fun ActionCategoryCard(
    category: String,
    items: List<ActionSuggestion>,
    selectedValue: String,
    context: Context,
    onOptionSelected: (String) -> Unit,
) {
    BeeCardSection(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(BeeSpacing.M)) {
            BeeHeadlineSmall(text = categoryTitle(category))

            Spacer(Modifier.height(BeeSpacing.M))

            items.forEach { suggestion ->
                val isSelected = selectedValue == suggestion.labelKey
                BeeSelectableOption(
                    isSelected = isSelected,
                    text = stringResource(getResIdOrFallback(context, suggestion.labelKey)),
                    onClick = { onOptionSelected(suggestion.labelKey) },
                )
                if (suggestion != items.last()) {
                    Spacer(Modifier.height(BeeSpacing.S))
                }
            }
        }
    }
}

private fun categoryTitle(category: String): String =
    when (category) {
        "regulation" -> "Regulação imediata"
        "context" -> "Mudança de contexto"
        "values" -> "Conexão com valor"
        else -> category.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
