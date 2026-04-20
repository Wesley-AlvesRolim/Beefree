package com.wesley.beefree.infrastructure.storage.adapters.db.exporters

import android.database.Cursor
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import com.wesley.beefree.infrastructure.storage.ports.DatabaseExporterStrategy
import java.io.OutputStreamWriter

class SqlDatabaseExporterStrategy(
    private val database: AppDatabase,
) : DatabaseExporterStrategy {
    override fun getMimeType(): String = "application/sql"

    override fun getFileExtension(): String = "sql"

    override fun writeData(stream: OutputStreamWriter) {
        val sqliteDb = database.openHelper.readableDatabase
        val tables = getAllTables(sqliteDb)
        for (table in tables) {
            writeTableSchema(sqliteDb, table, stream)
            writeTableData(sqliteDb, table, stream)
        }
    }

    private fun getAllTables(db: SupportSQLiteDatabase): List<String> {
        val tables = mutableListOf<String>()
        val query =
            """
            SELECT name
            FROM sqlite_master
            WHERE type = 'table'
              AND name NOT LIKE 'android_metadata'
              AND name NOT LIKE 'sqlite_sequence'
              AND name NOT LIKE 'room_master_table'
            """.trimIndent()
        db.query(query).use { cursor ->
            while (cursor.moveToNext()) {
                tables.add(cursor.getString(0))
            }
        }
        return tables
    }

    private fun writeTableSchema(
        db: SupportSQLiteDatabase,
        table: String,
        writer: OutputStreamWriter,
    ) {
        val query = "SELECT sql FROM sqlite_master WHERE type='table' AND name = ?"
        db.query(query, arrayOf(table)).use { cursor ->
            if (cursor.moveToFirst()) {
                val schema = cursor.getString(0)
                writer.write("-- Table: $table\n")
                writer.write("$schema;\n\n")
            }
        }
    }

    private fun writeTableData(
        db: SupportSQLiteDatabase,
        table: String,
        writer: OutputStreamWriter,
    ) {
        val cursor = db.query("SELECT * FROM `$table`")
        val columnNames = cursor.columnNames
        while (cursor.moveToNext()) {
            val values = mutableListOf<String>()
            for (i in 0 until cursor.columnCount) {
                values.add(
                    getColumnValue(cursor, i),
                )
            }
            val insert =
                "INSERT INTO `$table` (${
                    columnNames.joinToString(", ") { "`$it`" }
                }) VALUES (${values.joinToString(", ")});\n"
            writer.write(insert)
        }
        writer.write("\n")
        cursor.close()
    }

    private fun getColumnValue(
        cursor: Cursor,
        columnIndex: Int,
    ): String {
        var value: String? = null
        when (cursor.getType(columnIndex)) {
            Cursor.FIELD_TYPE_NULL -> value = "NULL"
            Cursor.FIELD_TYPE_INTEGER -> value = cursor.getLong(columnIndex).toString()
            Cursor.FIELD_TYPE_FLOAT -> value = cursor.getDouble(columnIndex).toString()
            Cursor.FIELD_TYPE_BLOB ->
                value =
                    "X'${
                        cursor.getBlob(columnIndex).joinToString("") { "%02x".format(it) }
                    }'"

            else -> {
                val stringRawValue = cursor.getString(columnIndex).replace("'", "''")
                value = "'$stringRawValue'"
            }
        }
        return value
    }
}
