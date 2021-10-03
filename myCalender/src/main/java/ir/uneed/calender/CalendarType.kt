package ir.uneed.calender

import androidx.annotation.StringRes

enum class CalendarType(@StringRes val title: Int, @StringRes val shortTitle: Int) {
    // So vital, don't ever change names of these
    SHAMSI(R.string.shamsi_calendar, R.string.shamsi_calendar_short),
    ISLAMIC(R.string.islamic_calendar, R.string.islamic_calendar_short),
    GREGORIAN(R.string.gregorian_calendar, R.string.gregorian_calendar_short);

    fun createDate(year: Int, month: Int, day: Int) = when (this) {
        ISLAMIC -> ir.uneed.calender.date.IslamicDate(year, month, day)
        GREGORIAN -> ir.uneed.calender.date.CivilDate(year, month, day)
        SHAMSI -> ir.uneed.calender.date.PersianDate(year, month, day)
    }
}