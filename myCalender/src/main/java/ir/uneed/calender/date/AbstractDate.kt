package ir.uneed.calender.date

import ir.uneed.calender.utils.monthName


abstract class AbstractDate {
    // Concrete things
    val year: Int
    val month: Int
    val dayOfMonth: Int

    /* What JDN (Julian Day Number) means?
     *
     * From https://en.wikipedia.org/wiki/Julian_day:
     * Julian day is the continuous count of days since the beginning of the
     * Julian Period and is used primarily by astronomers, and in software for
     * easily calculating elapsed days between two events (e.g. food production
     * date and sell by date).
     */
    constructor(year: Int, month: Int, dayOfMonth: Int) {
        this.year = year
        this.month = month
        this.dayOfMonth = dayOfMonth
    }

    constructor(jdn: Long) {
        val result = fromJdn(jdn)
        year = result[0]
        month = result[1]
        dayOfMonth = result[2]
    }

    constructor(date: AbstractDate) : this(date.toJdn())

    // Things needed to be implemented by subclasses
    abstract fun toJdn(): Long
    abstract fun fromJdn(jdn: Long): IntArray
    override fun equals(other: Any?): Boolean {
        if (javaClass != other!!.javaClass) return false
        if (other is AbstractDate) {
            return year == other.year && month == other.month && dayOfMonth == other.dayOfMonth
        }
        return false
    }

    override fun hashCode(): Int {
        var result = year
        result = 31 * result + month
        result = 31 * result + dayOfMonth
        return result
    }

    fun getDateString(showMonthName: Boolean = false, space: String = "/"): String =
        if (showMonthName)
            "".plus(dayOfMonth).plus(space).plus(monthName).plus(space).plus(year)
        else
            "".plus(dayOfMonth).plus(space).plus(month).plus(space).plus(year)

}