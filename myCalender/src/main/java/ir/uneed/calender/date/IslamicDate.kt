package ir.uneed.calender.date

import ir.uneed.calender.date.islamic.FallbackIslamicConverter
import ir.uneed.calender.date.islamic.IranianIslamicDateConverter
import ir.uneed.calender.date.islamic.UmmAlQuraConverter

class IslamicDate : AbstractDate {
    constructor(year: Int, month: Int, dayOfMonth: Int) : super(year, month, dayOfMonth) {}
    constructor(jdn: Long) : super(jdn)
    constructor(date: AbstractDate) : super(date)

    override fun toJdn(): Long {
        val year = year
        val month = month
        val day = dayOfMonth
        val tableResult = if (useUmmAlQura) UmmAlQuraConverter.toJdn(year, month, day)
            .toLong() else IranianIslamicDateConverter.toJdn(year, month, day)
        return if (tableResult != -1L) tableResult - islamicOffset else FallbackIslamicConverter.toJdn(
            year,
            month,
            day
        ) - islamicOffset
    }

    override fun fromJdn(jdn: Long): IntArray {
        var j = jdn
        j += islamicOffset.toLong()
        var result =
            if (useUmmAlQura) UmmAlQuraConverter.fromJdn(j) else IranianIslamicDateConverter.fromJdn(
                j
            )
        if (result == null) result = FallbackIslamicConverter.fromJdn(j)
        return result
    }

    companion object {
        // Converters
        var useUmmAlQura = false
        var islamicOffset = 0
    }
}