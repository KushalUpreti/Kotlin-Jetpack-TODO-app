package com.example.myapplication.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "tasks")
@Parcelize
data class Task(
    val task: String,
    val checked: Boolean = false,
    val important: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {
    val createdDateFormatted: String get() = DateFormat.getDateTimeInstance().format(created)
}