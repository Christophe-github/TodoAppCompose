package com.example.todoappcompose

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.todoappcompose.data.*
import com.example.todoappcompose.navigation.TodoNavHost
import com.example.todoappcompose.ui.theme.*


@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainActivityCompose()
        }

        val isDarkMode = isDarkMode()
        setStatusBarTheme(isDarkMode)
        setNavigationBarTheme(isDarkMode)

    }


}


@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
private fun MainActivityCompose() {
    ToDoAppComposeTheme {
        TodoNavHost()
    }
}


@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_4)
@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_C)
@Composable
private fun MainActivityPreview() {
    MainActivityCompose()
}