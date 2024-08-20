package com.powerpuff.daylog.schedule.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.powerpuff.daylog.schedule.data.Course
import com.powerpuff.daylog.schedule.data.DataRepository
import com.powerpuff.daylog.utils.SortType
import java.util.Calendar

class ScheduleViewModel(private val repository: DataRepository) : ViewModel() {

    private val _sortParams = MutableLiveData<SortType>()
    private val _selectedDay = MutableLiveData<Int>()

    init {
        _sortParams.value = SortType.TIME
        _selectedDay.value = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) // Default to current day
    }

    val courses = _sortParams.switchMap {
        repository.getAllCourse(it)
    }

    val coursesByDay = _selectedDay.switchMap { day ->
        repository.getCoursesByDay(day)
    }

    fun sort(newValue: SortType) {
        _sortParams.value = newValue
    }

    fun setSelectedDay(day: Int) {
        _selectedDay.value = day
    }

    fun delete(course: Course) {
        repository.delete(course)
    }
}
