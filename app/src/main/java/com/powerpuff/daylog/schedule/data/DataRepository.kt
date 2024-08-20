package com.powerpuff.daylog.schedule.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.sqlite.db.SupportSQLiteQuery
import com.powerpuff.daylog.utils.QueryType
import com.powerpuff.daylog.utils.QueryUtil
import com.powerpuff.daylog.utils.SortType
import com.powerpuff.daylog.utils.executeThread
import java.util.Calendar

class DataRepository(private val dao: CourseDao) {

    fun getNearestSchedule(queryType: QueryType) : LiveData<Course?> {
        val nearQuery: SupportSQLiteQuery = QueryUtil.nearestQuery(type = queryType)
        return dao.getNearestSchedule(nearQuery)
    }

    fun getAllCourse(sortType: SortType): LiveData<PagedList<Course>> {
        return LivePagedListBuilder(
            dao.getAll(QueryUtil.sortedQuery(sortType)),
            PagedList.Config.Builder().apply {
                setPageSize(PAGE_SIZE)
            }.build()
        ).build()
    }

    fun getCourse(id: Int) : LiveData<Course> {
        return dao.getCourse(id)
    }

    fun getTodaySchedule(): List<Course> {
        val date = Calendar.getInstance()
        val day = date.get(Calendar.DAY_OF_WEEK)
        return dao.getTodaySchedule(day)
    }

    fun insert(course: Course) = executeThread {
        dao.insert(course)
    }

    fun delete(course: Course) = executeThread {
        dao.delete(course)
    }

    fun getCoursesByDay(day: Int): LiveData<PagedList<Course>> {
        return LivePagedListBuilder(
            dao.getCoursesByDay(day),
            PagedList.Config.Builder().apply {
                setPageSize(PAGE_SIZE)
            }.build()
        ).build()
    }


    companion object {
        @Volatile
        private var instance: DataRepository? = null
        private const val PAGE_SIZE = 10

        fun getInstance(context: Context): DataRepository? {
            return instance ?: synchronized(DataRepository::class.java) {
                if (instance == null) {
                    val database = CourseDatabase.getInstance(context)
                    instance = DataRepository(database.courseDao())
                }
                return instance
            }
        }
    }
}
