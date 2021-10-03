package ir.uneed.calender

import ir.uneed.calender.utils.toCivilDate
import ir.uneed.calender.utils.toJavaCalendar
import java.util.*

@JvmInline
value class Jdn(private val value: Long) {
    constructor(value: ir.uneed.calender.date.AbstractDate) : this(value.toJdn())
    constructor(calendar: CalendarType, year: Int, month: Int, day: Int) :
            this(calendar.createDate(year, month, day))

    // 0 means Saturday in it, see #`test day of week from jdn`() in the testsuite
    val dayOfWeek: Int get() = ((value + 2L) % 7L).toInt()

    fun toCalendar(calendar: CalendarType): ir.uneed.calender.date.AbstractDate = when (calendar) {
        CalendarType.ISLAMIC -> toIslamicCalendar()
        CalendarType.GREGORIAN -> toGregorianCalendar()
        CalendarType.SHAMSI -> toPersianCalendar()
    }

    fun toIslamicCalendar() = ir.uneed.calender.date.IslamicDate(value)
    fun toGregorianCalendar() = ir.uneed.calender.date.CivilDate(value)
    fun toPersianCalendar() = ir.uneed.calender.date.PersianDate(value)

    fun createMonthDaysList(monthLength: Int) = (value until value + monthLength).map(::Jdn)

    operator fun compareTo(other: Jdn) = value.compareTo(other.value)
    operator fun plus(other: Int): Jdn = Jdn(value + other)
    operator fun minus(other: Int): Jdn = Jdn(value - other)

    // Difference of two Jdn values in days
    operator fun minus(other: Jdn): Int = (value - other.value).toInt()

    companion object {
        val today: Jdn get() = Jdn(Date().toJavaCalendar().toCivilDate())
    }
}