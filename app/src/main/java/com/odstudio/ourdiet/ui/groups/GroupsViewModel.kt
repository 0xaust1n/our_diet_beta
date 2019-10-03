package com.odstudio.ourdiet.ui.groups

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GroupsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Fuck Fragment"
    }
    val text: LiveData<String> = _text
}