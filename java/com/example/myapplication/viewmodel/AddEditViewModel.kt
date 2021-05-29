package com.example.myapplication.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Task
import com.example.myapplication.model.TaskDao
import com.example.myapplication.view.ADD_TASK_RESULT_OK
import com.example.myapplication.view.EDIT_TASK_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val taskDao: TaskDao
) : ViewModel() {
    //  task should be same as the one set in the nav graph as an argument
    val task = state.get<Task>("task")

    var taskName = state.get<String>("taskName") ?: task?.task ?: ""
        set(value) {
            field = value
            state.set("taskname", value)
        }

    var takImportance = state.get<Boolean>("takImportance") ?: task?.important ?: false
        set(value) {
            field = value
            state.set("takImportance", value)
        }

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    private fun createTask(task: Task) {
        viewModelScope.launch {
            taskDao.insert(task)
            addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
        }
    }

    fun onSaveClick() {
        if (taskName.isBlank()) {
            showInvalidInputMessage("Task cannot be empty")
            return;
        }
        if (task != null) {
            val updatedTask = task.copy(task = taskName, important = takImportance)
            updateTask(updatedTask)
        } else {
            val newTask = Task(task = taskName, important = takImportance)
            createTask(newTask)
        }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            taskDao.update(task)
            addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
        }
    }

    private fun showInvalidInputMessage(message: String) {
        viewModelScope.launch {
            addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(message))
        }
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val message: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }

}