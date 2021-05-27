package com.example.myapplication.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.ADD_TASK_RESULT_OK
import com.example.myapplication.ui.EDIT_TASK_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val preferenceDataStore: PreferenceDataStore
) : ViewModel() {
    val searchQuery = MutableStateFlow("")
    val preferenceFlow = preferenceDataStore.preferenceFlow

    private val taskEventChannel= Channel<TaskEvent>()
    val taskEvent = taskEventChannel.receiveAsFlow()

    @ExperimentalCoroutinesApi
    private val taskFlow = combine(searchQuery,preferenceFlow)
    { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        taskDao.getTasks(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
    }

    fun updateSortOrder(sortOrder: SortOrder){
        viewModelScope.launch {
            preferenceDataStore.updateSortOrder(sortOrder)
        }
    }

    fun updateHideCompleted(hideCompleted:Boolean){
        viewModelScope.launch {
            preferenceDataStore.updateHideCompleted(hideCompleted)
        }
    }

    @ExperimentalCoroutinesApi
    val tasks = taskFlow.asLiveData()

    fun onTaskChecked(task:Task,isChecked:Boolean){
        viewModelScope.launch {
            taskDao.update(task.copy(checked =  isChecked))
        }
    }

    fun onTaskSwiped(task: Task){
        viewModelScope.launch {
            taskDao.delete(task.copy())
            taskEventChannel.send(TaskEvent.ShowUndoDeleteTaskMessage(task))
        }
    }

    fun onRestoreDeletedTask(task: Task){
        viewModelScope.launch {
            taskDao.insert(task)
        }
    }

    fun onAddNewTaskClick()= viewModelScope.launch {
        taskEventChannel.send(TaskEvent.NavigateToAddTaskScreen)
    }

    fun onTaskSelected(task: Task) = viewModelScope.launch {
        taskEventChannel.send(TaskEvent.NavigateToEditTaskScreen(task))
    }

    fun onAddEdotResult(result:Int){
        when(result){
            ADD_TASK_RESULT_OK->showTaskSavedMessage("Task added")
            EDIT_TASK_RESULT_OK->showTaskSavedMessage("Task updated")
        }
    }

    private fun showTaskSavedMessage(message:String){
        viewModelScope.launch {

        }
    }

    sealed class TaskEvent{
        data class ShowUndoDeleteTaskMessage(val task: Task):TaskEvent()
        object NavigateToAddTaskScreen:TaskEvent()
        data class NavigateToEditTaskScreen(val task: Task):TaskEvent()
        data class ShowTaskSavedConfirmationMessage(val msg:String):TaskEvent()
    }
}

enum class SortOrder { BY_DATE, BY_NAME }