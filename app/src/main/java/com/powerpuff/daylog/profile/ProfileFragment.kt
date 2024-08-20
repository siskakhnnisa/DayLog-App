package com.powerpuff.daylog.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.powerpuff.daylog.R
import com.powerpuff.daylog.schedule.notification.DailyReminder
import com.powerpuff.daylog.signup.SignUpActivity
import com.powerpuff.daylog.utils.NightMode
import java.util.Locale

class ProfileFragment : Fragment() {

    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileUsername: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profileName = view.findViewById(R.id.tv_name)
        profileEmail = view.findViewById(R.id.tv_email)
//        profileUsername = view.findViewById(R.id.tv_username)

        val sharedPref = activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        profileName.text = sharedPref?.getString("name", "")
        profileEmail.text = sharedPref?.getString("email", "")
//        profileUsername.text = sharedPref?.getString("username", "")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mengatur Toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        // Mengatur Switch untuk Dark Mode
        val darkModeSwitch: Switch = view.findViewById(R.id.switch_dark_mode)
        darkModeSwitch.isChecked = isNightModeEnabled()
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            setNightMode(isChecked)
        }

        // Mengatur Switch untuk Notification Reminder
        val reminderSwitch: Switch = view.findViewById(R.id.reminder_notif)
        reminderSwitch.isChecked = isReminderEnabled()
        reminderSwitch.setOnCheckedChangeListener { _, isChecked ->
            setReminderNotification(isChecked)
        }

        // Set OnClickListener for Change Password
        val changePasswordTextView: TextView = view.findViewById(R.id.tv_changepassword)
        changePasswordTextView.setOnClickListener {
            changePasswordTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple11))
            val sharedPref = activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
            val intent = Intent(activity, EditProfileActivity::class.java).apply {
                putExtra("name", sharedPref?.getString("name", ""))
                putExtra("email", sharedPref?.getString("email", ""))
                putExtra("username", sharedPref?.getString("username", ""))
                putExtra("password", sharedPref?.getString("password", ""))
            }
            startActivity(intent)
        }

        // Set OnClickListener for Language
        val languageTextView: TextView = view.findViewById(R.id.tv_language)
        languageTextView.setOnClickListener {
            languageTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple11))
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }

        // Set OnClickListener for Sign Out
        val signOutTextView: TextView = view.findViewById(R.id.tv_logout)
        signOutTextView.setOnClickListener {
            signOutTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple11))
            val sharedPref = activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
            with (sharedPref?.edit()) {
                this?.clear() // Hapus semua data dari shared preferences
                this?.apply()
            }

            // Mengubah status login user
            val loginPref = activity?.getSharedPreferences("loginStatus", Context.MODE_PRIVATE)
            with (loginPref?.edit()) {
                this?.putBoolean("isLoggedIn", false)
                this?.apply()
            }

            // Arahkan user ke halaman sign up
            val intent = Intent(activity, SignUpActivity::class.java)
            startActivity(intent)
            activity?.finish() // Menutup activity saat ini
        }
    }

    private fun isNightModeEnabled(): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val nightModePref = sharedPreferences.getString(getString(R.string.pref_key_dark), NightMode.AUTO.name)
        val nightMode = NightMode.valueOf(nightModePref!!.toUpperCase(Locale.getDefault())).value
        return nightMode == AppCompatDelegate.MODE_NIGHT_YES
    }

    private fun setNightMode(isEnabled: Boolean) {
        val nightMode = if (isEnabled) {
            NightMode.ON.value
        } else {
            NightMode.OFF.value
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)

        // Simpan preferensi
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedPreferences.edit().putString(getString(R.string.pref_key_dark), if (isEnabled) NightMode.ON.name else NightMode.OFF.name).apply()

        // Buat ulang aktivitas agar perubahan diterapkan
        activity?.recreate()
    }

    private fun isReminderEnabled(): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        return sharedPreferences.getBoolean(getString(R.string.pref_key_reminder), false)
    }

    private fun setReminderNotification(isEnabled: Boolean) {
        val dailyReminder = DailyReminder()
        if (isEnabled) {
            dailyReminder.setDailyReminder(requireContext())
        } else {
            dailyReminder.cancelAlarm(requireContext())
        }

        // Simpan preferensi
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedPreferences.edit().putBoolean(getString(R.string.pref_key_reminder), isEnabled).apply()
    }
}
