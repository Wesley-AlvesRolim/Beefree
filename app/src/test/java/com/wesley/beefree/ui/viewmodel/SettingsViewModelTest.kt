package com.wesley.beefree.ui.viewmodel

import com.wesley.beefree.domain.entities.AddictionCategory
import com.wesley.beefree.domain.entities.AddictionCategoryEnum
import com.wesley.beefree.domain.mocks.AddictionRepositoryMock
import com.wesley.beefree.domain.mocks.DataExportSharerMock
import com.wesley.beefree.infrastructure.logging.TestLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        TestLogger.clear()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state reflects addiction categories`() =
        runTest {
            val repository = addictionRepository()
            val viewModel = createViewModel(repository)

            advanceUntilIdle()

            assertTrue(viewModel.isAdultMonitoringEnabled.value)
            assertFalse(viewModel.isBetsMonitoringEnabled.value)
        }

    @Test
    fun `toggle adult monitoring updates repository and ui state`() =
        runTest {
            val repository = addictionRepository()
            val viewModel = createViewModel(repository)

            advanceUntilIdle()
            viewModel.toggleAdultMonitoring()
            advanceUntilIdle()

            assertFalse(viewModel.isAdultMonitoringEnabled.value)
            assertEquals(1, repository.updateCalls)
            assertFalse(
                repository.categories.value
                    .first { it.name == AddictionCategoryEnum.ADULT_CONTENT.label }
                    .isMonitoringEnabled,
            )
        }

    @Test
    fun `toggle bets monitoring updates repository and ui state`() =
        runTest {
            val repository = addictionRepository()
            val viewModel = createViewModel(repository)

            advanceUntilIdle()
            viewModel.toggleBetsMonitoring()
            advanceUntilIdle()

            assertTrue(viewModel.isBetsMonitoringEnabled.value)
            assertEquals(1, repository.updateCalls)
            assertTrue(
                repository.categories.value
                    .first { it.name == AddictionCategoryEnum.BETS.label }
                    .isMonitoringEnabled,
            )
        }

    private fun createViewModel(repository: AddictionRepositoryMock): SettingsViewModel =
        SettingsViewModel(
            addictionRepository = repository,
            dataExportSharer = DataExportSharerMock(),
            logger = TestLogger,
        )

    private fun addictionRepository(): AddictionRepositoryMock =
        AddictionRepositoryMock().apply {
            categories.value =
                listOf(
                    AddictionCategory(
                        id = 1,
                        name = AddictionCategoryEnum.ADULT_CONTENT.label,
                        isMonitoringEnabled = true,
                        createdAt = 0L,
                        updatedAt = 0L,
                    ),
                    AddictionCategory(
                        id = 2,
                        name = AddictionCategoryEnum.BETS.label,
                        isMonitoringEnabled = false,
                        createdAt = 0L,
                        updatedAt = 0L,
                    ),
                )
        }
}
