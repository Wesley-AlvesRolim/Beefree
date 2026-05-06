package com.wesley.beefree.ui.screens.helpinterventionscreens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.designsystem.BeeCardElevated
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeech
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeechTone
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.components.designsystem.BeeTextArea

@Composable
fun TextInputStepContent(
    titleKey: String,
    value: String,
    onValueChange: (String) -> Unit,
    onAnswerChange: (Any) -> Unit,
    speechKey: String? = null,
    speechTone: BeeMascotSpeechTone = BeeMascotSpeechTone.Secondary,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
    ) {
        speechKey?.let {
            BeeMascotSpeech(
                speechText = stringResource(getResIdOrFallback(context, it)),
                tone = speechTone,
            )
        }

        BeeCardElevated(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(BeeSpacing.M)) {
                BeeLabelMedium(stringResource(getResIdOrFallback(context, titleKey)))
                Spacer(Modifier.height(BeeSpacing.M))

                BeeTextArea(
                    value = value,
                    onValueChange = {
                        onValueChange(it)
                        onAnswerChange(it)
                    },
                    minLines = 4,
                )
            }
        }
    }
}

private fun getResIdOrFallback(
    context: Context,
    key: String,
): Int {
    val resId = context.resources.getIdentifier(key, "string", context.packageName)
    return if (resId != 0) resId else com.wesley.beefree.R.string.app_name
}
