package com.wesley.beefree.domain.repository.ports

import android.content.Context
import java.io.File
import java.io.OutputStreamWriter

interface DatabaseExporter<T : File> {
    fun export(
        context: Context,
        strategy: DatabaseExporterStrategy,
    ): T
}

interface DatabaseExporterStrategy {
    fun getMimeType(): String

    fun getFileExtension(): String

    fun writeData(stream: OutputStreamWriter)
}

interface DataExportSharer {
    suspend fun shareExportedData()
}
