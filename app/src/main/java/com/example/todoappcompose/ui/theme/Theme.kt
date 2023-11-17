package com.example.todoappcompose.ui.theme

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.example.todoappcompose.R


/////////////  LIGHT Theme  /////////////

val LightColorPalette = lightColors(
    primary = Indigo700,
    primaryVariant = Indigo700Dark,
    secondary = Indigo700,
    secondaryVariant = Indigo700Dark,
    background = Color.White,
    )

val LightColorPaletteExtended = ExtendedColors(
    chipBackground = Color(0xffd9d9d9),
    chipSelected = Color(0xffafb7dd),
    priorityHigh = Red500,
    priorityMedium = Orange600,
    priorityLow = Green500,
    priorityNone = Blue500,)




/////////////  DARK Theme  /////////////

val DarkColorPalette = darkColors(
    primary = Indigo200,
    primaryVariant = Indigo200Dark,
    secondary = Indigo200,
    secondaryVariant = Indigo200Dark,
    background = Color(0xFF202020),
    surface = Color(0xFF24252b)
    )

val DarkColorPaletteExtended = ExtendedColors(
    chipBackground = Color(0xff313235),
    chipSelected = Color(0xff23368c),
    priorityHigh = Red700,
    priorityMedium = Orange600,
    priorityLow = Green700,
    priorityNone = Blue700,)




/////////////  RIPPLE Theme  /////////////

/*
private object TodoRippleTheme : RippleTheme {

    @Composable

    override fun defaultColor(): Color = MaterialTheme.colors.primary

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
        MaterialTheme.colors.background,
        lightTheme = !isSystemInDarkTheme()
    )
}

*/


/////////////  Main Theme  /////////////

@Composable
fun ToDoAppComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
//    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = TodoTypography,
        shapes = Shapes,
    ) {
        CompositionLocalProvider(
            LocalExtendedColors provides if (darkTheme) DarkColorPaletteExtended else LightColorPaletteExtended,
//            LocalRippleTheme provides TodoRippleTheme,
            content = content
        )
    }
}


//////// Functions to set theme from activity /////////
/////// TODO There is a way to do this with compose with the accompanist package /////////

fun Activity.isDarkMode(): Boolean {
    return when (window.context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
        Configuration.UI_MODE_NIGHT_YES -> {
            true
        }

        Configuration.UI_MODE_NIGHT_NO -> {
            false
        }

        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
            false
        }

        else -> {
            false
        }
    }
}

fun Activity.setStatusBarTheme(darkMode: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        if (!darkMode) {
            window?.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        @Suppress("DEPRECATION")
        if (!darkMode) {
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    } else {
        //On older devices we can't have dark icons so we must use a dark background to have a satisfying contrast
        window.statusBarColor =
            ContextCompat.getColor(applicationContext, R.color.indigo_700_dark)
    }
}

fun Activity.setNavigationBarTheme(darkMode: Boolean) {
    val background = if (darkMode) DarkColorPalette.background else LightColorPalette.background

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val color =
            android.graphics.Color.argb(background.alpha, background.red, background.green, background.blue)
        window.navigationBarColor = color
        if (!darkMode) {
            window?.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
            )
        }
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val color =
            android.graphics.Color.argb(background.alpha, background.red, background.green, background.blue)
        window.navigationBarColor = color
        @Suppress("DEPRECATION")
        if (!darkMode) {
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    } else {
        //On older devices we can't have dark icons so we must use a dark background to have a satisfying contrast
        window.navigationBarColor =
            ContextCompat.getColor(applicationContext, R.color.indigo_700_dark)
    }
}

