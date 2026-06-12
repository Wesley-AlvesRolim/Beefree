package com.wesley.beefree.infrastructure.storage.adapters.db.exporters

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.wesley.beefree.R
import com.wesley.beefree.domain.repository.ports.DataExportSharer
import com.wesley.beefree.domain.repository.ports.DatabaseExporterStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidDataExportSharer(
    private val context: Context,
    private val exporter: FileDatabaseExporter,
    private val exporterStrategy: DatabaseExporterStrategy,
) : DataExportSharer {
    override suspend fun shareExportedData() {
        val uri =
            withContext(Dispatchers.IO) {
                val file = exporter.export(context, exporterStrategy)
                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            }
        val intent =
            Intent(Intent.ACTION_SEND).apply {
                type = exporterStrategy.getMimeType()
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        val chooser = Intent.createChooser(intent, context.getString(R.string.settings_export_data))
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }
}
