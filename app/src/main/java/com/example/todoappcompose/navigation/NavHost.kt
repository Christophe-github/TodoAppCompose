package com.example.todoappcompose.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.example.todoappcompose.ui.theme.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TodoNavHost() {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(navController = navController, startDestination = "todoList") {
        composable(
            route = "todoList",
            exitTransition = {
                when (targetState.destination.route) {
                    "editTodo/{todoId}" -> slideExit()
                    "addTodo" -> materialZAxisExit()
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "editTodo/{todoId}" -> slidePopEnter()
                    "addTodo" -> materialZAxisPopEnter()
                    else -> null
                }
            }
        ) {
            TodolistRoute(navController)
        }
        composable(
            route = "addTodo",
            enterTransition = { materialZAxisEnter() },
            popExitTransition = { materialZAxisPopExit() },
        ) {
            EditAddTodoRoute(navController, false)
        }

        composable(
            route = "editTodo/{todoId}",
            enterTransition = { slideEnter() },
            popExitTransition = { slidePopExit() },
        ) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getString("todoId")
            if (todoId != null)
                EditAddTodoRoute(navController, true, todoId)
        }

    }
}