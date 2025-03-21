package com.ujjolch.masterapp

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import co.yml.charts.common.extensions.isNotNull
import kotlinx.coroutines.delay

@Composable
fun Navigation(navController: NavController,
               authViewModel: AuthViewModel,
               bluetoothViewModel: BleScanViewModel = viewModel(),
               userDetailsViewModel: UserDetailsViewModel = viewModel(),
               logInSharedViewModel: LogInSharedViewModel = viewModel(),
               signInSharedViewModel: SignInSharedViewModel = viewModel()
){
    val currentUser by userDetailsViewModel.currentUser.observeAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
//    val isVerified by authViewModel.isVerified.observeAsState()
//    var isVerifiedValue by remember {
//        mutableStateOf<Boolean?>(null)
//    }
    var StartupDone by remember {
        mutableStateOf(false)
    }
    var startDestination by remember {
        mutableStateOf(Screen.IntroScreen.route)
    }
    var signinclick by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val activity = context as? Activity
    val currentLang = getSavedLanguage(context)

//    LaunchedEffect(isVerified) {
//        if (isVerified.isNotNull()){
//            when (val result = isVerified) {
//                is Result.Success -> {
//                       isVerifiedValue = result.data
//                }
//
//                is Result.Error -> {
//
//                }
//
//                null -> {}
//            }
//        }
//    }
    LaunchedEffect(currentLang) {
        if(currentLang.isNotNull()){
            setLocale(context,currentLang!!)
        }
    }
    LaunchedEffect(true) {
        userDetailsViewModel.gethistlist()
        userDetailsViewModel.updateUserData()
    }

    LaunchedEffect(currentUser) {
        if(currentUser.isNotNull()){
            while (true){
                delay(2000)
                Log.d("dfvvbe","${currentUser!!.email}")
                authViewModel.checkIfVerified(currentUser!!.email)
            }
        }
    }

    LaunchedEffect(isLoading, isLoggedIn,authViewModel.isUserVerified()) {
        if (!isLoading && !StartupDone) {
                if (isLoggedIn &&  authViewModel.isUserVerified()) {
                    startDestination = Screen.MainView.route
                    StartupDone = true

                } else if (isLoggedIn && !authViewModel.isUserVerified()) {
                    startDestination = Screen.VerifiyEmailScreen.route
                    StartupDone = true
                }
            else if(!isLoggedIn) {
                    startDestination = Screen.LogInScreen.route
                    StartupDone = true
            }
        }
    }
    NavHost(
        navController = navController as NavHostController,
        startDestination = startDestination
//        if(isLoading) Screen.IntroScreen.route else{
//        if(isLoggedIn)
//            Screen.MainView.route else Screen.LogInScreen.route}
    ) {
        composable(Screen.IntroScreen.route){
            IntroScreen()
        }
        composable(Screen.SignInScreen.route) {
            BackHandler {

            }
            SignUpScreen(authViewModel = authViewModel,
                signInSharedViewModel = signInSharedViewModel,
                OnNavigateToLogIn = {
                    navController.navigate(Screen.LogInScreen.route)
                },
                onSignInSuccess = { navController.navigate(Screen.VerifiyEmailScreen.route)},
                onNavigateToPrivacyPolicy = {navController.navigate(Screen.PrivacyPolicyScreen.route)})
        }
        composable(Screen.LogInScreen.route) {
            BackHandler {

            }
            LoginScreen(authViewModel = authViewModel,
                logInSharedViewModel = logInSharedViewModel,
                onNavigateTosignin = {
                    navController.navigate(Screen.SignInScreen.route)
                },
                onVerifiedLogInSuccess = { navController.navigate(Screen.MainView.route) },
                onUnverifiedLogInSuccess = {navController.navigate(Screen.VerifiyEmailScreen.route)},
                onNavigateToPrivacyPolicy = { navController.navigate(Screen.PrivacyPolicyScreen.route)},
                onNavigateToForgetPassword = {navController.navigate(Screen.ForgetPasswordScreen.route)},
                onGoogleFirstTimeLogIn = {navController.navigate(Screen.MainViewForUpdateDetails.route)})
        }
//        composable(Screen.UpdateDetailScreen.route) {
//            BackHandler {
//
//            }
//            UpdateDetailScreen(onNavigateToAddDevice = {
//                navController.navigate(Screen.AddDeviceScreen.route)
//            },
//                showUnitChanger = true)
//        }
        composable(Screen.MainViewForUpdateDetails.route){
            BackHandler {

            }
            MainViewForUpdateDetails(
                showBackButton = false,
                onNavigateBack = {},
                onSave = {
                    if(hasBluetoothPermissions(context) && hasLocationPermissions(context)) {
                        navController.navigate(Screen.AddDeviceNewNavigation.route)
                    }
                    else{
                        navController.navigate(Screen.MainView.route)
                    }
                }
            )
        }
//        composable(Screen.AddDeviceScreen.route) {
//            AddDevice( onNavigateToMainView = {
//                navController.navigate(Screen.MainView.route)
//            })
//        }
        composable(Screen.AddDeviceNewNavigation.route) {
            BackHandler {

            }
            AddDeviceNewNavigation(
                onNavigateToMeScreen = { navController.navigate(Screen.MainView.route)},
                backButtonEnabled = false
                )
        }
        composable(Screen.MainView.route) {
            MainView(authViewModel,
                onLogOutSuccess = {navController.navigate(Screen.LogInScreen.route)})
        }
        composable(Screen.PrivacyPolicyScreen.route) {
            // Check the previous destination
            val previousDestination: NavDestination? =
                navController.previousBackStackEntry?.destination
            PrivacyPolicyScreen(logInSharedViewModel = logInSharedViewModel,signInSharedViewModel = signInSharedViewModel) {
                if (previousDestination.isNotNull()){
                    if (previousDestination!!.route == Screen.LogInScreen.route) {
                        navController.navigate(Screen.LogInScreen.route)
                    } else if (previousDestination!!.route == Screen.SignInScreen.route) {
                        navController.navigate(Screen.SignInScreen.route)
                    }
            }
            }
        }
        composable(Screen.VerifiyEmailScreen.route){
            BackHandler {

            }
            EmailVerificationScreen(
                authViewModel = authViewModel,
                onVerificationComplete = {
//                    navController.navigate(Screen.MainViewForUpdateDetails.route)
                    navController.navigate(Screen.MainViewForUpdateDetails.route)},
                onLogOutSuccess = {navController.navigate(Screen.LogInScreen.route)}
            )
        }
        composable(Screen.ForgetPasswordScreen.route){
            ForgetPasswordScreen(onNavigateBack = {navController.navigate(Screen.LogInScreen.route) })
        }
    }
}