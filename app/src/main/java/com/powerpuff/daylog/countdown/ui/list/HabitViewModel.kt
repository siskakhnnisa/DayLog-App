package com.powerpuff.daylog.countdown.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HabitViewModel : ViewModel() {
    private val _username = MutableLiveData<String>()

    fun setUsername(username: String) {
        _username.value = username
    }

    fun getUsername(): LiveData<String> {
        return _username
    }
}