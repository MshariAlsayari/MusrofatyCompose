package com.msharialsayari.musrofaty.ui_component

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.utils.mirror

object AppBarComponent {

    @Composable
    fun TopBarComponent(
        @StringRes title: Int?,
        onArrowBackClicked: () -> Unit = {},
        actions: @Composable RowScope.() -> Unit = {},
        isParent: Boolean = false
    ) {
        val navigationIcon: @Composable () -> Unit = {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .mirror()
                    .clickable { onArrowBackClicked() },
            )
        }


        val appbarTitle = if (title != null) stringResource(id = title) else ""
        AnimatedVisibility(
            visible = title != null,
        ) {

            TopAppBar(
                title = { Text(appbarTitle) },
                navigationIcon = if (isParent) null else navigationIcon,
                actions = actions,
                contentColor = Color.White
            )
        }

    }


    @Composable
    fun TopBarComponent(
        title: String?,
        onArrowBackClicked: () -> Unit = {},
        actions: @Composable RowScope.() -> Unit = {},
        isParent: Boolean = false
    ) {
        val navigationIcon: @Composable () -> Unit = {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .mirror()
                    .clickable { onArrowBackClicked() },
            )
        }


        val appbarTitle = title ?: ""
        AnimatedVisibility(
            visible = title != null,
        ) {

            TopAppBar(
                title = { Text(appbarTitle) },
                navigationIcon = if (isParent) null else navigationIcon,
                actions = actions,
                contentColor = Color.White
            )
        }

    }

    @Composable
    fun SearchTopAppBar(
        @StringRes title: Int?,
        onTextChange: (String) -> Unit,
        onSearchClicked: (String) -> Unit,
        isParent: Boolean = false,
        onArrowBackClicked: () -> Unit = {}
    ) {

        val trailingIconState = remember { mutableStateOf(TrailingIconState.DELETE) }
        val isSearchTopBar = remember { mutableStateOf(false) }
        val textFieldValue = remember { mutableStateOf("") }

        Box(
            modifier = Modifier
                .height(54.dp)
                .fillMaxWidth()
                .background(MusrofatyTheme.colors.toolbarColor)
        ){

            AnimatedVisibility(visible = isSearchTopBar.value) {

                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = Color.Transparent,
                    elevation = 0.dp
                ) {
                    TextField(
                        modifier = Modifier.fillMaxSize(),
                        value = textFieldValue.value,
                        onValueChange = {
                            textFieldValue.value = it
                            onTextChange(it)

                        },
                        placeholder = {
                            Text(
                                modifier = Modifier
                                    .alpha(ContentAlpha.medium),
                                text = stringResource(id = R.string.common_search),
                                color = Color.White
                            )
                        },
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp
                        ),
                        singleLine = true,
                        leadingIcon = {
                            IconButton(
                                modifier = Modifier
                                    .alpha(ContentAlpha.medium),
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                when  {
                                    trailingIconState.value == TrailingIconState.CLOSE || textFieldValue.value.isEmpty() -> {
                                        onTextChange("")
                                        textFieldValue.value = ""
                                        isSearchTopBar.value = false
                                        trailingIconState.value = TrailingIconState.DELETE
                                    }

                                    trailingIconState.value == TrailingIconState.DELETE -> {
                                        onTextChange("")
                                        textFieldValue.value = ""
                                        trailingIconState.value = TrailingIconState.CLOSE
                                    }

                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                onSearchClicked(textFieldValue.value)
                            }
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            backgroundColor = Color.Transparent
                        )
                    )
                }


            }

            AnimatedVisibility(visible = !isSearchTopBar.value) {
                TopBarComponent(title, onArrowBackClicked, actions = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier
                            .mirror()
                            .clickable {
                                isSearchTopBar.value = !isSearchTopBar.value
                            })

                }, isParent)
            }

        }








    }


}

enum class TrailingIconState {
    DELETE,
    CLOSE
}