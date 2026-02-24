package com.wesley.beefreepoc.utils

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityManager

object AccessibilityUtils {
    private const val TAG = "AccessibilityUtils"

    fun isAccessibilityServiceEnabled(
        context: Context,
        serviceClass: Class<*>,
    ): Boolean {
        val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE)
        if (accessibilityManager !is AccessibilityManager) {
            return false
        }
        val enabledServices =
            accessibilityManager.getEnabledAccessibilityServiceList(
                AccessibilityServiceInfo.FEEDBACK_ALL_MASK,
            )

        for (service in enabledServices) {
            val serviceName = service.resolveInfo.serviceInfo.name
            Log.d(TAG, "Enabled service: $serviceName")
            if (serviceName == serviceClass.name) {
                Log.d(TAG, "${serviceClass.simpleName} is enabled")
                return true
            }
        }
        Log.d(TAG, "${serviceClass.simpleName} is not enabled")
        return false
    }

    fun isAccessibilityServiceEnabledAlternative(
        context: Context,
        serviceClass: Class<*>,
    ): Boolean {
        val expectedComponentName = ComponentName(context, serviceClass).flattenToString()
        val accessibilityEnabled =
            Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED,
                0,
            )
        if (accessibilityEnabled == 1) {
            val settingValue =
                Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
                )
            if (settingValue != null) {
                val splitter = TextUtils.SimpleStringSplitter(':')
                splitter.setString(settingValue)
                while (splitter.hasNext()) {
                    val accessibilityService = splitter.next()
                    if (accessibilityService.equals(expectedComponentName, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun openAccessibilitySettings(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
