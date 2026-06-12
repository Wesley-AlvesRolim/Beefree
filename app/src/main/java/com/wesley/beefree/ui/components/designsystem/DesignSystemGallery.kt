package com.wesley.beefree.ui.components.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wesley.beefree.R
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.theme.Error
import com.wesley.beefree.ui.theme.ErrorContainer
import com.wesley.beefree.ui.theme.OnErrorContainer
import com.wesley.beefree.ui.theme.OnPrimary
import com.wesley.beefree.ui.theme.OnPrimaryContainer
import com.wesley.beefree.ui.theme.OnSecondaryContainer
import com.wesley.beefree.ui.theme.OnSurface
import com.wesley.beefree.ui.theme.OnSurfaceVariant
import com.wesley.beefree.ui.theme.OnTertiaryContainer
import com.wesley.beefree.ui.theme.Outline
import com.wesley.beefree.ui.theme.OutlineVariant
import com.wesley.beefree.ui.theme.Primary
import com.wesley.beefree.ui.theme.PrimaryContainer
import com.wesley.beefree.ui.theme.Secondary
import com.wesley.beefree.ui.theme.SecondaryContainer
import com.wesley.beefree.ui.theme.Success
import com.wesley.beefree.ui.theme.SurfaceContainer
import com.wesley.beefree.ui.theme.SurfaceContainerHigh
import com.wesley.beefree.ui.theme.SurfaceContainerHighest
import com.wesley.beefree.ui.theme.SurfaceContainerLow
import com.wesley.beefree.ui.theme.SurfaceContainerLowest
import com.wesley.beefree.ui.theme.SurfaceLight
import com.wesley.beefree.ui.theme.Tertiary
import com.wesley.beefree.ui.theme.TertiaryContainer

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DesignSystemGallery() {
    var darkTheme by remember { mutableStateOf(false) }
    BeeFreeTheme(darkTheme = darkTheme) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = BeeSpacing.L, vertical = BeeSpacing.XL),
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.XL),
        ) {
            BeeHeadlineLarge("Design System")
            BeeBodyMedium(
                "Lumina Path — Material Design 3 tokens",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
            ) {
                BeeBodyMedium(if (darkTheme) "Dark mode" else "Light mode")
                Switch(checked = darkTheme, onCheckedChange = { darkTheme = it })
            }

            GallerySection("Colors — Primary") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
                    verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
                ) {
                    ColorSwatch("primary", Primary, OnPrimary)
                    ColorSwatch("onPrimary", OnPrimary, Primary)
                    ColorSwatch("primaryContainer", PrimaryContainer, OnPrimaryContainer)
                    ColorSwatch("onPrimaryContainer", OnPrimaryContainer, PrimaryContainer)
                }
            }

            GallerySection("Colors — Surface Layers") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
                    verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
                ) {
                    ColorSwatch("surface", SurfaceLight, OnSurface)
                    ColorSwatch("containerLowest", SurfaceContainerLowest, OnSurface, border = true)
                    ColorSwatch("containerLow", SurfaceContainerLow, OnSurface)
                    ColorSwatch("container", SurfaceContainer, OnSurface)
                    ColorSwatch("containerHigh", SurfaceContainerHigh, OnSurface)
                    ColorSwatch("containerHighest", SurfaceContainerHighest, OnSurface)
                    ColorSwatch("onSurface", OnSurface, SurfaceLight)
                    ColorSwatch("onSurfaceVariant", OnSurfaceVariant, SurfaceLight)
                    ColorSwatch("outline", Outline, SurfaceLight)
                    ColorSwatch("outlineVariant", OutlineVariant, OnSurface)
                }
            }

            GallerySection("Colors — Secondary & Tertiary") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
                    verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
                ) {
                    ColorSwatch("secondary", Secondary, Color.White)
                    ColorSwatch("secondaryContainer", SecondaryContainer, OnSecondaryContainer)
                    ColorSwatch("tertiary", Tertiary, Color.White)
                    ColorSwatch("tertiaryContainer", TertiaryContainer, OnTertiaryContainer)
                }
            }

            GallerySection("Colors — Semantic") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
                    verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
                ) {
                    ColorSwatch("error", Error, Color.White)
                    ColorSwatch("errorContainer", ErrorContainer, OnErrorContainer)
                    ColorSwatch("success", Success, Color.White)
                }
            }

            GallerySection("Typography") {
                BeeHeadlineLarge("Headline Large")
                BeeHeadlineMedium("Headline Medium")
                BeeHeadlineSmall("Headline Small")
                Spacer(Modifier.height(BeeSpacing.S))
                BeeBodyLarge("Body Large — Inter Regular 16sp")
                BeeBodyMedium("Body Medium — Inter Regular 14sp")
                BeeBodySmall("Body Small — Inter Medium 12sp")
                Spacer(Modifier.height(BeeSpacing.S))
                BeeLabelLarge("Label Large — Inter Bold 16sp")
                BeeLabelMedium("Label Medium — Inter SemiBold 14sp")
                BeeLabelSmall("Label Small — Inter SemiBold 12sp")
            }

            GallerySection("Buttons") {
                Column(verticalArrangement = Arrangement.spacedBy(BeeSpacing.S)) {
                    BeeButtonPrimary(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                        BeeLabelLarge("Primary Button", color = MaterialTheme.colorScheme.onPrimary)
                    }
                    BeeButtonSecondary(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                        BeeLabelLarge("Secondary Button", color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                    BeeButtonOutlined(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                        BeeLabelLarge("Outlined Button")
                    }
                    BeeButtonGhost(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                        BeeLabelLarge("Ghost Button", color = MaterialTheme.colorScheme.primary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(BeeSpacing.M)) {
                        BeeLabelSmall("FAB:", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        BeeFAB(onClick = {}) {
                            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.btn_add))
                        }
                    }
                }
            }

            GallerySection("Cards") {
                Column(verticalArrangement = Arrangement.spacedBy(BeeSpacing.S)) {
                    BeeCardElevated(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(BeeSpacing.M)) {
                            BeeHeadlineSmall("BeeCardElevated")
                            BeeBodySmall("White background · 1dp shadow · radius 12dp", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    BeeCardTonal(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(BeeSpacing.M)) {
                            BeeHeadlineSmall("BeeCardTonal")
                            BeeBodySmall(
                                "surfaceContainerLow · no shadow · radius 12dp",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    BeeCardSection(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(BeeSpacing.M)) {
                            BeeHeadlineSmall("BeeCardSection")
                            BeeBodySmall(
                                "Large section card · radius 32dp · surfaceContainerLow",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    BeeCardInteractive(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(BeeSpacing.M)) {
                            BeeHeadlineSmall("BeeCardInteractive")
                            BeeBodySmall("White background · 1dp shadow · radius 32dp", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    BeeCardOutlined(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(BeeSpacing.M)) {
                            BeeHeadlineSmall("BeeCardOutlined")
                            BeeBodySmall(
                                "Outlined · outlineVariant border · radius 12dp",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }

            GallerySection("Chips") {
                var selected by remember { mutableStateOf(0) }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
                    verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
                ) {
                    BeeChip("Running", selected = selected == 0, onClick = { selected = 0 })
                    BeeChip("Reading", selected = selected == 1, onClick = { selected = 1 })
                    BeeChip("Meditation", selected = selected == 2, onClick = { selected = 2 })
                }
                Spacer(Modifier.height(BeeSpacing.S))
                BeeBodySmall("Tag chips (non-interactive):", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(BeeSpacing.XS))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
                    verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
                ) {
                    BeeChipTag("Morning Breathwork")
                    BeeChipTag("Evening Walk")
                    BeeChipTag(
                        "Boredom",
                        containerColor = TertiaryContainer,
                        contentColor = OnTertiaryContainer,
                    )
                    BeeChipTag(
                        "Loneliness",
                        containerColor = TertiaryContainer,
                        contentColor = OnTertiaryContainer,
                    )
                }
            }

            GallerySection("Text Fields") {
                var text1 by remember { mutableStateOf("") }
                var text2 by remember { mutableStateOf("Sample text") }
                Column(verticalArrangement = Arrangement.spacedBy(BeeSpacing.S)) {
                    BeeTextField(
                        value = text1,
                        onValueChange = { text1 = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { BeeBodyMedium("Placeholder...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                        label = { BeeBodySmall("Label") },
                    )
                    BeeTextField(
                        value = text2,
                        onValueChange = { text2 = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { BeeBodySmall("With value") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = stringResource(R.string.btn_favorite),
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        },
                    )
                    BeeTextArea(
                        value = "Multi-line text area for ACT journaling exercises. Borderless with surfaceContainerLowest fill.",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        label = { BeeBodySmall("Journal Entry") },
                    )
                }
            }

            GallerySection("Spacing") {
                Column(verticalArrangement = Arrangement.spacedBy(BeeSpacing.S)) {
                    listOf(
                        "XS" to BeeSpacing.XS,
                        "S" to BeeSpacing.S,
                        "M" to BeeSpacing.M,
                        "L" to BeeSpacing.L,
                        "XL" to BeeSpacing.XL,
                        "XXL" to BeeSpacing.XXL,
                    ).forEach { (name, value) ->
                        SpacingRow(name, value)
                    }
                }
            }

            GallerySection("Shapes") {
                Column(verticalArrangement = Arrangement.spacedBy(BeeSpacing.S)) {
                    listOf(
                        "radius-small (8dp)" to 8.dp,
                        "radius-medium (12dp)" to 12.dp,
                        "radius-large (24dp)" to 24.dp,
                        "radius-xl (32dp)" to 32.dp,
                        "radius-full (999dp)" to 999.dp,
                    ).forEach { (name, radius) ->
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(BeeSpacing.M)) {
                            Box(
                                modifier =
                                    Modifier
                                        .size(48.dp)
                                        .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(radius)),
                            )
                            BeeBodySmall(name, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GallerySection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(BeeSpacing.M)) {
        BeeHeadlineSmall(title)
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
        content()
    }
}

@Composable
private fun ColorSwatch(
    name: String,
    color: Color,
    textColor: Color,
    border: Boolean = false,
) {
    val borderStroke = if (border) 1.dp else 0.dp
    Column(
        modifier = Modifier.width(88.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BeeSpacing.XS),
    ) {
        Box(
            modifier =
                Modifier
                    .size(56.dp)
                    .background(color, RoundedCornerShape(8.dp))
                    .then(
                        if (border) {
                            Modifier.background(
                                Color.Transparent,
                                RoundedCornerShape(8.dp),
                            )
                        } else {
                            Modifier
                        },
                    ),
            contentAlignment = Alignment.Center,
        ) {
            if (border) {
                Box(
                    modifier =
                        Modifier
                            .size(54.dp)
                            .background(color, RoundedCornerShape(7.dp)),
                )
            }
        }
        BeeBodySmall(
            name,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
    }
}

@Composable
private fun SpacingRow(
    name: String,
    value: Dp,
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(BeeSpacing.M)) {
        Box(
            modifier =
                Modifier
                    .height(20.dp)
                    .width(value)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp)),
        )
        BeeBodySmall("$name — ${value.value.toInt()}dp", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Preview(showBackground = true)
@Composable
fun DesignSystemGalleryPreview() {
    DesignSystemGallery()
}
