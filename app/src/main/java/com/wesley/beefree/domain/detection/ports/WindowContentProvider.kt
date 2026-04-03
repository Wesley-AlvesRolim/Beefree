package com.wesley.beefree.domain.detection.ports

import android.view.accessibility.AccessibilityNodeInfo

fun interface WindowContentProvider {
    fun getRootNode(): AccessibilityNodeInfo?
}
