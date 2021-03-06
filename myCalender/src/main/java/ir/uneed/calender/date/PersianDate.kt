package ir.uneed.calender.date

import ir.uneed.calender.date.persian.AlgorithmicConverter
import ir.uneed.calender.date.persian.LookupTableConverter

class PersianDate : AbstractDate {
    constructor(year: Int, month: Int, dayOfMonth: Int) : super(year, month, dayOfMonth) {}
    constructor(jdn: Long) : super(jdn)
    constructor(date: AbstractDate) : super(date)

    // Converters
    override fun toJdn(): Long {
        val result = LookupTableConverter.toJdn(year, month, dayOfMonth)
        return if (result == -1L) AlgorithmicConverter.toJdn(year, month, dayOfMonth) else result
    }

    override fun fromJdn(jdn: Long): IntArray {
        val result = LookupTableConverter.fromJdn(jdn)
        return result ?: AlgorithmicConverter.fromJdn(jdn)
    }
}