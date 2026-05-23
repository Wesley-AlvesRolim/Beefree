package com.wesley.beefree.ui.viewmodel

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.wesley.beefree.domain.entities.AddictionCategory
import com.wesley.beefree.domain.entities.AddictionCategoryEnum
import com.wesley.beefree.domain.entities.RelapseRecord
import com.wesley.beefree.domain.repository.ports.AddictionRepository
import com.wesley.beefree.domain.repository.ports.DatabaseExporterStrategy
import com.wesley.beefree.infrastructure.logging.TestLogger
import com.wesley.beefree.infrastructure.storage.adapters.db.exporters.FileDatabaseExporter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
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
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.OutputStreamWriter

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
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
            val repository = FakeAddictionRepository()
            val viewModel = createViewModel(repository)

            advanceUntilIdle()

            assertTrue(viewModel.isAdultMonitoringEnabled.value)
            assertFalse(viewModel.isBetsMonitoringEnabled.value)
        }

    @Test
    fun `toggle adult monitoring updates repository and ui state`() =
        runTest {
            val repository = FakeAddictionRepository()
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
            val repository = FakeAddictionRepository()
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

    private fun createViewModel(repository: AddictionRepository): SettingsViewModel =
        SettingsViewModel(
            context = ApplicationProvider.getApplicationContext<Context>(),
            addictionRepository = repository,
            exporter = FileDatabaseExporter(),
            exporterStrategy = FakeDatabaseExporterStrategy(),
            logger = TestLogger,
        )

    private class FakeDatabaseExporterStrategy : DatabaseExporterStrategy {
        override fun getMimeType(): String = "application/sql"

        override fun getFileExtension(): String = "sql"

        override fun writeData(stream: OutputStreamWriter) = Unit
    }

    private class FakeAddictionRepository : AddictionRepository {
        val categories =
            MutableStateFlow(
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
                ),
            )
        var updateCalls = 0

        override suspend fun insertAddictionCategory(category: AddictionCategory): Long = 0

        override suspend fun updateAddictionCategory(category: AddictionCategory) {
            updateCalls++
            categories.value =
                categories.value.map {
                    if (it.name == category.name) category else it
                }
        }

        override suspend fun deleteAddictionCategory(category: AddictionCategory) = Unit

        override suspend fun getAddictionCategoryById(id: Int): AddictionCategory? = null

        override fun getAllAddictionCategories(): Flow<List<AddictionCategory>> = categories

        override suspend fun insertRelapse(relapse: RelapseRecord): Long = 0

        override fun getRelapseHistory(): Flow<List<RelapseRecord>> = emptyFlow()
    }
}
