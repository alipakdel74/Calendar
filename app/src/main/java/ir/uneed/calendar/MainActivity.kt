package ir.uneed.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ir.uneed.calendar.databinding.ActivityMainBinding
import ir.uneed.calender.Jdn
import ir.uneed.calender.utils.applyOption
import ir.uneed.calender.utils.dayOfWeekName

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        applyOption(
            isShowWeekOfYearEnabled = false
        )

        bringDate(Jdn.today)


        mainBinding.calendarPager.apply {
            onDayClicked = {
                bringDate(it)
            }
            goToThisDateBtn = {
                bringDate(Jdn.today)
            }
            selectedThisMonth = {

            }
        }

    }

    private fun bringDate(jdn: Jdn) {
        mainBinding.calendarPager.setSelectedDay(jdn)
        mainBinding.txtWeekName.text = jdn.dayOfWeekName.plus(" ").plus("\n")
            .plus(jdn.toPersianCalendar().getDateString(true, " ")).plus("\n")
            .plus(jdn.toIslamicCalendar().getDateString(true, "  ")).plus("\n")
            .plus(jdn.toGregorianCalendar().getDateString(true, "  "))

    }

}