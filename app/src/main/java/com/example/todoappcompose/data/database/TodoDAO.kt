package com.example.todoappcompose.data.database


import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.todoappcompose.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

@Dao
abstract class TodoDAO  {

    /////////////////////////////////
    // Requests to GET Todos
    /////////////////////////////////
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(todo : Todo) : Long

    @Query("SELECT * FROM Todo")
    abstract suspend fun getTodos() : List<Todo>

    @Query("SELECT * FROM Todo WHERE id == :id")
    abstract suspend fun getTodo(id : Int) : Todo

    @Query("SELECT * FROM Todo ")
    abstract fun observeTodos() : Flow<List<Todo>>

    @RawQuery(observedEntities = [Todo::class])
    abstract fun observeTodos(query : SupportSQLiteQuery) : Flow<List<Todo>>

    fun observeTodos(query: TodoQuery) : Flow<List<Todo>> {

        //PageNumber starts at 1 and not 0, so we have to subtract 1
        val offset = (query.page - 1) * query.itemsPerPage

        val priority = query.filter.priority
        val completed = query.filter.completed


        val orderBy = when (query.sortBy) {
            TodoSortBy.Default -> "id ASC"
            TodoSortBy.Deadline -> "deadline ASC"
            TodoSortBy.Priority  -> "priority ASC"
        }


        val dbQuery = with( StringBuilder()) {

            append("SELECT * FROM Todo ")

            if (priority != null || completed != null) append(" WHERE ")

            if (priority != null) append(" priority == '${priority.value}' ")

            if (priority != null && completed != null) append(" AND ")

            if (completed != null) append(" completed == ${completed.toSqlBoolean()} ")

            append(" ORDER BY $orderBy ")
            append(" LIMIT ${query.itemsPerPage} OFFSET $offset")

            this.toString()
        }

        return observeTodos(SimpleSQLiteQuery(dbQuery))
    }

    /////////////////////////////////
    // Requests to UPDATE Todos
    /////////////////////////////////
    @Update
    abstract suspend fun update(vararg todo : Todo)

    /////////////////////////////////
    // Requests to DELETE Todos
    /////////////////////////////////
    @Delete
    abstract suspend fun delete(vararg todo : Todo)


}

private fun Boolean.toSqlBoolean() = if (this ) 1 else 0
