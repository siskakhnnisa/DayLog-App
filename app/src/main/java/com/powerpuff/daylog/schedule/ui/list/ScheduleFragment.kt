package com.powerpuff.daylog.schedule.ui.list

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.powerpuff.daylog.R
import com.powerpuff.daylog.schedule.data.Course
import com.powerpuff.daylog.schedule.paging.CourseAdapter
import com.powerpuff.daylog.schedule.paging.CourseViewHolder
import com.powerpuff.daylog.schedule.ui.ScheduleViewModelFactory
import com.powerpuff.daylog.schedule.ui.add.AddCourseActivity
import com.powerpuff.daylog.utils.DayInfo
import com.powerpuff.daylog.utils.SortType
import java.text.SimpleDateFormat
import java.util.*

class ScheduleFragment : Fragment() {

    private lateinit var viewModel: ScheduleViewModel
    private lateinit var rvCourse: RecyclerView
    private val courseAdapter: CourseAdapter by lazy {
        CourseAdapter(::onCourseClick)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true) // Enable options menu in fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Mengatur Toolbar
//        val toolbar: androidx.appcompat.widget.Toolbar = view.findViewById(R.id.toolbar)
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val factory = ScheduleViewModelFactory.createFactory(requireContext())
        viewModel = ViewModelProvider(this, factory).get(ScheduleViewModel::class.java)

        setFabClick(view)
        setUpRecycler(view)
        initAction()
        updateList()

        // Set the current date on the TextView
        val tvCurrentDate: TextView = view.findViewById(R.id.tv_current_date)
        val currentDate = getCurrentDate()
        tvCurrentDate.text = currentDate

        // Update the week dates and set up click listeners
        updateWeekDates(view)

        // Set the current day
        setCurrentDay(view)
    }

    private fun setUpRecycler(view: View) {
        rvCourse = view.findViewById(R.id.rv_course)
        rvCourse.layoutManager = LinearLayoutManager(requireContext())
        rvCourse.adapter = courseAdapter
    }

    private fun onCourseClick(course: Course) {
        // Intent to detail activity if needed
        // Intent(requireContext(), DetailActivity::class.java).also { it.putExtra(DetailActivity.COURSE_ID, course.id)
        //     startActivity(it)
        // }
    }

    private fun initAction() {
        val callback = Callback()
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvCourse)
    }

    private fun updateList() {
        viewModel.coursesByDay.observe(viewLifecycleOwner) {
            courseAdapter.submitList(it)
            view?.findViewById<TextView>(R.id.tv_empty_list)?.visibility =
                if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setFabClick(view: View) {
        view.findViewById<Button>(R.id.btn_add_course).setOnClickListener {
            Intent(requireContext(), AddCourseActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    inner class Callback : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return makeMovementFlags(0, ItemTouchHelper.RIGHT)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val course = (viewHolder as CourseViewHolder).getCourse()
            viewModel.delete(course)
        }

    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun updateWeekDates(view: View) {
        val daysOfWeek = listOf(
            DayInfo(R.id.tv_monday_day, R.id.tv_monday_date, R.id.ll_monday, Calendar.MONDAY),
            DayInfo(R.id.tv_tuesday_day, R.id.tv_tuesday_date, R.id.ll_tuesday, Calendar.TUESDAY),
            DayInfo(R.id.tv_wednesday_day, R.id.tv_wednesday_date, R.id.ll_wednesday, Calendar.WEDNESDAY),
            DayInfo(R.id.tv_thursday_day, R.id.tv_thursday_date, R.id.ll_thursday, Calendar.THURSDAY),
            DayInfo(R.id.tv_friday_day, R.id.tv_friday_date, R.id.ll_friday, Calendar.FRIDAY),
            DayInfo(R.id.tv_saturday_day, R.id.tv_saturday_date, R.id.ll_saturday, Calendar.SATURDAY),
            DayInfo(R.id.tv_sunday_day, R.id.tv_sunday_date, R.id.ll_sunday, Calendar.SUNDAY)
        )

        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val dateFormat = SimpleDateFormat("d", Locale.getDefault())

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        for ((dayTextId, dateTextId, layoutId, calendarDay) in daysOfWeek) {
            val dayTextView: TextView = view.findViewById(dayTextId)
            val dateTextView: TextView = view.findViewById(dateTextId)
            val layout: View = view.findViewById(layoutId)

            // Update the TextViews
            dayTextView.text = dayFormat.format(calendar.time)
            dateTextView.text = dateFormat.format(calendar.time)

            layout.setOnClickListener {
                // Reset colors for all days
                resetDayColors(view, daysOfWeek)
                // Set the clicked day and date to purple and underline
                val purpleColor = ContextCompat.getColor(requireContext(), R.color.header)
                dayTextView.setTextColor(purpleColor)
                dateTextView.setTextColor(purpleColor)
                dateTextView.paintFlags = dateTextView.paintFlags or Paint.UNDERLINE_TEXT_FLAG

                // Set the selected day in the ViewModel
                viewModel.setSelectedDay(calendarDay)
            }

            // Move to the next day
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    private fun resetDayColors(view: View, daysOfWeek: List<DayInfo>) {
        val defaultColor = ContextCompat.getColor(requireContext(), R.color.textColor)
        for ((dayTextId, dateTextId, _, _) in daysOfWeek) {
            val dayTextView: TextView = view.findViewById(dayTextId)
            val dateTextView: TextView = view.findViewById(dateTextId)
            dayTextView.setTextColor(defaultColor)
            dateTextView.setTextColor(defaultColor)
            dateTextView.paintFlags = dateTextView.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
        }
    }

    private fun setCurrentDay(view: View) {
        val daysOfWeek = listOf(
            DayInfo(R.id.tv_monday_day, R.id.tv_monday_date, R.id.ll_monday, Calendar.MONDAY),
            DayInfo(R.id.tv_tuesday_day, R.id.tv_tuesday_date, R.id.ll_tuesday, Calendar.TUESDAY),
            DayInfo(R.id.tv_wednesday_day, R.id.tv_wednesday_date, R.id.ll_wednesday, Calendar.WEDNESDAY),
            DayInfo(R.id.tv_thursday_day, R.id.tv_thursday_date, R.id.ll_thursday, Calendar.THURSDAY),
            DayInfo(R.id.tv_friday_day, R.id.tv_friday_date, R.id.ll_friday, Calendar.FRIDAY),
            DayInfo(R.id.tv_saturday_day, R.id.tv_saturday_date, R.id.ll_saturday, Calendar.SATURDAY),
            DayInfo(R.id.tv_sunday_day, R.id.tv_sunday_date, R.id.ll_sunday, Calendar.SUNDAY)
        )

        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_WEEK)

        val todayInfo = daysOfWeek.firstOrNull { it.calendarDay == today }
        todayInfo?.let { info ->
            val dayTextView: TextView = view.findViewById(info.dayTextId)
            val dateTextView: TextView = view.findViewById(info.dateTextId)

            // Set the clicked day and date to purple and underline
            val purpleColor = ContextCompat.getColor(requireContext(), R.color.header)
            dayTextView.setTextColor(purpleColor)
            dateTextView.setTextColor(purpleColor)
            dateTextView.paintFlags = dateTextView.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            // Set the selected day in the ViewModel
            viewModel.setSelectedDay(today)
        }
    }
}
