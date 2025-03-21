package com.ujjolch.masterapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun UserDetailNavigation(navController: NavController,
                         userDetailsViewModel: UserDetailsViewModel,
                         sharedViewModel: SharedViewModel = viewModel(),
                         onSave:()->Unit){
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.GenderScreen.route

    ){
        composable (Screen.GenderScreen.route){
           GenderSelectionScreen(sharedViewModel = sharedViewModel,
               userDetailsViewModel = userDetailsViewModel,
               onContinueClick = {navController.navigate(Screen.DOBScreen.route)})
        }
        composable (Screen.DOBScreen.route){
            DOBSelectorScreen(sharedViewModel = sharedViewModel,
                userDetailsViewModel = userDetailsViewModel,
                    onNavigateToHeightScreen = {navController.navigate(Screen.HeightScreen.route)})
        }
        composable (Screen.HeightScreen.route){
            HeightSelectionScreen(sharedViewModel = sharedViewModel,
                                    userDetailsViewModel = userDetailsViewModel,
                onSave = onSave)
        }
    }
}