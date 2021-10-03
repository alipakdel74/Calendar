package ir.uneed.calender.utils

import android.content.res.Resources
import ir.uneed.calender.CalendarType
import ir.uneed.calender.Jdn
import ir.uneed.calender.date.AbstractDate
import ir.uneed.calender.date.CivilDate
import ir.uneed.calender.date.IslamicDate
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

val Number.dp: Float get() = this.toFloat() * Resources.getSystem().displayMetrics.density
val Number.sp: Float get() = this.toFloat() * Resources.getSystem().displayMetrics.scaledDensity


val monthNameEmptyList = List(12) { "" }
var persianMonths = monthNameEmptyList
var islamicMonths = monthNameEmptyList
var gregorianMonths = monthNameEmptyList
val weekDaysEmptyList = List(7) { "" }
var weekDays = weekDaysEmptyList
var weekEnds = BooleanArray(7)
var weekDaysInitials = weekDaysEmptyList


// See the naming here, https://developer.mozilla.org/en-US/docs/Web/CSS/list-style-type
val PERSIAN_DIGITS = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')

fun formatNumber(number: Int): String = formatNumber(number.toString())

fun formatNumber(number: String): String {
    if (language == Language.EN_US) return number
    return number.map { PERSIAN_DIGITS.getOrNull(Character.getNumericValue(it)) ?: it }
        .joinToString("")
}

fun <T> listOf12Items(
    x1: T, x2: T, x3: T, x4: T, x5: T, x6: T, x7: T, x8: T, x9: T, x10: T, x11: T, x12: T
) = listOf(x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12)

fun <T> listOf7Items(
    x1: T, x2: T, x3: T, x4: T, x5: T, x6: T, x7: T
) = listOf(x1, x2, x3, x4, x5, x6, x7)


fun Calendar.toCivilDate() =
    CivilDate(
        this[Calendar.YEAR],
        this[Calendar.MONTH] + 1,
        this[Calendar.DAY_OF_MONTH]
    )

fun Date.toJavaCalendar(forceLocalTime: Boolean = false): Calendar = Calendar.getInstance().also {
    if (!forceLocalTime)
        it.timeZone = TimeZone.getTimeZone("Asia/Tehran")
    it.time = this
}

fun Date.fromToDate(forceLocalTime: Boolean = false): Jdn =
    Jdn(toJavaCalendar(forceLocalTime).toCivilDate())

fun Jdn.toDate(): Date {
    val calendar = Calendar.getInstance()
    val jdn = this.toGregorianCalendar()
    val result = jdn.fromJdn(jdn.toJdn())
    calendar.set(result[0], result[1] - 1, result[2])
    return calendar.time
}

fun CalendarType.getMonthLength(year: Int, month: Int): Int {
    val nextMonthYear = if (month == 12) year + 1 else year
    val nextMonthMonth = if (month == 12) 1 else month + 1
    val nextMonthStartingDay = Jdn(this, nextMonthYear, nextMonthMonth, 1)
    val thisMonthStartingDay = Jdn(this, year, month, 1)
    return nextMonthStartingDay - thisMonthStartingDay
}

val mainCalendar = if (language == Language.EN_US) CalendarType.GREGORIAN else CalendarType.SHAMSI

fun applyWeekStartOffsetToWeekDay(dayOfWeek: Int): Int = (dayOfWeek + 7 - 0) % 7

fun Jdn.getWeekOfYear(startOfYear: Jdn): Int {
    val dayOfYear = this - startOfYear
    return ceil(1 + (dayOfYear - applyWeekStartOffsetToWeekDay(this.dayOfWeek)) / 7.0).toInt()
}

val Jdn.dayOfWeekName: String get() = weekDays[this.dayOfWeek]
val AbstractDate.monthName get() = this.calendarType.monthsNames.getOrNull(month - 1) ?: ""

val AbstractDate.calendarType: CalendarType
    get() = when (this) {
        is IslamicDate -> CalendarType.ISLAMIC
        is CivilDate -> CalendarType.GREGORIAN
        else -> CalendarType.SHAMSI
    }

val CalendarType.monthsNames: List<String>
    get() = when (this) {
        CalendarType.SHAMSI -> persianMonths
        CalendarType.ISLAMIC -> islamicMonths
        CalendarType.GREGORIAN -> gregorianMonths
    }

fun getInitialOfWeekDay(position: Int) = weekDaysInitials[position % 7]
fun revertWeekStartOffsetFromWeekDay(dayOfWeek: Int): Int = (dayOfWeek + 0) % 7
fun getWeekDayName(position: Int) = weekDays[position % 7]
fun isWeekEnd(dayOfWeek: Int) = weekEnds[dayOfWeek]


fun Date.toIsoDate(
    timeZoneId: String = "UTC",
    pattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
): String {
    val format = SimpleDateFormat(pattern, Locale.ENGLISH)
    format.timeZone = TimeZone.getTimeZone(timeZoneId)
    return format.format(this)
}

fun String.fromIsoDate(
    timeZoneId: String = "UTC",
    pattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
): Date {
    val format = SimpleDateFormat(pattern, Locale.ENGLISH)
    format.timeZone = TimeZone.getTimeZone(timeZoneId)
    return format.parse(this) ?: Date()
}

/**
 * - IRAN = +3:30
 * - IRAN-Daylight saving = +4:30
 * */
fun Date.toLocalTime(pattern: String = "HH:mm"): String {
    val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
    val timeZone = TimeZone.getTimeZone("Iran")
    val dayLightSaving = if (timeZone.inDaylightTime(this)) timeZone.dstSavings else 0
    val offset = timeZone.rawOffset + dayLightSaving
    return simpleDateFormat.format(time + offset)
}



