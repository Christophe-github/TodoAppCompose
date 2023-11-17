package com.example.todoappcompose.data

import androidx.room.*
import com.example.todoappcompose.data.database.Converters


@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true) val id : Int,
    val title : String,
    val description : String,
    @TypeConverters(Converters::class)
    val priority : TodoPriority,
    val deadline : Long,
    val completed : Boolean) {

    companion object {
        fun create(
            title: String = "",
            description: String = "",
            deadline: Long = 0,
            completed: Boolean = false) =
            Todo(0,title,description,TodoPriority.NONE,deadline,completed)
    }
}


