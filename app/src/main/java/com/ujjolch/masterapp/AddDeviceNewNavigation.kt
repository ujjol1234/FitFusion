package com.ujjolch.masterapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AddDeviceNewNavigation(navController: NavController = rememberNavController(),
                           bleScanViewModel: BleScanViewModel = viewModel(),
                           backButtonEnabled: Boolean = true,
                           userDetailsViewModel: UserDetailsViewModel = viewModel(),
                           onNavigateToMeScreen:()->Unit){
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.AddDeviceCheckerScreen.route

    ){
        composable(Screen.AddDeviceCheckerScreen.route){
            AddDeviceCheckerScreen(onNavigateToAddDeviceScreen = {navController.navigate(Screen.NewAddDeviceScreen.route)},
                onNavigateToMeScreen = { onNavigateToMeScreen() },
                backButtonEnabled = backButtonEnabled)
        }
        composable(Screen.NewAddDeviceScreen.route){
            NewAddDeviceScreen(bleScanViewModel = bleScanViewModel,
                userDetailsViewModel = userDetailsViewModel,
                onNavigateToMyDevice = {onNavigateToMeScreen()},
                backButtonEnabled = backButtonEnabled)
        }
    }
}