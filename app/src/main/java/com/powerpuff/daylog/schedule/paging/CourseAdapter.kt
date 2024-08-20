package com.powerpuff.daylog.schedule.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.powerpuff.daylog.R
import com.powerpuff.daylog.schedule.data.Course

class CourseAdapter(private val clickListener: (Course) -> Unit) :
    PagedListAdapter<Course, CourseViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Course>() {
            override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.item_course,parent,false)
        return CourseViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) { val course = getItem(position)!!
        holder.bind(course, clickListener)
    }
}