package com.powerpuff.daylog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.powerpuff.daylog.databinding.ActivityMainBinding
import com.powerpuff.daylog.countdown.ui.list.HomeFragment
import com.powerpuff.daylog.profile.ProfileFragment
import com.powerpuff.daylog.schedule.ui.list.ScheduleFragment
import com.powerpuff.daylog.utils.NightMode
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply theme based on preferences
        applyThemeFromPreferences()

        if (savedInstanceState == null) {
            // Application is opened for the first time
            replaceFragment(HomeFragment())
        } else {
            // Application has been opened before
            val fragmentTag = savedInstanceState.getString("current_fragment", HomeFragment::class.java.name)
            replaceFragment(Fragment.instantiate(this, fragmentTag))
        }
        setupBottomNavigation()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the current fragment
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_layout)
        outState.putString("current_fragment", currentFragment?.javaClass?.name ?: HomeFragment::class.java.name)
    }
    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment = when (item) {
                R.id.menuHome -> HomeFragment::class.java.name
                R.id.menuProfile -> ProfileFragment::class.java.name
                R.id.menuSchool -> ScheduleFragment::class.java.name
                else -> HomeFragment::class.java.name
            }
            replaceFragment(Fragment.instantiate(this, selectedFragment))
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
        // Save current fragment
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.edit().putString("current_fragment", fragment::class.java.name).apply()
    }

    private fun applyThemeFromPreferences() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val nightModePref = sharedPreferences.getString(getString(R.string.pref_key_dark), NightMode.AUTO.name)
        val nightMode = NightMode.valueOf(nightModePref!!.toUpperCase(Locale.getDefault())).value
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    override fun onResume() {
        super.onResume()
        // Restore the last selected fragment in the bottom navigation
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val fragmentTag = sharedPreferences.getString("current_fragment", HomeFragment::class.java.name)
        val selectedItemId = when (fragmentTag) {
            ProfileFragment::class.java.name -> R.id.menuProfile
            ScheduleFragment::class.java.name -> R.id.menuSchool
            else -> R.id.menuHome
        }
        binding.bottomNavigationView.setItemSelected(selectedItemId, true)
    }
}
