package ir.uneed.calender.ui

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ir.uneed.calender.Jdn
import ir.uneed.calender.utils.*

class DaysAdapter(
    val sharedDayViewData: SharedDayViewData, private val calendarPager: CalendarPager?
) : RecyclerView.Adapter<DaysAdapter.ViewHolder>() {

    var days = emptyList<Jdn>()
    var startingDayOfWeek: Int = 0
    var weekOfYearStart: Int = 0
    var weeksCount: Int = 0

    private var selectedDay = -1

    internal fun selectDay(dayOfMonth: Int) {
        val prevDay = selectedDay
        selectedDay = -1
        notifyItemChanged(prevDay)

        if (dayOfMonth == -1) return

        selectedDay = dayOfMonth + 6 + applyWeekStartOffsetToWeekDay(startingDayOfWeek)

        if (isShowWeekOfYearEnabled) selectedDay += selectedDay / 7 + 1

        notifyItemChanged(selectedDay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DayView(parent.context).also {
            it.layoutParams = sharedDayViewData.layoutParams
            it.sharedDayViewData = sharedDayViewData
        }
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    // days of week * month view rows
    override fun getItemCount(): Int = 7 * if (isShowWeekOfYearEnabled) 8 else 7

    private val todayJdn = Jdn.today

    inner class ViewHolder(itemView: DayView) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            val itemDayView = (v as? DayView) ?: return
            val jdn = itemDayView.jdn ?: return
            calendarPager?.let { it.onDayClicked(jdn)}
            selectDay(itemDayView.dayOfMonth)
        }

        override fun onLongClick(v: View): Boolean {
            onClick(v)
            val jdn = (v as? DayView)?.jdn ?: return false
            calendarPager?.let { it.onDayLongClicked(jdn) }
            return false
        }

        fun bind(pos: Int) {
            var position = pos
            val dayView = (itemView as? DayView) ?: return
            if (isShowWeekOfYearEnabled) {
                if (position % 8 == 0) {
                    val row = position / 8
                    if (row in 1..weeksCount) {
                        val weekNumber = formatNumber(weekOfYearStart + row - 1)
                        dayView.setWeekNumber(weekNumber)

                        dayView.isVisible = true
                    } else
                        setEmpty()
                    return
                }

                position = position - position / 8 - 1
            }

            val fixedStartingDayOfWeek = applyWeekStartOffsetToWeekDay(startingDayOfWeek)
            if (days.size < position - 6 - fixedStartingDayOfWeek) {
                setEmpty()
            } else if (position < 7) {
                val weekDayInitial =
                    if (shortWeekName) getInitialOfWeekDay(revertWeekStartOffsetFromWeekDay(position))
                    else getWeekDayName(revertWeekStartOffsetFromWeekDay(position))
                dayView.setInitialOfWeekDay(weekDayInitial)

                dayView.isVisible = true
                dayView.setBackgroundResource(0)
            } else {
                if (position - 7 - fixedStartingDayOfWeek >= 0) {
                    val day = days[position - 7 - fixedStartingDayOfWeek]

                    val isToday = day == todayJdn

                    val dayOfMonth = position - 6 - fixedStartingDayOfWeek
                    setOf("6").mapNotNull(String::toIntOrNull).forEach { weekEnds[it] = true }
                    val isHoliday = isWeekEnd((day + startingDayOfWeek - days[0]) % 7)
                    dayView.setDayOfMonthItem(
                        isToday,
                        pos == selectedDay,
                        isHoliday,
                        day,
                        dayOfMonth,
                        ""
                    )


                    dayView.isVisible = true
                } else {
                    setEmpty()
                }
            }
        }

        private fun setEmpty() {
            itemView.isVisible = false
        }
    }
}