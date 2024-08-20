package com.powerpuff.daylog.utils

import java.util.concurrent.Executors

//COUNTDOWN

const val HABIT = "HABIT"
const val HABIT_ID = "HABIT_ID"
//const val HABIT_TITLE = "HABIT_TITLE"
const val NOTIFICATION_CHANNEL_ID = "notify-channel"
//const val NOTIF_UNIQUE_WORK: String = "NOTIF_UNIQUE_WORK"

//SCHEDULE

const val NOTIFICATION_CHANNEL_NAME = "Course Channel"
//const val NOTIFICATION_CHANNEL_ID_SCHEDULE= "notify-schedule"
const val NOTIFICATION_ID = 32
const val ID_REPEATING = 101

//SIGN IN SIGN OUT
const val MIN_LENGTH_PASSWORD = 8

private val SINGLE_EXECUTOR = Executors.newSingleThreadExecutor()

fun executeThread(f: () -> Unit) {
    SINGLE_EXECUTOR.execute(f)
}
