package com.wesley.beefree.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineLarge
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@Composable
fun GreetingSection(name: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = BeeSpacing.M, vertical = BeeSpacing.XS),
    ) {
        BeeHeadlineLarge(stringResource(R.string.home_greeting, name))
        BeeHeadlineSmall(stringResource(R.string.app_name))
    }
}
