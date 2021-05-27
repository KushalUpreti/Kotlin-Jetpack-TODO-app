package com.example.myapplication.ui.addedittask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.Task
import com.example.myapplication.data.TaskDao
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val taskDao: TaskDao
):ViewModel() {
//  task should be same as the one set in the nav graph as an argument
    val task = state.get<Task>("task")

    var taskName = state.get<String>("taskName") ?: task?.task ?: ""
        set(value) {
            field = value
            state.set("taskname",value)
        }

    var takImportance = state.get<Boolean>("takImportance") ?: task?.important ?: false
        set(value) {
            field = value
            state.set("takImportance",value)
        }


}