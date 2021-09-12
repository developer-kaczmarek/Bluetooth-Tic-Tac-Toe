package io.github.kaczmarek.tictactoe.utils

import android.content.Context
import android.util.TypedValue
import androidx.fragment.app.Fragment
import kotlin.math.roundToInt

/**
 * Метод превращает dp в px и возвращает целочисленное значение
 */
fun Context.dpToPxInt(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        this.resources.displayMetrics
    ).roundToInt()
}

fun Context.dpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        this.resources.displayMetrics
    )
}

inline fun <reified T : Any> Fragment.getExtraNotNull(key: String, default: T? = null): T {
    val value = arguments?.get(key)
    return requireNotNull(if (value is T) value else default) { key }
}