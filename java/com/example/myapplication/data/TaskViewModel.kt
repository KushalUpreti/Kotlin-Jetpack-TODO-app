package com.example.myapplication.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskDao: TaskDao,
) : ViewModel() {
    val searchQuery = MutableStateFlow("")
    val sortOrder = MutableStateFlow(SortOrder.BY_NAME)
    val hideCompleted = MutableStateFlow(false)


    @ExperimentalCoroutinesApi
    private val taskFlow = combine(searchQuery, sortOrder, hideCompleted)
    { query, sortOrder, hideCompleted ->
        Triple(query, sortOrder, hideCompleted)
    }.flatMapLatest { (query, sortOrder, hideCompleted) ->
        taskDao.getTasks(query, sortOrder, hideCompleted)
    }


    @ExperimentalCoroutinesApi
    val tasks = taskFlow.asLiveData()
}

enum class SortOrder { BY_DATE, BY_NAME }