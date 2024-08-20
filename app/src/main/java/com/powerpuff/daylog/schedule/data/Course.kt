package com.powerpuff.daylog.schedule.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.powerpuff.daylog.schedule.data.DataCourseName.TABLE_NAME
import com.powerpuff.daylog.schedule.data.DataCourseName.COL_ID
import com.powerpuff.daylog.schedule.data.DataCourseName.COL_COURSE_NAME
import com.powerpuff.daylog.schedule.data.DataCourseName.COL_DAY
import com.powerpuff.daylog.schedule.data.DataCourseName.COL_START_TIME
import com.powerpuff.daylog.schedule.data.DataCourseName.COL_END_TIME
import com.powerpuff.daylog.schedule.data.DataCourseName.COL_LECTURER
import com.powerpuff.daylog.schedule.data.DataCourseName.COL_NOTE


@Entity(tableName =TABLE_NAME)
data class Course(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COL_ID)
    val id: Int = 0,
    @ColumnInfo(name = COL_COURSE_NAME)
    val courseName: String,
    @ColumnInfo(name = COL_DAY)
    val day: Int,
    @ColumnInfo(name = COL_START_TIME)
    val startTime: String,
    @ColumnInfo(name = COL_END_TIME)
    val endTime: String,
    @ColumnInfo(name = COL_LECTURER)
    val lecturer: String,
    @ColumnInfo(name = COL_NOTE)
    val note: String
)
