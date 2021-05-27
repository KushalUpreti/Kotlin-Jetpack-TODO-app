package com.example.myapplication.ui.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ToDoApplication
import com.example.myapplication.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class  DeleteAllViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val applicationScope:CoroutineScope
) : ViewModel(){

    fun onConfirmDeletion() = viewModelScope.launch {
        taskDao.deleteAll()
    }
}