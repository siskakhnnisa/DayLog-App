package com.powerpuff.daylog.utils

import android.provider.SyncStateContract
import com.powerpuff.daylog.utils.MIN_LENGTH_PASSWORD
import android.text.TextUtils
import android.util.Patterns
import android.view.View

fun isValidEmail(email: String): Boolean {
    return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun validateMinLength(password: String): Boolean {
    return !TextUtils.isEmpty(password) && password.length >= MIN_LENGTH_PASSWORD
}


fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}
