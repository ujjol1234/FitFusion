package com.ujjolch.masterapp

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun ChangeUserScreen(userDetailsViewModel: UserDetailsViewModel,
                     onNavigateBack:()->Unit,
                     onNavigateToAddUser:()->Unit) {
    val subUserList by userDetailsViewModel.SubUserList.observeAsState()

    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = 45f,
        animationSpec = tween(durationMillis = 300)
    )

    LaunchedEffect(true) {
        delay(500)
        expanded = true
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.TopStart
    ) {
        LazyColumn {
            item {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(animationSpec = tween(800)) + fadeIn(
                    animationSpec = tween(
                        500
                    )
                ),
                exit = shrinkVertically(animationSpec = tween(300)) + fadeOut(
                    animationSpec = tween(
                        300
                    )
                )
            ) {
                Column {
                    Row (
                        Modifier
                            .fillMaxWidth()
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = {
                                    onNavigateBack()
                                })
                            }
                        ,
                        horizontalArrangement = Arrangement.Start){

                    FloatingActionButton(
                        onClick = { onNavigateBack() },
                        backgroundColor = Color.White,
                        contentColor = Color.Black
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            modifier = Modifier.rotate(rotation)
                        )
                    }
                }

                    Spacer(modifier = Modifier.height(24.dp))
                    subUserList?.forEach {
                        val userInitials = "${it.firstName.get(0)}${it.lastName.get(0)}"
                        SpeedDialAction(label = "${it.firstName} ${it.lastName}",
                            iconLabel = userInitials.uppercase()) {
                            userDetailsViewModel.clearUserDataAndHist()
                            userDetailsViewModel.setCurrentSubUser(it)
                            onNavigateBack()
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    Row (Modifier.fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                onNavigateToAddUser()
                            })
                        }
                        ,
                        horizontalArrangement = Arrangement.Start){
                        FloatingActionButton(
                            onClick = { onNavigateToAddUser() },
                            backgroundColor = Color.White,
                            contentColor = Color.Black
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add"
                            )
                        }

                    }

                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
    }
}

@Composable
fun SpeedDialAction(
    label: String,
    iconLabel:String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    onClick()
                })
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.size(52.dp),
            backgroundColor = Color.White
        ) {
            Text(text = iconLabel)
        }
        Text(
            text = label,
            fontSize = 16.sp,
            modifier = Modifier.padding(12.dp),
            color = Color.White
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewAddUserScreen() {
//    AddUserScreen()
//}