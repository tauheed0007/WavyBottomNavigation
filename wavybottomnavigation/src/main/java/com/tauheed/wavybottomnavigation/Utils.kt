package com.tauheed.wavybottomnavigation



import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
/**
 *
 * @author  Tauheed
 */
private var dp = 0f
private fun getDp(context: Context): Float {
    return if (dp == 0f) {
        context.resources.displayMetrics.density.apply {
            dp = this
        }
    } else
        dp
}

internal fun Float.dp(context: Context) = this * getDp(context)
internal fun Int.dp(context: Context) = this * getDp(context).toInt()

internal object DrawableHelper {

    fun changeColorDrawableVector(c: Context?, resDrawable: Int, color: Int): Drawable? {
        if (c == null)
            return null

        val d = VectorDrawableCompat.create(c.resources, resDrawable, null) ?: return null
        d.mutate()
        if (color != -2)
            d.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        return d
    }

    fun changeColorDrawableRes(c: Context?, resDrawable: Int, color: Int): Drawable? {
        if (c == null)
            return null

        val d = ContextCompat.getDrawable(c, resDrawable) ?: return null
        d.mutate()
        if (color != -2)
            d.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        return d
    }
}

internal object ColorHelper {

    fun mixTwoColors(color1: Int, color2: Int, amount: Float): Int {
        val alphaChannel = 24
        val redChannel = 16
        val greenChannel = 8

        val inverseAmount = 1.0f - amount

        val a =
            ((color1 shr alphaChannel and 0xff).toFloat() * amount + (color2 shr alphaChannel and 0xff).toFloat() * inverseAmount).toInt() and 0xff
        val r =
            ((color1 shr redChannel and 0xff).toFloat() * amount + (color2 shr redChannel and 0xff).toFloat() * inverseAmount).toInt() and 0xff
        val g =
            ((color1 shr greenChannel and 0xff).toFloat() * amount + (color2 shr greenChannel and 0xff).toFloat() * inverseAmount).toInt() and 0xff
        val b =
            ((color1 and 0xff).toFloat() * amount + (color2 and 0xff).toFloat() * inverseAmount).toInt() and 0xff

        return a shl alphaChannel or (r shl redChannel) or (g shl greenChannel) or b
    }
}

internal fun Context.getDrawableCompat(res: Int) = ContextCompat.getDrawable(this, res)

internal inline fun <T : View?> T.runAfterDelay(delay: Long, crossinline f: T.() -> Unit) {
    this?.postDelayed({
        try {
            f()
        } catch (e: Exception) {
        }
    }, delay)
}

internal fun ofColorStateList(
    @ColorInt color: Int
) = ColorStateList.valueOf(color)


fun <T> View?.updateLayoutParams(onLayoutChange: (params: T) -> Unit) {
    if (this == null)
        return
    try {
        @Suppress("UNCHECKED_CAST")
        onLayoutChange(layoutParams as T)
        layoutParams = layoutParams
    } catch (e: Exception) {
        e.printStackTrace()
    }
}