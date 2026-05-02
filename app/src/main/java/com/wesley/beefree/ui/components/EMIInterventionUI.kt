package com.wesley.beefree.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.wesley.beefree.R
import com.wesley.beefree.domain.intervention.EmiTool
import com.wesley.beefree.ui.components.designsystem.BeeBodyLarge
import com.wesley.beefree.ui.components.designsystem.BeeChipTag
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineLarge
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.theme.BeeFreeTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EMIInterventionUI(
    reason: String = "",
    coreValues: List<String> = emptyList(),
    emiTool: EmiTool = EmiTool.THOUGHT_RECORD,
    onCloseRequest: () -> Unit = {},
) {
    val context = LocalContext.current
    BeeFreeTheme(darkTheme = true) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                    .pointerInput(Unit) {}
                    .systemBarsPadding()
                    .padding(BeeSpacing.M),
        ) {
            IconButton(
                onClick = {
                    onCloseRequest()
                },
                modifier = Modifier.align(Alignment.TopEnd),
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = stringResource(R.string.emi_close_description),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BeeHeadlineLarge(
                    text = stringResource(R.string.dont_do_this),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(BeeSpacing.S))

                BeeBodyLarge(
                    text = stringResource(R.string.emi_breathe),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )

                if (reason.isNotEmpty()) {
                    BeeBodyLarge(
                        text = reason,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = BeeSpacing.S),
                    )
                }

                if (coreValues.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(BeeSpacing.L))

                    BeeHeadlineSmall(
                        text = stringResource(R.string.emi_remember_values),
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(BeeSpacing.S))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S, Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
                    ) {
                        coreValues.forEach { value ->
                            BeeChipTag(
                                label = value,
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EMIInterventionUIPreview() {
    BeeFreeTheme {
        EMIInterventionUI(reason = "Triggered by keyword 'bets'") {}
    }
}

@Preview(showBackground = true)
@Composable
fun EMIInterventionUIWithValuesPreview() {
    BeeFreeTheme {
        EMIInterventionUI(
            reason = "",
            coreValues = listOf("Família", "Saúde", "Fé"),
            emiTool = EmiTool.URGE_SURFING,
        ) {}
    }
}
