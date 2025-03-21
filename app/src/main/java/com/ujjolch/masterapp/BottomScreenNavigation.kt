package com.ujjolch.masterapp

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun BottomScreenNavigation(navController: NavController,pd: PaddingValues,
                           bluetoothViewModel: BleScanViewModel = viewModel(),
                           userDetailsViewModel: UserDetailsViewModel = viewModel(),
        authViewModel: AuthViewModel , onLogOutSuccess:()->Unit){
    var backPressedTime by remember { mutableStateOf(0L) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(true){
        withContext(Dispatchers.IO) {
            userDetailsViewModel.gethistlist()
            userDetailsViewModel.getUnits()
        }
    }
    NavHost(navController = navController as NavHostController,
        startDestination = Screen.BottomScreen.HomeScreen.route ){

        composable(Screen.BottomScreen.HomeScreen.route,
//            enterTransition =  {slideInHorizontally() }
        )
        {       BackHandler {

            handleBackButtonPress(
                context = context,
                backPressedTime = backPressedTime,
                onUpdateTime = { backPressedTime = it },
                onExit = { (navController.context as? Activity)?.finish() }
            )

        }

                HomeScreen( bleScanViewModel =  bluetoothViewModel,
                    onNavigateToHealthReport = {navController.navigate(Screen.HealthReportScreen.route)},
                    userDetailsViewModel = userDetailsViewModel,
                    onNavigateToAddUser = {navController.navigate(Screen.ChangeUserScreen.route)},
                    onNavigateToAIAssistant = {navController.navigate(Screen.AIAssistantScreen.route)})
        }
        composable(Screen.BottomScreen.HistoryScreen.route,
//            enterTransition =  {slideInHorizontally() }
        )
        {
            BackHandler {  //used for handling back button click
                handleBackButtonPress(   //used for exiting if back is pressed twice
                    context = context,
                    backPressedTime = backPressedTime,
                    onUpdateTime = { backPressedTime = it },
                    onExit = { (navController.context as? Activity)?.finish() }
                )
            }
            HistoryScreen(userDetailsViewModel)
        }
        composable(Screen.BottomScreen.MeScreen.route,
//            enterTransition =  {slideInHorizontally() }
        )
        {
            BackHandler {  //used for handling back button click
                handleBackButtonPress(   //used for exiting if back is pressed twice
                    context = context,
                    backPressedTime = backPressedTime,
                    onUpdateTime = { backPressedTime = it },
                    onExit = { (navController.context as? Activity)?.finish() }
                )
            }
            MyProfileScreen(onMyDevicesClick = {navController.navigate(ScreenInMeScreen.MyDevices.route)},
                            onUpdateDetailsClick = {navController.navigate(Screen.MainViewForUpdateDetails.route)},
                            onChangePasswordClick = {navController.navigate(ScreenInMeScreen.ChangePassword.route)},
                            onUnitClick = {navController.navigate(ScreenInMeScreen.Unit.route)},
                            onPrivacyPolicyClick = {navController.navigate(Screen.PrivacyPolicyScreen.route)},
                            onLanguageClick = {navController.navigate(Screen.SelectLanguageScreen.route)},
                            onLogOutSuccess = onLogOutSuccess,
                            authViewModel = authViewModel,
                            onUserManagementClick = {navController.navigate(Screen.UserManagementScreen.route)},
                            userDetailsViewModel = userDetailsViewModel)
        }
        composable(ScreenInMeScreen.MyDevices.route){
            MyDeviceScreen(onNavBackClicked = {navController.navigate(Screen.BottomScreen.MeScreen.route) },
                onAddDeviceClicked = {navController.navigate(Screen.AddDeviceNewNavigation.route)},
                userDetailsViewModel = userDetailsViewModel)
        }
//        composable(Screen.BottomScreenAddDevice.route){
//            BottomScreenAddDevice(userDetailsViewModel = userDetailsViewModel) {
//                navController.navigate(ScreenInMeScreen.MyDevices.route)
//            }
//        }
        composable(Screen.AddDeviceNewNavigation.route){
           AddDeviceNewNavigation(
               userDetailsViewModel = userDetailsViewModel,
               onNavigateToMeScreen = { navController.navigate(ScreenInMeScreen.MyDevices.route) })
        }
//        composable(Screen.BottomScreenUpdateDetails.route){
//            BottomScreenUpdateDetails (userDetailsViewModel = userDetailsViewModel,
//                onNavigateToMeScreen = { navController.navigate(Screen.BottomScreen.MeScreen.route)
//                    userDetailsViewModel.updateUserData()})
//        }
        composable(Screen.MainViewForUpdateDetails.route){
            MainViewForUpdateDetails(userDetailsViewModel = userDetailsViewModel,
                onNavigateBack = { if(navController.previousBackStackEntry?.destination?.route==Screen.BottomScreen.MeScreen.route){
                    navController.navigate(Screen.BottomScreen.MeScreen.route)}
                                 else if(navController.previousBackStackEntry?.destination?.route==Screen.UserManagementScreen.route){
                                     navController.navigate(Screen.UserManagementScreen.route)
                                 }},
                onSave = {navController.navigate(Screen.BottomScreen.MeScreen.route)})
        }
        composable(Screen.HealthReportScreen.route){
            HealthReportScreen(onBackClick = { navController.navigate(Screen.BottomScreen.HomeScreen.route) },
                userDetailsViewModel = userDetailsViewModel)
        }
        composable(ScreenInMeScreen.ChangePassword.route){
            ChangePasswordScreen(authViewModel = authViewModel,
                                onChangePasswordSuccess = {navController.navigate(Screen.BottomScreen.MeScreen.route)})
        }
        composable(ScreenInMeScreen.Unit.route){
            UnitScreen(userDetailsViewModel = userDetailsViewModel,
                onNavigateToMeScreen = {navController.navigate(Screen.BottomScreen.MeScreen.route)})
        }
        composable(Screen.ChangeUserScreen.route){
            ChangeUserScreen(userDetailsViewModel = userDetailsViewModel,
                onNavigateBack = {navController.navigate(Screen.BottomScreen.HomeScreen.route)},
                onNavigateToAddUser = {navController.navigate(Screen.AddUserScreen.route)})
        }
        composable(Screen.AddUserScreen.route){
            AddUserScreen(userDetailsViewModel = userDetailsViewModel,
                        onNavigateBack = {navController.navigate(Screen.BottomScreen.HomeScreen.route)})
        }
        composable(Screen.PrivacyPolicyScreen.route){
            PrivacyPolicyScreen(
                onNavigateBack = {navController.navigate(Screen.BottomScreen.MeScreen.route)}
            )
        }
        composable(Screen.UserManagementScreen.route){
            UserManagementScreen(userDetailsViewModel = userDetailsViewModel,
                onNavigateBack = {navController.navigate(Screen.BottomScreen.MeScreen.route) },
                onNavigateToAddUser = {
                    navController.navigate(Screen.AddUserScreen.route)
                },
                onNavigateToUserDetails = {navController.navigate(Screen.MainViewForUpdateDetails.route)})
        }
        composable(Screen.SelectLanguageScreen.route){
            SelectLanguageScreen(onNavigateBack = {navController.navigate(Screen.BottomScreen.MeScreen.route)})
        }
        composable(Screen.AIAssistantScreen.route){
            ChatScreen(userDetailsViewModel, onBack = {navController.navigate(Screen.BottomScreen.HomeScreen.route)})
        }
    }

}

fun handleBackButtonPress(
    context: Context,
    backPressedTime: Long,
    onUpdateTime: (Long) -> Unit,
    onExit: () -> Unit
) {
    val currentTime = System.currentTimeMillis()
    if (currentTime - backPressedTime < 2000) {
        onExit()
    } else {
        onUpdateTime(currentTime)
        Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
    }
}