package com.powerpuff.daylog.countdown.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.powerpuff.daylog.R
import com.powerpuff.daylog.countdown.data.Habit
import com.powerpuff.daylog.countdown.ViewModelFactory
import com.powerpuff.daylog.customview.CustomTextInput
import com.powerpuff.daylog.utils.TimePickerFragment
import java.text.SimpleDateFormat
import java.util.*

class AddHabitActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var viewModel: AddHabitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_habit)

        supportActionBar?.title = getString(R.string.add_habit)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(AddHabitViewModel::class.java)

        val saveButton = findViewById<Button>(R.id.btn_saved)
        saveButton.setOnClickListener {
            saveHabit()
        }
    }

    private fun saveHabit() {
        val title = findViewById<CustomTextInput>(R.id.add_ed_title).text.toString()
        val minutesFocusText = findViewById<CustomTextInput>(R.id.add_ed_minutes_focus).text.toString()
        val startTime = findViewById<TextView>(R.id.add_tv_start_time).text.toString()
        val priorityLevel = findViewById<Spinner>(R.id.sp_priority_level).selectedItem.toString()

        if (title.isEmpty() || minutesFocusText.isEmpty() || startTime.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_message), Toast.LENGTH_SHORT).show()
        } else {
            try {
                val minutesFocus = minutesFocusText.toLong()
                val habit = Habit(
                    title = title,
                    minutesFocus = minutesFocus,
                    startTime = startTime,
                    priorityLevel = priorityLevel
                )
                viewModel.saveHabit(habit)
                Toast.makeText(this, "Habit saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, getString(R.string.invalid_minutes), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun showTimePicker(view: View) {
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, "timePicker")
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        findViewById<TextView>(R.id.add_tv_start_time).text = dateFormat.format(calendar.time)
    }
}
