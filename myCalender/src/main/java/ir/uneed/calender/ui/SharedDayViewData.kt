package ir.uneed.calender.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.TypedValue
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import ir.uneed.calender.utils.*

class SharedDayViewData(context: Context, height: Float) {

    val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height.toInt())

    private fun addShadowIfNeeded(paint: Paint) {
        paint.setShadowLayer(1f, 1f, 1f, Color.BLACK)
    }

    private val typeface = Typeface.createFromAsset(context.assets, "Arabic-Regular.ttf")
    private val textSize = height * 20 / 40
    private val headerTextSize = height * 12 / 40

    private val colorTextDay = context.resolveColor(android.R.color.black)
    private val colorTextDaySelected = context.resolveColor(android.R.color.white)
    private val colorTextDayName = context.resolveColor(android.R.color.holo_blue_dark)
    private val colorTextHoliday = context.resolveColor(android.R.color.holo_red_dark)


    val backgroundSelectedPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.style = Paint.Style.FILL
        it.color = context.resolveColor(android.R.color.holo_green_light)
    }

    val todayBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.style = Paint.Style.STROKE
        it.strokeWidth = 1.5.dp
        it.color = colorTextHoliday
    }

    val dayOfMonthNumberTextHolidayPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.textAlign = Paint.Align.CENTER
        it.typeface = typeface
        it.textSize = textSize
        it.color = colorTextHoliday
        if (boldAllText || boldDay) addShadowIfNeeded(it)
    }

    val dayOfMonthNumberTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.textAlign = Paint.Align.CENTER
        it.typeface = typeface
        it.textSize = textSize
        it.color = colorTextDay
        if (boldAllText || boldDay) addShadowIfNeeded(it)
    }
    val headerTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.textAlign = Paint.Align.CENTER
        it.typeface = typeface
        it.textSize = headerTextSize
        it.color = colorTextDay
        if (boldAllText || boldDay) addShadowIfNeeded(it)
    }

    val dayOfMonthNumberTextSelectedPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.textAlign = Paint.Align.CENTER
        it.typeface = typeface
        it.textSize = textSize
        it.color = colorTextDaySelected
        if (boldAllText || boldDay) addShadowIfNeeded(it)
    }
    val headerTextSelectedPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.textAlign = Paint.Align.CENTER
        it.typeface = typeface
        it.textSize = headerTextSize
        it.color = colorTextDaySelected
        if (boldAllText || boldDay) addShadowIfNeeded(it)
    }

    val weekNumberTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.textAlign = Paint.Align.CENTER
        it.typeface = typeface
        it.textSize = headerTextSize
        it.color = colorTextDayName
        if (boldAllText || boldDay) addShadowIfNeeded(it)
    }

    val weekDayInitialsTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.textAlign = Paint.Align.CENTER
        it.typeface = typeface
        it.textSize = if (shortWeekName) height * 18 / 40 else 15.dp
        it.color = colorTextDayName

        if (boldAllText || boldWeekName) addShadowIfNeeded(it)
    }

    val widgetFooterTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.textAlign = Paint.Align.CENTER
        it.typeface = typeface
        it.textSize = height * 22 / 40
        it.color = colorTextDayName
        it.alpha = 90
        addShadowIfNeeded(it)
    }


    @ColorInt
    fun Context.resolveColor(attr: Int) = TypedValue().let {
        ContextCompat.getColor(this, attr)
    }
}
