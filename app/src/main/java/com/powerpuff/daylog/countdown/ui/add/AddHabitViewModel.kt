package com.powerpuff.daylog.countdown.ui.add

import androidx.lifecycle.ViewModel
import com.powerpuff.daylog.countdown.data.Habit
import com.powerpuff.daylog.countdown.data.HabitRepository

class AddHabitViewModel(private val habitRepository: HabitRepository) : ViewModel() {
    fun saveHabit(habit: Habit) {
        habitRepository.insertHabit(habit)
    }
}