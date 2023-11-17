package com.example.todoappcompose.data.database

import androidx.room.TypeConverter
import com.example.todoappcompose.data.TodoPriority

class Converters {

    @TypeConverter
    fun toIntPriority(priority: TodoPriority) = priority.value

    @TypeConverter
    fun fromIntPriority(priority: Int) = TodoPriority.values().first { it.value == priority }

}