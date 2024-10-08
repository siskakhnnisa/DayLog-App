package com.powerpuff.daylog.countdown.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.powerpuff.daylog.utils.HabitSortType
import com.powerpuff.daylog.utils.SortUtils
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HabitRepository(private val habitDao: HabitDao, private val executor: ExecutorService) {

    companion object {

        const val PAGE_SIZE = 30
        const val PLACEHOLDER = true

        @Volatile
        private var instance: HabitRepository? = null

        fun getInstance(context: Context): HabitRepository {
            return instance ?: synchronized(this) {
                if (instance == null) {
                    val database = HabitDatabase.getInstance(context)
                    instance = HabitRepository(
                        database.habitDao(),
                        Executors.newSingleThreadExecutor()
                    )
                }
                return instance as HabitRepository
            }

        }
    }

    fun getHabits(filter: HabitSortType): LiveData<PagedList<Habit>> {
        val sortableQuery = SortUtils.getSortedQuery(filter)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(PLACEHOLDER)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .build()
        return LivePagedListBuilder(habitDao.getHabits(sortableQuery), config).build()
    }

    fun getHabitById(habitId: Int): LiveData<Habit> {
        return habitDao.getHabitById(habitId)
    }

    fun insertHabit(newHabit: Habit): Long {
        val habitData = Callable { habitDao.insertHabit(newHabit) }
        val executor = executor.submit(habitData)
        return executor.get()
    }

    fun deleteHabit(habit: Habit) {
        executor.execute {
            habitDao.deleteHabit(habit)
        }
    }

    fun searchHabitsByTitle(title: String): LiveData<PagedList<Habit>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(PLACEHOLDER)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .build()
        return LivePagedListBuilder(habitDao.searchHabitsByTitle(title), config).build()
    }



}