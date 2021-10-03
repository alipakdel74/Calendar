package ir.uneed.calender.utils

import ir.uneed.calender.CalendarType

var language = Language.FA
var shortWeekName = false
var isShowWeekOfYearEnabled = false
var otherCalendars = listOf(CalendarType.GREGORIAN, CalendarType.ISLAMIC)
var showOtherCalendar = false
var boldAllText = true
var boldWeekName = false
var boldDay = false
var fontDate: String = "Arabic-Regular.ttf"
var footerTextColor:Int = android.R.color.holo_blue_dark
var weekNumberTextColor:Int = android.R.color.holo_purple
var weekDaysTextColor:Int = android.R.color.holo_purple
var otherDateTextColor:Int = android.R.color.darker_gray
var dayTextColor:Int = android.R.color.black
var todayBackgroundColor:Int = android.R.color.holo_orange_dark
var todayTextColor:Int = android.R.color.white
var colorTextHoliday:Int = android.R.color.holo_red_dark
var colorSelectedBorder:Int = android.R.color.holo_red_dark

fun applyOption(
    language: Language = Language.FA,
    shortWeekName: Boolean = false,
    isShowWeekOfYearEnabled: Boolean = false,
    otherCalendars: List<CalendarType> = listOf(CalendarType.GREGORIAN, CalendarType.ISLAMIC),
    showOtherCalendar: Boolean = false,
    boldAllText: Boolean = true,
    boldWeekName: Boolean = false,
    boldDay: Boolean = false,
    fontDate: String = "Arabic-Regular.ttf",
    footerTextColor:Int = android.R.color.holo_blue_dark,
    weekNumberTextColor:Int = android.R.color.holo_purple,
    weekDaysTextColor:Int = android.R.color.holo_purple,
    otherDateTextColor:Int = android.R.color.darker_gray,
    dayTextColor:Int = android.R.color.black,
    todayBackgroundColor:Int = android.R.color.holo_green_dark,
    todayTextColor:Int = android.R.color.holo_orange_dark,
    colorTextHoliday:Int = android.R.color.holo_red_dark,
    colorSelectedBorder:Int = android.R.color.holo_red_dark,
) {
    ir.uneed.calender.utils.language = language
    ir.uneed.calender.utils.shortWeekName = shortWeekName
    ir.uneed.calender.utils.isShowWeekOfYearEnabled = isShowWeekOfYearEnabled
    ir.uneed.calender.utils.otherCalendars = otherCalendars
    ir.uneed.calender.utils.showOtherCalendar = showOtherCalendar
    ir.uneed.calender.utils.boldAllText = boldAllText
    ir.uneed.calender.utils.boldWeekName = boldWeekName
    ir.uneed.calender.utils.boldDay = boldDay
    ir.uneed.calender.utils.fontDate = fontDate
    ir.uneed.calender.utils.footerTextColor = footerTextColor
    ir.uneed.calender.utils.weekNumberTextColor = weekNumberTextColor
    ir.uneed.calender.utils.weekDaysTextColor = weekDaysTextColor
    ir.uneed.calender.utils.otherDateTextColor = otherDateTextColor
    ir.uneed.calender.utils.dayTextColor = dayTextColor
    ir.uneed.calender.utils.todayBackgroundColor = todayBackgroundColor
    ir.uneed.calender.utils.todayTextColor = todayTextColor
    ir.uneed.calender.utils.colorTextHoliday = colorTextHoliday
    ir.uneed.calender.utils.colorSelectedBorder = colorSelectedBorder
}