package com.powerpuff.daylog.schedule.paging

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.powerpuff.daylog.R
import com.powerpuff.daylog.schedule.data.Course
import com.powerpuff.daylog.utils.DayName.Companion.getByNumber

class CourseViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val tvTime: TextView = view.findViewById(R.id.tv_time)
    private val tvCourse: TextView = view.findViewById(R.id.tv_course)
    private val tvLecturer: TextView = view.findViewById(R.id.tv_lecturer)
    private val tvNote: TextView = view.findViewById(R.id.tv_note)
    private lateinit var course: Course
    private val timeString = itemView.context.resources.getString(R.string.time_format)


    fun bind(course: Course, clickListener: (Course) -> Unit) {
        this.course = course

        course.apply {
            val dayName = getByNumber(day)
            val timeFormat = String.format(timeString, dayName, startTime, endTime)
            tvLecturer.text = lecturer
            tvTime.text = timeFormat
            tvNote.text = note
            tvCourse.text = courseName

        }

        itemView.setOnClickListener {
            clickListener(course)
        }
    }

    fun getCourse(): Course = course
}