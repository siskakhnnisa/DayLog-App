package com.powerpuff.daylog.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.powerpuff.daylog.R
import com.powerpuff.daylog.profile.EditProfileActivity
import com.powerpuff.daylog.signin.SignInActivity
import com.powerpuff.daylog.utils.HelperClass

class SignUpActivity : AppCompatActivity() {

    private lateinit var signupName: EditText
    private lateinit var signupUsername: EditText
    private lateinit var signupEmail: EditText
    private lateinit var signupPassword: EditText
    private lateinit var loginRedirectText: TextView
    private lateinit var signupButton: Button
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signupName = findViewById(R.id.signup_name)
        signupEmail = findViewById(R.id.signup_email)
        signupUsername = findViewById(R.id.signup_username)
        signupPassword = findViewById(R.id.signup_password)
        loginRedirectText = findViewById(R.id.loginRedirectText)
        signupButton = findViewById(R.id.signup_button)

        signupButton.setOnClickListener {
            val isNameValid = validateName()
            val isEmailValid = validateEmail()
            val isUsernameValid = validateUsername()
            val isPasswordValid = validatePassword()

            if (!isNameValid || !isEmailValid || !isUsernameValid || !isPasswordValid) {
                Toast.makeText(this@SignUpActivity, "Please fill in all fields correctly.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Process signup only if all validations are met
            database = FirebaseDatabase.getInstance()
            reference = database.getReference("users")

            val name = signupName.text.toString()
            val email = signupEmail.text.toString()
            val username = signupUsername.text.toString()
            val password = signupPassword.text.toString()

            val helperClass = HelperClass(name, email, username, password)
            reference.child(username).setValue(helperClass)

            Toast.makeText(this@SignUpActivity, "You have signed up successfully!", Toast.LENGTH_SHORT).show()

            // Set login state
            val sharedPref = getSharedPreferences("userDetails", MODE_PRIVATE)
            with(sharedPref.edit()) {
                putBoolean("isLoggedIn", true)
                apply()
            }

            // Pass data to EditProfileActivity
            val intentEditProfile = Intent(this@SignUpActivity, EditProfileActivity::class.java)
            intentEditProfile.putExtra("name", name)
            intentEditProfile.putExtra("email", email)
            intentEditProfile.putExtra("username", username)
            intentEditProfile.putExtra("password", password)
            startActivity(intentEditProfile)

            // Pass data to SignInActivity (optional if needed)
            val intentSignIn = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(intentSignIn)
        }

        loginRedirectText.setOnClickListener {
            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(intent)
        }

        supportActionBar?.hide()
    }

    private fun validateName(): Boolean {
        val value = signupName.text.toString()
        return if (value.isEmpty()) {
            signupName.error = "Name cannot be empty"
            false
        } else {
            signupName.error = null
            true
        }
    }

    private fun validateEmail(): Boolean {
        val value = signupEmail.text.toString()
        return if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            signupEmail.error = "Invalid email address"
            false
        } else {
            signupEmail.error = null
            true
        }
    }

    private fun validateUsername(): Boolean {
        val value = signupUsername.text.toString()
        return if (value.isEmpty()) {
            signupUsername.error = "Username cannot be empty"
            false
        } else {
            signupUsername.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        val value = signupPassword.text.toString()
        return if (value.isEmpty() || value.length < 6) {
            signupPassword.error = "Password must be at least 6 characters long"
            false
        } else {
            signupPassword.error = null
            true
        }
    }

}
