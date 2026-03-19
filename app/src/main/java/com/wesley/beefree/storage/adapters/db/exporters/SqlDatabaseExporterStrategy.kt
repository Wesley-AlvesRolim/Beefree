package com.wesley.beefree.storage.adapters.db.exporters

import android.database.Cursor
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wesley.beefree.storage.adapters.db.AppDatabase
import com.wesley.beefree.storage.ports.DatabaseExporterStrategy
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
        val cursor = db.query(query)
        while (cursor.moveToNext()) {
            tables.add(cursor.getString(0))
        }
        cursor.close()
        return tables
    }

    private fun writeTableSchema(
        db: SupportSQLiteDatabase,
        table: String,
        writer: OutputStreamWriter,
    ) {
        val cursor = db.query("SELECT sql FROM sqlite_master WHERE type='table' AND name='$table'")
        if (cursor.moveToFirst()) {
            val schema = cursor.getString(0)
            writer.write("-- Table: $table\n")
            writer.write("$schema;\n\n")
        }
        cursor.close()
    }

    private fun writeTableData(
        db: SupportSQLiteDatabase,
        table: String,
        writer: OutputStreamWriter,
    ) {
        val cursor = db.query("SELECT * FROM $table")
        val columnNames = cursor.columnNames
        while (cursor.moveToNext()) {
            val values = mutableListOf<String>()
            for (i in 0 until cursor.columnCount) {
                if (cursor.isNull(i)) {
                    values.add("NULL")
                } else {
                    when (cursor.getType(i)) {
                        Cursor.FIELD_TYPE_INTEGER -> values.add(cursor.getLong(i).toString())
                        Cursor.FIELD_TYPE_FLOAT -> values.add(cursor.getDouble(i).toString())
                        Cursor.FIELD_TYPE_BLOB ->
                            values.add(
                                "X'${
                                    cursor.getBlob(i).joinToString("") { "%02x".format(it) }
                                }'",
                            )

                        else -> {
                            val value = cursor.getString(i).replace("'", "''")
                            values.add("'$value'")
                        }
                    }
                }
            }
            val insert = "INSERT INTO $table (${columnNames.joinToString(", ")}) VALUES (${
                values.joinToString(", ")
            });\n"
            writer.write(insert)
        }
        writer.write("\n")
        cursor.close()
    }
}
