package com.powerpuff.daylog.countdown.data

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "habits")
@Parcelize
data class Habit(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @NonNull
    @ColumnInfo(name = "title")
    val title: String,

    @NonNull
    @ColumnInfo(name = "minutesFocus")
    val minutesFocus: Long,

    @NonNull
    @ColumnInfo(name = "startTime")
    val startTime: String,

    @NonNull
    @ColumnInfo(name = "priorityLevel")
    val priorityLevel: String
) : Parcelable