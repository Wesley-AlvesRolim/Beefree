package com.wesley.beefree.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.wesley.beefree.R
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.ui.components.OnboardingLayout
import com.wesley.beefree.ui.components.OnboardingNavigationRow
import com.wesley.beefree.ui.components.OnboardingTitle
import com.wesley.beefree.ui.components.designsystem.*

private data class HobbyOption(
    val labelRes: Int,
    val icon: ImageVector,
)

private val hobbyOptions =
    listOf(
        HobbyOption(R.string.onboarding_hobby_exercise, Icons.Filled.FitnessCenter),
        HobbyOption(R.string.onboarding_hobby_reading, Icons.AutoMirrored.Filled.MenuBook),
        HobbyOption(R.string.onboarding_hobby_music, Icons.Filled.MusicNote),
        HobbyOption(R.string.onboarding_hobby_meditation, Icons.Filled.SelfImprovement),
        HobbyOption(R.string.onboarding_hobby_cooking, Icons.Filled.Restaurant),
        HobbyOption(R.string.onboarding_hobby_hiking, Icons.AutoMirrored.Filled.DirectionsWalk),
        HobbyOption(R.string.onboarding_hobby_photography, Icons.Filled.PhotoCamera),
        HobbyOption(R.string.onboarding_hobby_team_sports, Icons.Filled.SportsSoccer),
        HobbyOption(R.string.onboarding_hobby_art, Icons.Filled.Palette),
        HobbyOption(R.string.onboarding_hobby_videogames, Icons.Filled.SportsEsports),
        HobbyOption(R.string.onboarding_hobby_volunteering, Icons.Filled.VolunteerActivism),
        HobbyOption(R.string.onboarding_hobby_other, Icons.Filled.Add),
    )

@Composable
fun OnboardingHobbiesScreen(
    answers: OnboardingAnswers,
    onUpdate: (OnboardingAnswers.() -> OnboardingAnswers) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    OnboardingLayout(
        onBack = onBack,
        sectionTitle = stringResource(R.string.onboarding_section_seu_contexto),
        chapterNumber = 4,
        bottomBar = { OnboardingNavigationRow(onNext = onNext) },
    ) {
        BeeMascotSpeech(
            speechText = stringResource(R.string.onboarding_hobbies_mascot_speech),
            tone = BeeMascotSpeechTone.Primary,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingTitle(stringResource(R.string.onboarding_hobbies_title))
        Spacer(modifier = Modifier.height(BeeSpacing.L))
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(BeeSpacing.XXL * 10),
            userScrollEnabled = false,
        ) {
            items(hobbyOptions) { option ->
                val label = stringResource(option.labelRes)
                val isSelected = label in answers.hobbies
                HobbyTile(
                    label = label,
                    icon = option.icon,
                    isSelected = isSelected,
                    onClick = {
                        onUpdate {
                            val updated = if (isSelected) hobbies - label else hobbies + label
                            copy(hobbies = updated)
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun HobbyTile(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    BeeMultiSelectOption(
        text = label,
        isSelected = isSelected,
        onClick = onClick,
        modifier =
            Modifier
                .fillMaxWidth()
                .height(BeeSpacing.XXL * 2),
        direction = BeeSelectableOptionDirection.Column,
        textVariant = BeeSelectableOptionTextVariant.Small,
        textAlign = TextAlign.Center,
        columnArrangement = Arrangement.Center,
        columnAlignment = Alignment.CenterHorizontally,
        indicator = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(BeeSpacing.L),
            )
            Spacer(modifier = Modifier.height(BeeSpacing.XXL))
        },
    )
}
