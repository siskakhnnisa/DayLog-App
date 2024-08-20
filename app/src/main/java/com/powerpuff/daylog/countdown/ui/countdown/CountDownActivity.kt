package com.powerpuff.daylog.countdown.ui.countdown

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.powerpuff.daylog.R
import com.powerpuff.daylog.countdown.data.Habit
//import com.powerpuff.daylog.countdown.notification.NotificationWorker
import com.powerpuff.daylog.utils.*

class CountDownActivity : AppCompatActivity() {

    private lateinit var oneTimeWorkRequest: OneTimeWorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)
        supportActionBar?.title = "Count Down"


        val habit = intent.getParcelableExtra<Habit>(HABIT) as Habit

        findViewById<TextView>(R.id.tv_count_down_title).text = habit.title

        val viewModel = ViewModelProvider(this).get(CountDownViewModel::class.java)
        viewModel.setInitialTime(habit.minutesFocus)
        viewModel.currentTimeString.observe(this, {
            findViewById<TextView>(R.id.tv_count_down).text = it
        })

//        val channelName = getString(R.string.notify_channel_name)
//        var workManager = WorkManager.getInstance(this)

//        val data = Data.Builder()
//            .putInt(HABIT_ID, habit.id)
//            .putString(HABIT_TITLE, habit.title)
//            .putString(NOTIFICATION_CHANNEL_ID, channelName)
//            .build()

//        oneTimeWorkRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
//            .setInputData(data)
//            .addTag(NOTIF_UNIQUE_WORK)
//            .build()

//        viewModel.eventCountDownFinish.observe(this, {
//            if (it) {
//                workManager.enqueueUniqueWork(
//                    NOTIF_UNIQUE_WORK,
//                    ExistingWorkPolicy.REPLACE,
//                    oneTimeWorkRequest
//                )
//                updateButtonState(false)
//            }
//
//        })


        findViewById<Button>(R.id.btn_start).setOnClickListener {
            updateButtonState(true)
            viewModel.startTimer()

        }

        findViewById<Button>(R.id.btn_stop).setOnClickListener {
            viewModel.resetTimer()
            updateButtonState(false)
//            workManager.cancelUniqueWork(NOTIF_UNIQUE_WORK)
        }
    }

    private fun updateButtonState(isRunning: Boolean) {
        findViewById<Button>(R.id.btn_start).isEnabled = !isRunning
        findViewById<Button>(R.id.btn_stop).isEnabled = isRunning
    }
}