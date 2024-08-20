package com.powerpuff.daylog.countdown.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.powerpuff.daylog.R
import com.powerpuff.daylog.countdown.data.Habit
import com.powerpuff.daylog.countdown.ViewModelFactory
import com.powerpuff.daylog.countdown.ui.countdown.CountDownActivity
import com.powerpuff.daylog.utils.HABIT
import com.powerpuff.daylog.utils.HABIT_ID
import com.google.android.material.appbar.CollapsingToolbarLayout

class DetailHabitActivity : AppCompatActivity() {

    private lateinit var selectedHabit: Habit
    private lateinit var viewModel: DetailHabitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_habit)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val habitId = intent.getIntExtra(HABIT_ID, 0)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(DetailHabitViewModel::class.java)

        viewModel.start(habitId)
        viewModel.habit.observe(this, { habit ->
            if (habit != null) {
                selectedHabit = habit
                findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = habit.title
                findViewById<TextView>(R.id.detail_ed_time_minutes).setText(habit.minutesFocus.toString())
                findViewById<TextView>(R.id.detail_ed_start_time).setText(habit.startTime)
                when (habit.priorityLevel) {
                    resources.getStringArray(R.array.priority_level)[0] -> {
                        findViewById<View>(R.id.detail_priority_level).setBackgroundColor(
                            ContextCompat.getColor(this, R.color.high_priority)
                        )
                    }
                    resources.getStringArray(R.array.priority_level)[1] -> {
                        findViewById<View>(R.id.detail_priority_level).setBackgroundColor(
                            ContextCompat.getColor(this, R.color.medium_priority)
                        )
                    }
                    else -> {
                        findViewById<View>(R.id.detail_priority_level).setBackgroundColor(
                            ContextCompat.getColor(this, R.color.low_priority)
                        )
                    }
                }
            }
        })

        findViewById<Button>(R.id.btn_open_count_down).setOnClickListener {
            val intent = Intent(this, CountDownActivity::class.java)
            intent.putExtra(HABIT, selectedHabit)
            startActivity(intent)
        }
    }
}