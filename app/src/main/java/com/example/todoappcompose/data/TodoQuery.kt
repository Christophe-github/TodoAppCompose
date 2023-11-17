package com.example.todoappcompose.data

data class TodoQuery(
    val sortBy: TodoSortBy,
    val page: Int,
    val itemsPerPage: Int,
    val filter: TodoFilter

) {
    companion object {
        val DEFAULT = TodoQuery(
            TodoSortBy.Default,
            1,
            20,
            TodoFilter(null, null)
        )
    }
}


data class TodoFilter(
    val priority: TodoPriority?,
    val completed: Boolean?
)

enum class TodoPriority(val value: Int) : Comparable<TodoPriority> {
    HIGH(1),
    MEDIUM(2),
    LOW(3),
    NONE(4);


    fun isHigherThan(other: TodoPriority): Boolean = value < other.value

    fun isHigherOrEqualsThan(other: TodoPriority): Boolean = value <= other.value


}




