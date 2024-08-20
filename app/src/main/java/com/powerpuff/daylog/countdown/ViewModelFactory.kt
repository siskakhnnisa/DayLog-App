package com.powerpuff.daylog.countdown

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.powerpuff.daylog.countdown.data.HabitRepository
import com.powerpuff.daylog.countdown.ui.add.AddHabitViewModel
import com.powerpuff.daylog.countdown.ui.detail.DetailHabitViewModel
//import com.powerpuff.daylog.countdown.ui.random.RandomHabitViewModel
import com.powerpuff.daylog.countdown.ui.list.HabitListViewModel

class ViewModelFactory private constructor(private val habitRepository: HabitRepository) :
    ViewModelProvider.Factory{

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    HabitRepository.getInstance(context)
                )
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(HabitListViewModel::class.java) -> {
                HabitListViewModel(habitRepository) as T
            }
            modelClass.isAssignableFrom(AddHabitViewModel::class.java) -> {
                AddHabitViewModel(habitRepository) as T
            }
            modelClass.isAssignableFrom(DetailHabitViewModel::class.java) -> {
                DetailHabitViewModel(habitRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}