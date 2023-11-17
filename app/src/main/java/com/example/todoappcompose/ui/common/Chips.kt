package com.example.todoappcompose.ui.common

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoappcompose.ui.theme.ExtendedTheme
import com.example.todoappcompose.ui.theme.ToDoAppComposeTheme


@ExperimentalMaterialApi
@Composable
fun MyChip(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ExtendedTheme.colors.chipBackground,
    aboveBackgroundColor: Color = Color.Transparent,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) =
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        // Clipping must be done before everything else, notably before the *clickable* modifier
        // because otherwise the ripple effect will spill out of the clipping zone
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .then(modifier)
            .background(backgroundColor)
            .background(aboveBackgroundColor)
            .widthIn(min = 100.dp)
            .padding(vertical = 12.dp, horizontal = 18.dp)
    ) {

        leadingIcon?.let {
            leadingIcon()
            Spacer(Modifier.width(8.dp))
        }

        Text(text)

        AnimatedVisibility(
            visible = trailingIcon != null,
            enter = fadeIn(tween(100, 50)) + expandHorizontally(),
            exit = fadeOut(tween(100)) + shrinkHorizontally(),
        ) {
            Box(Modifier.padding(start = 8.dp).size(24.dp)) {
                trailingIcon?.invoke()
            }
        }

    }


@Composable
@ExperimentalMaterialApi
fun MyClickableChip(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ExtendedTheme.colors.chipBackground,
    onClick: () -> Unit
) =
    MyChip(
        text,
        modifier.clickable { onClick() },
        backgroundColor,
    )


@Composable
@ExperimentalMaterialApi
fun MySelectableChip(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ExtendedTheme.colors.chipBackground,
    leadingIcon: @Composable (() -> Unit)? = null,
    selected: Boolean,
    onClick: () -> Unit
) =
    MyChip(
        text,
        modifier = modifier.selectable(selected, onClick = onClick),
        backgroundColor = backgroundColor,
        aboveBackgroundColor =
        if (selected)
            ExtendedTheme.colors.chipSelected
        else
            Color.Transparent,
        leadingIcon = leadingIcon,
        trailingIcon =
        if (!selected) null
        else {
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                )
            }
        }
    )


@Preview
@Composable
@ExperimentalMaterialApi
private fun MyChipPreview() {
    ToDoAppComposeTheme {
        MyChip(
            text = "test",
            leadingIcon = { Icon(Icons.Filled.Add, null) },
            trailingIcon = { Icon(Icons.Filled.ShoppingCart, null) },
        )
    }
}

@Preview
@Composable
@ExperimentalMaterialApi
private fun MyClickableChipPreview() {
    ToDoAppComposeTheme {
        MyClickableChip("test", onClick = {})
    }
}


@Preview
@Composable
@ExperimentalMaterialApi
private fun MySelectableChipPreview() {
    var selected by remember { mutableStateOf(true) }

    ToDoAppComposeTheme {
        MySelectableChip(
            text = "test",
            leadingIcon = { Icon(Icons.Filled.Email, null) },
            selected = selected,
            onClick = { selected = !selected }
        )
    }
}