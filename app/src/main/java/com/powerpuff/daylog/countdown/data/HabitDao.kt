package com.powerpuff.daylog.countdown.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface HabitDao {

    @RawQuery(observedEntities = [Habit::class])
    fun getHabits(query: SupportSQLiteQuery): DataSource.Factory<Int, Habit>

    @Query("Select * from habits where id = :habitId")
    fun getHabitById(habitId: Int): LiveData<Habit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHabit(habit: Habit): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg habits: Habit)

    @Delete
    fun deleteHabit(habits: Habit)

    @Query("SELECT * FROM habits WHERE priorityLevel = :priorityLevel ORDER BY RANDOM() LIMIT 1")
    fun getRandomHabitByPriorityLevel(priorityLevel: String): LiveData<Habit>

    @Query("SELECT * FROM habits WHERE title LIKE '%' || :title || '%'")
    fun searchHabitsByTitle(title: String): DataSource.Factory<Int, Habit>


}