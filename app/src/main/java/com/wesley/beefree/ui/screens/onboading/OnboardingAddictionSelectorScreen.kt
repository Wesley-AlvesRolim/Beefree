package com.wesley.beefree.ui.screens.onboading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.OnboardingLayout
import com.wesley.beefree.ui.components.OnboardingMascot
import com.wesley.beefree.ui.components.OnboardingTitle
import com.wesley.beefree.ui.viewmodel.ports.AddictionCategory

@Composable
fun OnboardingAddictionSelectorScreen(
    selectedAddictions: Set<AddictionCategory>,
    onToggleAddiction: (AddictionCategory) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    OnboardingLayout {
        OnboardingMascot()
        Spacer(modifier = Modifier.padding(8.dp))
        OnboardingTitle(stringResource(R.string.onboarding_addiction_selector_title))
        Spacer(modifier = Modifier.padding(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = stringResource(R.string.onboarding_addiction_selector_subtitle),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Left,
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = stringResource(R.string.onboarding_addiction_selector_hint),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Left,
            )
        }
        Spacer(modifier = Modifier.padding(24.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            AddictionSelectButton(
                text = stringResource(R.string.onboarding_addiction_adult_content),
                isSelected = selectedAddictions.contains(AddictionCategory.ADULT_CONTENT),
                onClick = { onToggleAddiction(AddictionCategory.ADULT_CONTENT) },
            )
            Spacer(modifier = Modifier.padding(4.dp))
            AddictionSelectButton(
                text = stringResource(R.string.onboarding_addiction_bets),
                isSelected = selectedAddictions.contains(AddictionCategory.BETS),
                onClick = { onToggleAddiction(AddictionCategory.BETS) },
            )
            Spacer(modifier = Modifier.padding(4.dp))
            AddictionSelectButton(
                text = stringResource(R.string.onboarding_addiction_others),
                isSelected = selectedAddictions.contains(AddictionCategory.OTHERS),
                onClick = { onToggleAddiction(AddictionCategory.OTHERS) },
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Button(
            onClick = onNext,
            enabled = selectedAddictions.isNotEmpty(),
        ) {
            Text(stringResource(R.string.onboarding_btn_continue))
        }
    }
}

@Composable
private fun AddictionSelectButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors =
            if (isSelected) {
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
            } else {
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
    ) {
        Text(text)
    }
}
