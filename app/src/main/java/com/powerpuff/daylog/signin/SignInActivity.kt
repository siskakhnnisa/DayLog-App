package com.powerpuff.daylog.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.powerpuff.daylog.MainActivity
import com.powerpuff.daylog.R
import com.powerpuff.daylog.profile.ProfileFragment
import com.powerpuff.daylog.signup.SignUpActivity

class SignInActivity : AppCompatActivity() {

    private lateinit var loginUsername: EditText
    private lateinit var loginPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var signupRedirectText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        loginUsername = findViewById(R.id.login_username)
        loginPassword = findViewById(R.id.login_password)
        loginButton = findViewById(R.id.login_button)
        signupRedirectText = findViewById(R.id.signupRedirectText)

        loginButton.setOnClickListener {

            if (!validateUsername() || !validatePassword()) {
                // Validation failed
            } else {
                checkUser()
            }
        }

        signupRedirectText.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        supportActionBar?.hide()
    }

    private fun validateUsername(): Boolean {
        val value = loginUsername.text.toString()
        return if (value.isEmpty()) {
            loginUsername.error = "Username cannot be empty"
            false
        } else {
            loginUsername.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        val value = loginPassword.text.toString()
        return if (value.isEmpty()) {
            loginPassword.error = "Password cannot be empty"
            false
        } else {
            loginPassword.error = null
            true
        }
    }

    private fun checkUser() {
        val userUsername = loginUsername.text.toString().trim()
        val userPassword = loginPassword.text.toString().trim()

        val reference = FirebaseDatabase.getInstance().getReference("users")
        val checkUserDatabase = reference.orderByChild("username").equalTo(userUsername)

        checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    loginUsername.error = null
                    val passwordFromDB = snapshot.child(userUsername).child("password").getValue(String::class.java)

                    if (passwordFromDB == userPassword) {
                        loginUsername.error = null

                        // Set login state
                        val sharedPref = getSharedPreferences("userDetails", MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putBoolean("isLoggedIn", true)
                            apply()
                        }

                        val nameFromDB = snapshot.child(userUsername).child("name").getValue(String::class.java)
                        val emailFromDB = snapshot.child(userUsername).child("email").getValue(String::class.java)
                        val usernameFromDB = snapshot.child(userUsername).child("username").getValue(String::class.java)

                        // Save user details in SharedPreferences
                        with (sharedPref.edit()) {
                            putString("name", nameFromDB)
                            putString("email", emailFromDB)
                            putString("username", usernameFromDB)
                            putString("password", passwordFromDB)
                            apply()
                        }

                        // Simpan preferensi status login user
                        val loginPref = getSharedPreferences("loginStatus", Context.MODE_PRIVATE)
                        with (loginPref.edit()) {
                            putBoolean("isLoggedIn", true)
                            apply()
                        }

                        val intent = Intent(this@SignInActivity, MainActivity::class.java)
                        intent.putExtra("selectFragment", "HomeFragment")
                        startActivity(intent)
                        finish()
                    } else {
                        loginPassword.error = "Invalid Credentials"
                        loginPassword.requestFocus()
                    }
                } else {
                    loginUsername.error = "User does not exist"
                    loginUsername.requestFocus()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignInActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


}
