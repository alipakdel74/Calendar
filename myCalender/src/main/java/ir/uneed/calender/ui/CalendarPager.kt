package ir.uneed.calender.ui


import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import ir.uneed.calender.ArrowView
import ir.uneed.calender.CalendarType
import ir.uneed.calender.Jdn
import ir.uneed.calender.R
import ir.uneed.calender.databinding.FragmentMonthBinding
import ir.uneed.calender.date.AbstractDate
import ir.uneed.calender.utils.*
import java.lang.ref.WeakReference
import java.util.*


class CalendarPager(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    // Public API
    var onDayClicked = fun(_: Jdn) {}
    var onDayLongClicked = fun(_: Jdn) {}
    var selectedThisMonth = fun(_: Jdn) {}
    var goToThisDateBtn = fun(_: View) {}

    // Selected month is visible current month of the pager, maybe a day is not selected on it yet
    var onMonthSelected = fun() {}

    fun setSelectedDay(jdn: Jdn, highlight: Boolean = true, monthChange: Boolean = true) {
        selectedJdn = if (highlight) jdn else null

        if (monthChange) {
            val today = Jdn.today.toCalendar(mainCalendar)
            val date = jdn.toCalendar(mainCalendar)
            viewPager.setCurrentItem(
                applyOffset((today.year - date.year) * 12 + today.month - date.month), true
            )
        }

        refresh()
    }

    private fun initGlobal(context: Context) {
        applyAppLanguage(context)
        loadLanguageResources()
    }

    // Public API, to be reviewed
    fun refresh(isEventsModified: Boolean = false) = pagesViewHolders
        .mapNotNull { it.get() }.forEach { it.pageRefresh(isEventsModified, selectedJdn) }

    private val pagesViewHolders = ArrayList<WeakReference<PagerAdapter.ViewHolder>>()

    // Package API, to be rewritten with viewPager.adapter.notifyItemChanged()
    private fun addViewHolder(vh: PagerAdapter.ViewHolder) = pagesViewHolders.add(WeakReference(vh))

    private val monthsLimit = 5000 // this should be an even number

    private fun getDateFromOffset(calendar: CalendarType, offset: Int): AbstractDate {
        val date = Jdn.today.toCalendar(calendar)
        var month = date.month - offset
        month -= 1
        var year = date.year

        year += month / 12
        month %= 12
        if (month < 0) {
            year -= 1
            month += 12
        }
        month += 1
        return calendar.createDate(year, month, 1)
    }

    private fun applyOffset(position: Int) = monthsLimit / 2 - position

    private val viewPager = ViewPager2(context)
    private var selectedJdn: Jdn? = null

    @ColorInt
    fun Context.resolveColor(attr: Int) = TypedValue().let {
        ContextCompat.getColor(this, attr)
    }

    init {
        initGlobal(context)

        val btn = btnGoto()
        btn.setOnClickListener {
            goToThisDateBtn.invoke(it)
        }

        viewPager.adapter = PagerAdapter()
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val selectedMonth = getDateFromOffset(mainCalendar, applyOffset(position))
                val thisPageJdn = Jdn(selectedMonth)
                selectedThisMonth(thisPageJdn)
                if (thisPageJdn.toPersianCalendar().month == Jdn.today.toPersianCalendar().month &&
                    thisPageJdn.toPersianCalendar().year == Jdn.today.toPersianCalendar().year
                )
                    btn.visibility = View.GONE
                else
                    btn.visibility = View.VISIBLE
                refresh()
            }
        })
        viewPager.setCurrentItem(applyOffset(0), false)

        addView(viewPager)
        addView(btn)
    }

    private fun btnGoto(): AppCompatImageView {
        val imageView = AppCompatImageView(context)
        imageView.id = generateViewId()
        imageView.setImageResource(R.drawable.ic_back_arrow)
        imageView.background = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                color = ColorStateList.valueOf(context.resolveColor(todayBackgroundColor))
            setSize(32.dp.toInt(), 32.dp.toInt())
        }

        val params =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.BOTTOM
        params.setMargins(8.dp.toInt(), 8.dp.toInt(), 8.dp.toInt(), 8.dp.toInt())
        imageView.setPadding(8.dp.toInt(), 8.dp.toInt(), 8.dp.toInt(), 8.dp.toInt())
        imageView.layoutParams = params
        return imageView
    }

    inner class PagerAdapter : RecyclerView.Adapter<PagerAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            FragmentMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

        override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

        override fun getItemCount() = monthsLimit

        inner class ViewHolder(private val binding: FragmentMonthBinding) :
            RecyclerView.ViewHolder(binding.root) {

            var pageRefresh = fun(_: Boolean, _: Jdn?) {}

            init {
                binding.monthView.initialize(SharedDayViewData(context, 40.sp), this@CalendarPager)

                if (showArrow) {
                    binding.previous.visibility = View.VISIBLE
                    binding.next.visibility = View.VISIBLE
                } else {
                    binding.previous.visibility = View.GONE
                    binding.next.visibility = View.GONE
                }

                binding.previous.let {
                    it.rotateTo(ArrowView.Direction.START)
                    it.setOnClickListener {
                        viewPager.setCurrentItem(viewPager.currentItem - 1, true)
                    }
                    it.setOnLongClickListener {
                        viewPager.setCurrentItem(viewPager.currentItem - 12, false)
                        true
                    }

                }

                binding.next.let {
                    it.rotateTo(ArrowView.Direction.END)
                    it.setOnClickListener {
                        viewPager.setCurrentItem(viewPager.currentItem + 1, true)
                    }
                    it.setOnLongClickListener {
                        viewPager.setCurrentItem(viewPager.currentItem + 12, false)
                        true
                    }
                }

                addViewHolder(this)
            }

            fun bind(position: Int) {
                val offset = applyOffset(position)
                val monthStartDate = getDateFromOffset(mainCalendar, offset)
                val monthStartJdn = Jdn(monthStartDate)
                val monthLength =
                    mainCalendar.getMonthLength(monthStartDate.year, monthStartDate.month)
                binding.monthView.bind(monthStartJdn, monthStartDate)

                pageRefresh = { isEventsModification: Boolean, jdn: Jdn? ->
                    if (viewPager.currentItem == position) {
                        if (isEventsModification && jdn != null)
                            onDayClicked(jdn)
                        else
                            onMonthSelected()

                        binding.monthView.selectDay(
                            if (jdn != null && jdn >= monthStartJdn && jdn - monthStartJdn + 1 <= monthLength)
                                jdn - monthStartJdn + 1
                            else -1
                        )
                    } else binding.monthView.selectDay(-1)
                }

                pageRefresh(false, selectedJdn)
            }
        }
    }
}
