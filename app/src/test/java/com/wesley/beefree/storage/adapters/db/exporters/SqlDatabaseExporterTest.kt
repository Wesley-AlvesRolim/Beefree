package com.wesley.beefree.infrastructure.storage.adapters.db.exporters

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.AddictionKeywordEntity
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.AddictionTypeEntity
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
            val addictionType =
                AddictionTypeEntity(
                    id = 1,
                    name = "Pornography",
                    isMonitoringEnabled = true,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                )
            database.addictionTypeDao().insert(addictionType)

            val keyword1 = AddictionKeywordEntity(id = 1, addictionTypeId = 1, keyword = "porn")
            val keyword2 = AddictionKeywordEntity(id = 2, addictionTypeId = 1, keyword = "onlyfans")
            database.addictionKeywordDao().insert(keyword1)
            database.addictionKeywordDao().insert(keyword2)

            val exporter = FileDatabaseExporter()
            val strategy = SqlDatabaseExporterStrategy(database)

            val exportedFile = exporter.export(context, strategy)

            assertTrue(exportedFile.exists())
            val content = exportedFile.readText()

            assertTrue(content.contains("CREATE TABLE `AddictionTypes`"))
            assertTrue(content.contains("CREATE TABLE `AddictionKeywords`"))

            assertTrue(content.contains("INSERT INTO `AddictionTypes`"))
            assertTrue(content.contains("'Pornography'"))

            assertTrue(content.contains("INSERT INTO `AddictionKeywords`"))
            assertTrue(content.contains("'porn'"))
            assertTrue(content.contains("'onlyfans'"))
        }
    }
}
