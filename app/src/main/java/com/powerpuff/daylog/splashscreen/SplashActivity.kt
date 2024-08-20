package com.powerpuff.daylog.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.powerpuff.daylog.MainActivity
import com.powerpuff.daylog.R
import com.powerpuff.daylog.signup.SignUpActivity

class SplashActivity : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 3000 // Delay in milliseconds (e.g., 3000 for 3 seconds)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Check if the user is already signed in
        val sharedPref = getSharedPreferences("userDetails", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        Handler().postDelayed({
            if (isLoggedIn) {
                // User is already signed in, redirect to MainActivity with HomeFragment
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                intent.putExtra("selectFragment", "HomeFragment")
                startActivity(intent)
            } else {
                // User is not signed in, redirect to SignUpActivity
                val intent = Intent(this@SplashActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
            finish() // Close SplashActivity
        }, SPLASH_DELAY)
    }
}

