package ir.uneed.calender.utils

import java.util.*

enum class Language(private val code: String) {
    FA("fa"),//فارسی
    EN_IR("en"),//English (Iran)
    EN_US("en-US");//English

    private val language get() = code.replace(Regex("-(IR|AF|US)"), "")

    // en-IR and fa-AF aren't recognized by system, that's handled by #language
    fun asSystemLocale() = Locale(language)

    // Formatting "Day Month Year" considerations

    val my: String get() = "%s %s"

    fun getPersianMonths(): List<String> = when (this) {
        FA, EN_IR -> persianCalendarMonthsInPersian
        else -> arrayListOf()
    }

    fun getIslamicMonths(): List<String> = when (this) {
        FA, EN_IR -> islamicCalendarMonthsInPersian
        else -> arrayListOf()
    }

    fun getGregorianMonths() = when (this) {
        FA, EN_IR -> gregorianCalendarMonthsInPersian
        else -> arrayListOf()
    }

    fun getWeekDays(): List<String> = weekDaysInPersian
    fun getWeekDaysGeorgian(): List<String> = weekDaysInGeorgian

    fun getWeekDaysInitials(): List<String> = when (this) {
        EN_IR -> weekDaysInitialsInEnglishIran
        EN_US -> getWeekDaysGeorgian().map { it.substring(0, 3) }
        else -> getWeekDays().map { it.substring(0, 1) }
    }

    companion object {

        // These are special cases and new ones should be translated in strings.xml of the language
        private val persianCalendarMonthsInPersian = listOf12Items(
            "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد",
            "شهریور", "مهر", "آبان", "آذر", "دی",
            "بهمن", "اسفند"
        )
        private val islamicCalendarMonthsInPersian = listOf12Items(
            "مُحَرَّم", "صَفَر", "ربیع‌الاول", "ربیع‌الثانی", "جمادى‌الاولى", "جمادی‌الثانیه",
            "رجب", "شعبان", "رمضان", "شوال", "ذی‌القعده", "ذی‌الحجه"
        )
        private val gregorianCalendarMonthsInPersian = listOf12Items(
            "ژانویه", "فوریه", "مارس", "آوریل", "مه", "ژوئن", "ژوئیه", "اوت", "سپتامبر", "اکتبر",
            "نوامبر", "دسامبر"
        )
        private val weekDaysInPersian = listOf7Items(
            "شنبه", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنجشنبه", "جمعه"
        )
        private val weekDaysInGeorgian = listOf7Items(
            "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
        )
        private val weekDaysInitialsInEnglishIran = listOf7Items(
            "Sh", "Ye", "Do", "Se", "Ch", "Pa", "Jo"
        )
    }

}