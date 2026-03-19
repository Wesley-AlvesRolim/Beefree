package com.wesley.beefree.storage.ports

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
