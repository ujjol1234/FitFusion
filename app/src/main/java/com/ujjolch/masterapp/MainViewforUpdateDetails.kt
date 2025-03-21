package com.ujjolch.masterapp

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.masterapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainViewForUpdateDetails(userDetailsViewModel: UserDetailsViewModel = viewModel(),
                             showBackButton:Boolean = true,
                            onNavigateBack:()->Unit,
                             onSave:()->Unit) {
    val controller: NavController = rememberNavController()
    var progress by remember { mutableStateOf(0f) }
    val animatedProgress by animateFloatAsState(targetValue = progress, animationSpec = tween(durationMillis = 1000))
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentPath = navBackStackEntry?.destination?.route

    LaunchedEffect(currentPath) {
        if(currentPath == Screen.GenderScreen.route){
            progress = 0.1f
        }
        else if(currentPath == Screen.DOBScreen.route){
            progress = 0.33f
        }
        else if(currentPath == Screen.HeightScreen.route){
            progress = 0.66f
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Row (
                            Modifier
                                .fillMaxWidth()
                                .padding(end = 20.dp)
                            , horizontalArrangement = Arrangement.Center){
                            androidx.compose.material.Text(
                                text = stringResource(id = R.string.BodyData),
                                color = Color.Black,
                                fontSize = 20.sp
                            )
                        }
                    },
                    navigationIcon = {
                        androidx.compose.material.IconButton(onClick = {
                            if(currentPath == Screen.HeightScreen.route){
                                controller.navigate(Screen.DOBScreen.route)
                            }
                            else if(currentPath == Screen.DOBScreen.route){
                                controller.navigate(Screen.GenderScreen.route)
                            }
                            else if(currentPath == Screen.GenderScreen.route){
                                onNavigateBack()
                            }
                        },
                            enabled = if(!showBackButton && currentPath == Screen.GenderScreen.route ) false else true) {
                            androidx.compose.material.Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = if(!showBackButton && currentPath == Screen.GenderScreen.route ) Color.White else Color.Black
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row (Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center){
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .height(4.dp),
                        color = Color.Yellow
                    )
                }
            }
        }
    ) { innerPadding ->
        Box (
            Modifier
                .fillMaxSize()
                .padding(innerPadding)){
            UserDetailNavigation(navController = controller,
                onSave = onSave,
                userDetailsViewModel = userDetailsViewModel)
        }

    }
}

//@Preview(showBackground = true)
//@Composable
//fun MainViewForUpdateDetailsPreview() {
//    MainViewForUpdateDetails()
//}
