package com.powerpuff.daylog.schedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.powerpuff.daylog.R
import com.powerpuff.daylog.customview.CustomTextInput
import com.powerpuff.daylog.utils.TimePickerFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var edCourseName: CustomTextInput
    private lateinit var edDay: Spinner
    private lateinit var edLecturer: CustomTextInput
    private lateinit var edNote: CustomTextInput
    private lateinit var btnStart: ImageButton
    private lateinit var btnEnd: ImageButton
    private lateinit var tvStart: TextView
    private lateinit var tvEnd: TextView
    private lateinit var btnSave: Button


    private lateinit var viewModel: AddCourseViewModel

    private var timeStart: String? = null
    private var timeEnd: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        supportActionBar?.title = getString(R.string.add_course)

        val viewModelFactory = AddCourseModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddCourseViewModel::class.java)

        init()

        btnStart.setOnClickListener {
            showDatePicker(TAG_START)
        }

        btnEnd.setOnClickListener {
            showDatePicker(TAG_END)
        }

        btnSave.setOnClickListener {
            saveCourse()
        }
    }

    private fun init() {
        edCourseName = findViewById(R.id.add_ed_course_name)
        edDay = findViewById(R.id.add_spinner_day)
        edLecturer = findViewById(R.id.add_ed_lecturer)
        edNote = findViewById(R.id.add_ed_note)
        btnStart = findViewById(R.id.add_btn_start)
        btnEnd = findViewById(R.id.add_btn_end)
        tvStart = findViewById(R.id.add_tv_start)
        tvEnd = findViewById(R.id.add_tv_end)
        btnSave = findViewById(R.id.btn_saved)
    }

    private fun saveCourse() {
        if (edCourseName.text.toString().isEmpty() || timeStart.isNullOrEmpty() || timeEnd.isNullOrEmpty()) {
            Toast.makeText(this, getString(R.string.input_empty_message), Toast.LENGTH_SHORT).show()
        } else {
            viewModel.insertCourse(
                edCourseName.text.toString(),
                edDay.selectedItemPosition,
                timeStart!!,
                timeEnd!!,
                edLecturer.text.toString(),
                edNote.text.toString()
            )
            Toast.makeText(this, "Insert data successful", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }



    private fun showDatePicker(tag: String) {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, tag)
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val timePicker = Calendar.getInstance()
        timePicker.set(Calendar.HOUR_OF_DAY, hour)
        timePicker.set(Calendar.MINUTE, minute)

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        if (tag == TAG_START) {
            tvStart.text = timeFormat.format(timePicker.time)
            timeStart = tvStart.text.toString()
        } else {
            tvEnd.text = timeFormat.format(timePicker.time)
            timeEnd = tvEnd.text.toString()
        }
    }

    companion object {
        const val TAG_START = "start"
        const val TAG_END = "end"
    }
}
