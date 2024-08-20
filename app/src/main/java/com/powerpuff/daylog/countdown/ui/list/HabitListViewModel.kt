package com.powerpuff.daylog.countdown.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import com.powerpuff.daylog.R
import com.powerpuff.daylog.countdown.data.Habit
import com.powerpuff.daylog.countdown.data.HabitRepository
import com.powerpuff.daylog.utils.Event
import com.powerpuff.daylog.utils.HabitSortType

class HabitListViewModel(private val habitRepository: HabitRepository) : ViewModel() {

    private val _filter = MutableLiveData<HabitSortType>()

    val habits: LiveData<PagedList<Habit>> = _filter.switchMap {
        habitRepository.getHabits(it)
    }

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _undo = MutableLiveData<Event<Habit>>()
    val undo: LiveData<Event<Habit>> = _undo

    init {
        _filter.value = HabitSortType.START_TIME
    }

    fun filter(filterType: HabitSortType) {
        _filter.value = filterType
    }

    fun deleteHabit(habit: Habit) {
        habitRepository.deleteHabit(habit)
        _snackbarText.value = Event(R.string.habit_deleted)
        _undo.value = Event(habit)
    }

    fun insert(habit: Habit) {
        habitRepository.insertHabit(habit)
    }





    private val _searchQuery = MutableLiveData<String>()
    val searchResults: LiveData<PagedList<Habit>> = _searchQuery.switchMap { query ->
        habitRepository.searchHabitsByTitle(query)
    }

    fun searchHabitsByTitle(title: String) {
        _filter.value = HabitSortType.START_TIME  // Reset filter
        val query = title.trim()
        if (query.isNotEmpty()) {
            _filter.value = HabitSortType.CUSTOM // Set filter to custom to trigger new search
            _searchQuery.value = query
        }
    }



}