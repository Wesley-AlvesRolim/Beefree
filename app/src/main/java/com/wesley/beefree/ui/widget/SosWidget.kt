package com.wesley.beefree.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.wesley.beefree.MainActivity
import com.wesley.beefree.R

class SosWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        appWidgetIds.forEach { appWidgetId ->
            val intent =
                Intent(context, MainActivity::class.java).apply {
                    putExtra(EXTRA_OPEN_SOS, true)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
            val pendingIntent =
                PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )
            val views =
                RemoteViews(context.packageName, R.layout.widget_sos).apply {
                    setOnClickPendingIntent(R.id.sos_button, pendingIntent)
                }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    companion object {
        const val EXTRA_OPEN_SOS = "open_sos"
    }
}
