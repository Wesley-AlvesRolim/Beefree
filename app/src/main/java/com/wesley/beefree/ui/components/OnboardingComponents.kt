package com.wesley.beefree.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.designsystem.*

val LocalOnboardingProgress = compositionLocalOf { 0 to 0 }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingLayout(
    onBack: (() -> Unit)? = null,
    showTopBar: Boolean = true,
    sectionTitle: String? = null,
    chapterNumber: Int? = null,
    bottomBar: (@Composable () -> Unit)? = null,
    contentVerticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Scaffold(
            topBar = {
                Column {
                    if (showTopBar) {
                        TopAppBar(
                            title = {
                                Column {
                                    if (chapterNumber != null) {
                                        BeeLabelSmall(
                                            text = stringResource(R.string.onboarding_chapter_label, chapterNumber),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                    if (!sectionTitle.isNullOrBlank()) {
                                        BeeHeadlineSmall(sectionTitle)
                                    }
                                }
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
                    }
                    if (chapterNumber != null) {
                        BeeSectionProgress(
                            activeSection = chapterNumber,
                            modifier = Modifier.padding(horizontal = BeeSpacing.M, vertical = BeeSpacing.S),
                        )
                    }
                }
            },
            bottomBar = {
                bottomBar?.let {
                    Surface(color = MaterialTheme.colorScheme.surface) {
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .navigationBarsPadding()
                                    .padding(BeeSpacing.M),
                        ) {
                            it()
                        }
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
        ) { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .then(if (showTopBar) Modifier else Modifier.statusBarsPadding())
                        .padding(BeeSpacing.M)
                        .verticalScroll(rememberScrollState()),
                verticalArrangement = contentVerticalArrangement,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content,
            )
        }
    }
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
    text: String = stringResource(R.string.onboarding_btn_continue),
) {
    BeeButtonPrimary(
        onClick = onNext,
        enabled = nextEnabled,
        modifier = Modifier.fillMaxWidth(),
    ) {
        BeeLabelLarge(text, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
fun OnboardingLikertOption(
    number: Int,
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BeeSelectableOption(
        text = text,
        isSelected = selected,
        onClick = onClick,
        modifier = modifier,
        indicator = { isSelected ->
            Box(
                modifier =
                    Modifier
                        .size(BeeSpacing.L)
                        .background(
                            color =
                                if (isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.primaryContainer
                                },
                            shape = CircleShape,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                BeeLabelSmall(
                    text = number.toString(),
                    color =
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        },
                )
            }
        },
    )
}
