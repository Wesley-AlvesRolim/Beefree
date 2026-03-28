package com.wesley.beefree.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.designsystem.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingLayout(
    onBack: (() -> Unit)? = null,
    showTopBar: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        if (showTopBar) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            BeeHeadlineSmall(stringResource(R.string.onboarding_top_bar_title))
                        },
                        navigationIcon = {
                            if (onBack != null) {
                                IconButton(onClick = onBack) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = stringResource(R.string.onboarding_btn_back),
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            }
                        },
                        colors =
                            TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                            ),
                    )
                },
                containerColor = MaterialTheme.colorScheme.surface,
            ) { innerPadding ->
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(BeeSpacing.M)
                            .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = content,
                )
            }
        } else {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(BeeSpacing.M)
                        .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content,
            )
        }
    }
}

@Composable
fun OnboardingMascot() {
    Image(
        painter = painterResource(R.drawable.cute_bee_1),
        contentDescription = stringResource(R.string.onboarding_mascot_description),
        modifier =
            Modifier
                .width(128.dp)
                .height(128.dp),
    )
}

@Composable
fun OnboardingTitle(text: String) {
    BeeHeadlineMedium(
        text = text,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
fun OnboardingNavigationRow(
    onNext: () -> Unit,
    nextEnabled: Boolean = true,
) {
    BeeButtonPrimary(
        onClick = onNext,
        enabled = nextEnabled,
        modifier = Modifier.fillMaxWidth(),
    ) {
        BeeLabelLarge(stringResource(R.string.onboarding_btn_continue), color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
fun OnboardingSelectableOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = SelectionOptionCard(
    text = text,
    isSelected = isSelected,
    onClick = onClick,
    selectedIcon = Icons.Filled.RadioButtonChecked,
    unselectedIcon = Icons.Outlined.RadioButtonUnchecked,
    modifier = modifier,
)

@Composable
fun OnboardingMultiSelectOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = SelectionOptionCard(
    text = text,
    isSelected = isSelected,
    onClick = onClick,
    selectedIcon = Icons.Default.CheckBox,
    unselectedIcon = Icons.Outlined.CheckBoxOutlineBlank,
    modifier = modifier,
)

@Composable
private fun SelectionOptionCard(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
        border =
            if (isSelected) {
                BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
            } else {
                null
            },
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = BeeSpacing.M, vertical = BeeSpacing.M),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            BeeBodyLarge(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Icon(
                imageVector = if (isSelected) selectedIcon else unselectedIcon,
                contentDescription = null,
                tint =
                    if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outlineVariant
                    },
                modifier = Modifier.size(24.dp),
            )
        }
    }
}
