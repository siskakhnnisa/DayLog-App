package com.powerpuff.daylog.schedule.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.powerpuff.daylog.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

@Database(entities = [Course::class], version = 1, exportSchema = false)
abstract class CourseDatabase : RoomDatabase() {

    abstract fun courseDao(): CourseDao

    companion object {
        @Volatile
        private var INSTANCE: CourseDatabase? = null

        fun getInstance(context: Context): CourseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CourseDatabase::class.java,
                    "course.db"
                )
                    .addCallback(CourseDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class CourseDatabaseCallback(
            private val context: Context
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        fillWithStartingData(context, database.courseDao())
                    }
                }
            }
        }

        private fun fillWithStartingData(context: Context, dao: CourseDao) {
            val json = loadJson(context)
            try {
                val gson = Gson()
                val jsonObject = gson.fromJson(json, JsonObject::class.java)
                val jsonArray = jsonObject.getAsJsonArray("course")
                val type = object : TypeToken<List<Course>>() {}.type
                val courses: List<Course> = gson.fromJson(jsonArray, type)
                dao.insertAll(courses)
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }

        private fun loadJson(context: Context): String {
            val resourceId = R.raw.course // ID resource dari file course.json
            return try {
                val `in` = context.resources.openRawResource(resourceId)
                val reader = BufferedReader(InputStreamReader(`in`))
                val builder = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    builder.append(line)
                }
                builder.toString()
            } catch (ex: IOException) {
                ex.printStackTrace()
                ""
            }
        }
    }
}
