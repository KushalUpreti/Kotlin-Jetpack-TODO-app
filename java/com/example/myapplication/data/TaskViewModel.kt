package com.example.myapplication.data

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class TaskViewModel @Inject constructor(
    private val taskDao: TaskDao
) : ViewModel(){
}