package com.example.todoappcompose.ui.theme

import androidx.compose.animation.*
import androidx.compose.animation.core.tween


private const val scaleFactor = 0.05f
private const val biggerScale = 1 + scaleFactor
private const val smallerScale = 1 - scaleFactor

private const val durationExitMillis = 120
private const val durationEnterMillis = 200
private const val delayEnterMillis = 50



@ExperimentalAnimationApi
fun materialZAxisExit() =
    scaleOut(targetScale = biggerScale, animationSpec = tween(durationExitMillis)) + fadeOut(
        tween(durationExitMillis)
    )

@ExperimentalAnimationApi
fun materialZAxisPopExit() =
    scaleOut(
        targetScale = smallerScale,
        animationSpec = tween(durationExitMillis)
    ) + fadeOut(tween(durationExitMillis))


@ExperimentalAnimationApi
fun materialZAxisEnter() =
    scaleIn(
        initialScale = smallerScale,
        animationSpec = tween(durationEnterMillis, delayMillis = delayEnterMillis)
    ) +
            fadeIn(animationSpec = tween(durationEnterMillis, delayMillis = delayEnterMillis))


@ExperimentalAnimationApi
fun materialZAxisPopEnter() =
    scaleIn(
        initialScale = biggerScale,
        animationSpec = tween(durationEnterMillis, delayMillis = delayEnterMillis)
    ) + fadeIn(tween(durationEnterMillis, delayMillis = delayEnterMillis))


fun slideEnter() = slideInHorizontally(initialOffsetX = { it })

fun slidePopEnter() = slideInHorizontally(initialOffsetX = { -it })

fun slideExit() = slideOutHorizontally(targetOffsetX = { -it })

fun slidePopExit() = slideOutHorizontally(targetOffsetX = { it })