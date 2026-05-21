package com.wesley.beefree.infrastructure.storage.adapters.db.exporters

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.AddictionCategoryEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SqlDatabaseExporterTest {
    private lateinit var database: AppDatabase
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        database =
            Room
                .inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `should export database to sql file with schema and data`() {
        runBlocking {
            val category =
                AddictionCategoryEntity(
                    id = 1,
                    name = "ADULT_CONTENT",
                    isMonitoringEnabled = true,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                )
            database.addictionCategoryDao().insert(category)

            val exporter = FileDatabaseExporter()
            val strategy = SqlDatabaseExporterStrategy(database)

            val exportedFile = exporter.export(context, strategy)

            assertTrue(exportedFile.exists())
            val content = exportedFile.readText()

            assertTrue(content.contains("CREATE TABLE `AddictionCategory`"))
            assertTrue(content.contains("INSERT INTO `AddictionCategory`"))
            assertTrue(content.contains("'ADULT_CONTENT'"))
        }
    }
}
