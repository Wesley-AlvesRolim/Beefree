package com.wesley.beefree.utils

import android.content.Context
import com.wesley.beefree.R

fun getResIdOrFallback(
    context: Context,
    key: String,
): Int {
    val resId = context.resources.getIdentifier(key, "string", context.packageName)
    return if (resId != 0) resId else R.string.app_name
}

fun resolveStringOrKey(
    context: Context,
    value: String,
): String {
    val resId = context.resources.getIdentifier(value, "string", context.packageName)
    return if (resId != 0) context.getString(resId) else value
}
