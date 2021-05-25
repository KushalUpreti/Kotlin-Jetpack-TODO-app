package com.example.myapplication.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    fun getTasks(query: String,sortOrder: SortOrder,hideCompleted: Boolean):Flow<List<Task>> =
        when(sortOrder){
            SortOrder.BY_NAME->getTasksSortedByName(query,hideCompleted)
            SortOrder.BY_DATE->getTasksSortedByDate(query,hideCompleted)
        }

    @Query("SELECT * FROM tasks WHERE (checked != :hideCompleted OR checked = 0)AND task LIKE '%' || :query || '%' ORDER BY important DESC, task")
    fun getTasksSortedByName(query: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE (checked!= :hideCompleted OR checked = 0)AND task LIKE '%' || :query || '%' ORDER BY important DESC, created")
    fun getTasksSortedByDate(query: String, hideCompleted: Boolean): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)


}