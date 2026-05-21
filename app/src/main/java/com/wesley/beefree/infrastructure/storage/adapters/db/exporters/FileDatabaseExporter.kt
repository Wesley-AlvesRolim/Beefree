package com.wesley.beefree.infrastructure.storage.adapters.db.exporters

import android.content.Context
import com.wesley.beefree.domain.repository.ports.DatabaseExporter
import com.wesley.beefree.domain.repository.ports.DatabaseExporterStrategy
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

class FileDatabaseExporter : DatabaseExporter<File> {
    override fun export(
        context: Context,
        strategy: DatabaseExporterStrategy,
    ): File {
        val file = File(context.cacheDir, "beefree_export.${strategy.getFileExtension()}")
        val stream = OutputStreamWriter(FileOutputStream(file), StandardCharsets.UTF_8)
        try {
            strategy.writeData(stream)
        } finally {
            stream.close()
        }
        return file
    }
}
