package com.wesley.beefree.ui.screens.settings

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import com.wesley.beefree.R
import com.wesley.beefree.ui.theme.BeeFreeTheme
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun monitoringSectionDisplayed() {
        composeTestRule.setContent {
            BeeFreeTheme {
                SettingsScreenContent(
                    isAdultMonitoringEnabled = true,
                    isBetsMonitoringEnabled = false,
                    onToggleAdultMonitoring = {},
                    onToggleBetsMonitoring = {},
                    onExportData = {},
                    onNavigateToAbout = {},
                    onNavigateToTerms = {},
                )
            }
        }

        val context = ApplicationProvider.getApplicationContext<Context>()
        composeTestRule
            .onNodeWithText(context.getString(R.string.settings_monitoring_section_title))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.settings_adult_content_label))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.settings_bets_label))
            .assertIsDisplayed()
    }

    @Test
    fun noSupportNetworkContactsShown() {
        composeTestRule.setContent {
            BeeFreeTheme {
                SettingsScreenContent(
                    isAdultMonitoringEnabled = true,
                    isBetsMonitoringEnabled = false,
                    onToggleAdultMonitoring = {},
                    onToggleBetsMonitoring = {},
                    onExportData = {},
                    onNavigateToAbout = {},
                    onNavigateToTerms = {},
                )
            }
        }

        composeTestRule
            .onAllNodesWithText("João Primo")
            .assertCountEquals(0)
    }

    @Test
    fun manageContactsDisabledRowDisplayed() {
        composeTestRule.setContent {
            BeeFreeTheme {
                SettingsScreenContent(
                    isAdultMonitoringEnabled = true,
                    isBetsMonitoringEnabled = false,
                    onToggleAdultMonitoring = {},
                    onToggleBetsMonitoring = {},
                    onExportData = {},
                    onNavigateToAbout = {},
                    onNavigateToTerms = {},
                )
            }
        }

        val context = ApplicationProvider.getApplicationContext<Context>()
        composeTestRule
            .onNodeWithText(context.getString(R.string.settings_manage_contacts))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.settings_manage_contacts_coming_soon))
            .assertIsDisplayed()
    }

    @Test
    fun aboutRowDisplayed() {
        composeTestRule.setContent {
            BeeFreeTheme {
                SettingsScreenContent(
                    isAdultMonitoringEnabled = true,
                    isBetsMonitoringEnabled = false,
                    onToggleAdultMonitoring = {},
                    onToggleBetsMonitoring = {},
                    onExportData = {},
                    onNavigateToAbout = {},
                    onNavigateToTerms = {},
                )
            }
        }

        val context = ApplicationProvider.getApplicationContext<Context>()
        composeTestRule
            .onNodeWithText(context.getString(R.string.settings_about))
            .assertIsDisplayed()
    }

    @Test
    fun termsOfServiceRowDisplayed() {
        composeTestRule.setContent {
            BeeFreeTheme {
                SettingsScreenContent(
                    isAdultMonitoringEnabled = true,
                    isBetsMonitoringEnabled = false,
                    onToggleAdultMonitoring = {},
                    onToggleBetsMonitoring = {},
                    onExportData = {},
                    onNavigateToAbout = {},
                    onNavigateToTerms = {},
                )
            }
        }

        val context = ApplicationProvider.getApplicationContext<Context>()
        composeTestRule
            .onNodeWithText(context.getString(R.string.settings_terms_of_service))
            .assertIsDisplayed()
    }

    @Test
    fun helpCenterNotDisplayed() {
        composeTestRule.setContent {
            BeeFreeTheme {
                SettingsScreenContent(
                    isAdultMonitoringEnabled = true,
                    isBetsMonitoringEnabled = false,
                    onToggleAdultMonitoring = {},
                    onToggleBetsMonitoring = {},
                    onExportData = {},
                    onNavigateToAbout = {},
                    onNavigateToTerms = {},
                )
            }
        }

        composeTestRule
            .onAllNodesWithText("Help Center", substring = true)
            .assertCountEquals(0)
    }

    @Test
    fun versionFooterDisplayed() {
        composeTestRule.setContent {
            BeeFreeTheme {
                SettingsScreenContent(
                    isAdultMonitoringEnabled = true,
                    isBetsMonitoringEnabled = false,
                    onToggleAdultMonitoring = {},
                    onToggleBetsMonitoring = {},
                    onExportData = {},
                    onNavigateToAbout = {},
                    onNavigateToTerms = {},
                )
            }
        }

        val context = ApplicationProvider.getApplicationContext<Context>()
        composeTestRule
            .onNodeWithText(
                "Versão",
                substring = true,
            ).assertIsDisplayed()
    }

    @Test
    fun footerTaglineDisplayed() {
        composeTestRule.setContent {
            BeeFreeTheme {
                SettingsScreenContent(
                    isAdultMonitoringEnabled = true,
                    isBetsMonitoringEnabled = false,
                    onToggleAdultMonitoring = {},
                    onToggleBetsMonitoring = {},
                    onExportData = {},
                    onNavigateToAbout = {},
                    onNavigateToTerms = {},
                )
            }
        }

        val context = ApplicationProvider.getApplicationContext<Context>()
        composeTestRule
            .onNodeWithText(context.getString(R.string.settings_footer_tagline))
            .assertIsDisplayed()
    }
}
