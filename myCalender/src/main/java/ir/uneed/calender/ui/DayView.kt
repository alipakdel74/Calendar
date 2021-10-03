package ir.uneed.calender.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import ir.uneed.calender.Jdn
import ir.uneed.calender.utils.formatNumber
import ir.uneed.calender.utils.otherCalendars
import ir.uneed.calender.utils.showOtherCalendar
import kotlin.math.min

class DayView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var text = ""
    private var today = false
    private var dayIsSelected = false
    private var holiday = false
    var jdn: Jdn? = null
    var dayOfMonth = -1
    private var isWeekNumber = false
    private var header = ""

    var sharedDayViewData: SharedDayViewData? = null

    private val textBounds = Rect()
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val shared = sharedDayViewData ?: return

        // Draw circle around day
        val radius = min(width, height) / 2f
        if (dayIsSelected)
            canvas.drawCircle(width / 2f, height / 2f, radius - 5, shared.backgroundSelectedPaint)
        if (today) canvas.drawCircle(width / 2f, height / 2f, radius - 5, shared.todayBorderPaint)

        val textPaint = when {
            jdn != null -> when {
                holiday -> shared.dayOfMonthNumberTextHolidayPaint
                dayIsSelected && !today -> shared.dayOfMonthNumberTextSelectedPaint
                today -> shared.todayTextPaint
                else /*!dayIsSelected*/ -> shared.dayOfMonthNumberTextPaint
            }
            isWeekNumber -> shared.weekNumberTextPaint
            else -> shared.weekDayInitialsTextPaint
        }

        // Measure a sample text to find height for vertical center aligning of the text to draw
        val sample = if (jdn != null) text else "Yy"
        textPaint.getTextBounds(sample, 0, sample.length, textBounds)
        val yPos = (height + textBounds.height()) / 2f
        // Draw day number/label
        canvas.drawText(text, width / 2f, yPos, textPaint)

        // Draw day header which is used for shit work
        if (header.isNotEmpty()) {
            canvas.drawText(
                header, width / 2f, yPos * 0.87f - textBounds.height(),
                if (dayIsSelected) shared.headerTextSelectedPaint else shared.headerTextPaint
            )
        }

        // Experiment around what happens if we show other calendars day of month
        if (showOtherCalendar)
            jdn?.also {
                otherCalendars.forEachIndexed { i, calendar ->
                    val offset =
                        (if (layoutDirection == LAYOUT_DIRECTION_RTL) -1 else 1) * if (i == 1) -1 else 1
                    canvas.drawText(
                        // better to not calculate this during onDraw
                        formatNumber(it.toCalendar(calendar).dayOfMonth),
                        (width - radius * offset) / 2f,
                        (height + textBounds.height() + radius) / 2f,
                        shared.headerTextPaint
                    )
                }
            }
    }

    private fun setAll(
        text: String, isToday: Boolean = false, isSelected: Boolean = false,
        isHoliday: Boolean = false, jdn: Jdn? = null, dayOfMonth: Int = -1,
        header: String = "", isWeekNumber: Boolean = false
    ) {
        this.text = text
        this.today = isToday
        this.dayIsSelected = isSelected
        this.holiday = isHoliday
        this.jdn = jdn
        this.dayOfMonth = dayOfMonth
        this.isWeekNumber = isWeekNumber
        this.header = header
        postInvalidate()
    }

    fun setDayOfMonthItem(
        isToday: Boolean, isSelected: Boolean,
        isHoliday: Boolean, jdn: Jdn, dayOfMonth: Int,
        header: String
    ) = setAll(
        text = formatNumber(dayOfMonth), isToday = isToday, isSelected = isSelected,
        isHoliday = isHoliday, jdn = jdn, dayOfMonth = dayOfMonth, header = header
    )

    fun setInitialOfWeekDay(text: String) = setAll(text)
    fun setWeekNumber(text: String) = setAll(text, isWeekNumber = true)
}
