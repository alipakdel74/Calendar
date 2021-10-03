package ir.uneed.calender.utils

import android.content.Context
import java.util.*

// Context preferably should be activity context not application
fun applyAppLanguage(context: Context) {
    val locale = language.asSystemLocale()
    Locale.setDefault(locale)
    val config = context.resources.configuration
    config.setLocale(locale)
    config.setLayoutDirection(locale)
    context.createConfigurationContext(config)
}

fun loadLanguageResources() {
    persianMonths = language.getPersianMonths()
    islamicMonths = language.getIslamicMonths()
    gregorianMonths = language.getGregorianMonths()
    weekDays = if (language == Language.EN_US) language.getWeekDaysGeorgian()else language.getWeekDays()
    weekDaysInitials = language.getWeekDaysInitials()
}