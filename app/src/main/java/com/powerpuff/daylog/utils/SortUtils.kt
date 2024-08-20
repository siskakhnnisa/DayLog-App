package com.powerpuff.daylog.utils

import androidx.sqlite.db.SimpleSQLiteQuery

object SortUtils {

    fun getSortedQuery(sortType: HabitSortType): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM habits ")
        when (sortType) {
            HabitSortType.START_TIME -> {
                simpleQuery.append("ORDER BY time(startTime) ASC")
            }
            HabitSortType.MINUTES_FOCUS -> {
                simpleQuery.append("ORDER BY minutesFocus ASC")
            }
            HabitSortType.TITLE_NAME -> {
                simpleQuery.append("ORDER BY title ASC")
            }
            else -> {
                simpleQuery.append("ORDER BY startTime ASC") // or any other default sorting
            }
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }

}