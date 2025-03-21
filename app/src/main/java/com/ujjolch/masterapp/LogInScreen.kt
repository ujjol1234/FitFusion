package com.ujjolch.masterapp

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.extensions.isNotNull
import com.example.masterapp.R
import kotlinx.coroutines.delay

//@Composable
//fun LogInScreen(onNavigateTosignin:()->Unit,
//                authViewModel: AuthViewModel,
//                onLogInSuccess:() -> Unit){
//    var email by remember { mutableStateOf("") }
//    var password by remember {
//        mutableStateOf("")
//    }
//    val result by authViewModel.authResult.observeAsState()
//    Column (verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier= Modifier
//            .fillMaxSize()
//            .padding(8.dp)){
//        OutlinedTextField(value = email,
//            onValueChange = {email=it},
//            label = { Text("Email") },
//            modifier = Modifier.padding(8.dp))
//        OutlinedTextField(value = password,
//            onValueChange = {password=it},
//            label = { Text("Password") },
//            modifier = Modifier.padding(8.dp))
//        Button(onClick = {
//            authViewModel.LogIn(email,password)
//            when(result){
//                is Result.Success->{
//                    onLogInSuccess()
//                }
//                is Result.Error ->{
//
//                }
//                else ->{}
//            }
//        }) {
//            Text(text = "Log In")
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        Text("Don't have an account? Sign up.",
//            modifier = Modifier.clickable { onNavigateTosignin() }
//        )
//    }
//}

@Composable
fun LoginScreen(onNavigateTosignin:()->Unit,
                authViewModel: AuthViewModel,
                logInSharedViewModel: LogInSharedViewModel,
                onGoogleFirstTimeLogIn:()-> Unit,
                onVerifiedLogInSuccess:() -> Unit,
                onUnverifiedLogInSuccess:()->Unit,
                 onNavigateToPrivacyPolicy:()->Unit,
                onNavigateToForgetPassword:()->Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val result by authViewModel.authResult.observeAsState()
    var loginclick by remember { mutableStateOf(false) }
    var loginclickforgoogle by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var loadingforgoogle by remember { mutableStateOf(false) }
    var resultCodeForGoogle by remember { mutableStateOf(0) }

    val cachedEmail by logInSharedViewModel.LogInEmail.observeAsState()
    val cachedPassword by logInSharedViewModel.Password.observeAsState()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidthDp = configuration.screenWidthDp.dp

    var Agreed by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val activity = context as? Activity

    val homeScreenBlue by remember {
        mutableStateOf( R.color.Home_Screen_Blue)
    }
    var hasbltpermission by remember {
        mutableStateOf(false)
    }
    // GoogleSignInManager instance
    val googleSignInManager = GoogleSignInManager(context)
    val authResultforgoogle by googleSignInManager.authResult.observeAsState()

    // Remember launcher for activity result
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("FB125", "Activity Result OK")
                googleSignInManager.handleSignInResult(
                    requestCode = GoogleSignInManager.RC_SIGN_IN,
                    resultCode = result.resultCode,
                    data = result.data
                )
                resultCodeForGoogle = -1
            } else {
//                val extras = result.data?.extras
//                if (extras != null) {
//                    for (key in extras.keySet()) {
//                    }
//                }
            }
        }
    )
    LaunchedEffect(loginclickforgoogle,resultCodeForGoogle) {
        if(loginclickforgoogle) {
            loadingforgoogle =true
            while (resultCodeForGoogle == -1) {
                loading = true
               if(isUserSignedInWithGoogle()) {
                   when(val result = checkIfFirstTimeUser()){
                       is Result.Success ->{
                           if(result.data == true){
                               onGoogleFirstTimeLogIn()
                               loginclickforgoogle = false
                           }
                           else{
                               onVerifiedLogInSuccess()
                               loginclickforgoogle = false
                           }
                       }

                       is Result.Error -> {
                           onVerifiedLogInSuccess()
                       }
                   }
               }
                delay(100)
            }
        }
    }
    LaunchedEffect(authResultforgoogle) {  //Running with a delay so we cant use it for navigation will use it giving error messages
        when(authResultforgoogle){
            is Result.Success -> {

            }
            is Result.Error -> {
                Toast.makeText(context,"Log in failed",Toast.LENGTH_SHORT).show()
                loading = false
                loadingforgoogle = false
            }

            null -> {}
        }
    }

    LaunchedEffect(loginclick) {
        if (loginclick) {
            loading = true
            while (loading) {
                when (result) {
                    is Result.Success -> {
                        if (authViewModel.isUserVerified()) {
                            onVerifiedLogInSuccess()
                        } else {
                            onUnverifiedLogInSuccess()
                        }
                        loading = false
                        loginclick = false
                    }

                    is Result.Error -> {
                        val exceptionMessage = if ((((result as Result.Error).exception.message)?.startsWith("The supplied auth credential is incorrect")) ?: false)
                            "Incorrect email or password"
                        else (result as Result.Error).exception?.message
                        Toast.makeText(
                            context,
                            "$exceptionMessage",
                            Toast.LENGTH_SHORT
                        ).show()
                        loading = false
                        loginclick = false
                        authViewModel.resetAuthResult()
                    }

                    else -> {}
                }
                delay(100)
            }
        }
    }
    LaunchedEffect(Unit) {
        while(!hasbltpermission){
            if(activity.isNotNull()) {
                hasbltpermission = hasBluetoothPermissions(activity!!)
            }
            delay(2000)
        }
    }
    LaunchedEffect(Unit) {
        if(activity.isNotNull()) {
            if (!hasBluetoothPermissions(activity!!)) {
                    requestBluetoothPermissionsWithoutNavigation(activity)
            }
            if(!hasLocationPermissions(activity!!)){
                requestLocationPermissionsWithoutNavigation(activity)
            }
        }
    }
    LaunchedEffect(hasbltpermission) {
        if(activity.isNotNull()) {
            if (hasbltpermission && !hasLocationPermissions(activity!!)) {
                requestLocationPermissionsWithoutNavigation(activity)

            }
        }

    }
    LaunchedEffect(cachedEmail,cachedPassword) {

        if(cachedEmail.isNotNull() && cachedEmail!!.isNotBlank()){
            email = cachedEmail!!
        }
        if(cachedPassword.isNotNull() && cachedPassword!!.isNotBlank()){
            password = cachedPassword!!
        }
    }

    val DynamicImageTextPadding = if(screenHeight>700.dp) (screenHeight.value/21.9).dp else 0.dp


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                })
            },
//        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(top =if(screenHeight.value<700) 25.dp else 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.new_app_logo),
                contentDescription = "Company Logo",
                modifier = Modifier.size(if(screenHeight>700.dp) 220.dp else 200.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(DynamicImageTextPadding))
            TransparentTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email"
            )
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))
            TransparentTextFieldForPassword(
                value = password,
                onValueChange = { password = it },
                label = "Password",
            )
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(4.dp))
            Row (Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End){
                Text(text = "Forget Password?",
                    color = colorResource(id = homeScreenBlue),
                    modifier = Modifier.clickable {
                        logInSharedViewModel.set(email,password)
                        onNavigateToForgetPassword()
                    })
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
//                Checkbox(
//                    checked = Agreed,
//                    onCheckedChange = { Agreed = it },
//                    colors = CheckboxDefaults.colors(
//                        checkedColor = Color.Green , // Change color when checked
//                        uncheckedColor = if (Agreed) Color.Green else Color.Red, // Change color when unchecked
//                        checkmarkColor = Color.White, // Color of the checkmark
//                    )
//                )
//                Text(
//                    text = "I have read and agreed on ",
//                    color = Color.Black
//                )
//                Text(
//                    text = "Privacy Policy",
//                    color = colorResource(id = homeScreenBlue),
//                    modifier = Modifier.clickable {
//                        logInSharedViewModel.set(email,password)
//                        onNavigateToPrivacyPolicy()
//                    }
//                )
            }
            Button(
                enabled = true,
                onClick = {
                    if(email.isNotBlank() && password.isNotBlank()) {
                        authViewModel.LogIn(email, password)
                        loginclick = true
                        logInSharedViewModel.delete()
//                    when(result){
//                            is Result.Success->{
//                                onVerifiedLogInSuccess()
//                            }
//                            is Result.Error ->{
//
//                            }
//                            else ->{}
//                        }
                    }
                          },
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black, // Customize the background color
                    contentColor = Color.White)
            ) {
                Text(
                    text = "Sign In",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
        }
            Spacer(modifier = Modifier.height(16.dp))
        Text("Don't have an account? Sign up.",
            color = colorResource(id = homeScreenBlue),
            modifier = Modifier.clickable {
                logInSharedViewModel.delete()
                onNavigateTosignin()
            }
        )
            Spacer(modifier = Modifier.height(16.dp))
            if(loading){
                LinearProgressIndicator()
            }
            Row (
                Modifier
                    .padding(bottom = 10.dp)){
               GoogleSignInButton {
                   val signinIntent = googleSignInManager.googleSignInClient.signInIntent
                launcher.launch(signinIntent)
                loginclickforgoogle = true
                logInSharedViewModel.delete()
               }

            }
//            Text(text = "$authResultforgoogle")
    }
//        Row (
//            Modifier
//                .padding(bottom = 10.dp)){
//            IconButton(onClick = {
//                val signinIntent = googleSignInManager.googleSignInClient.signInIntent
//                launcher.launch(signinIntent)
//                loginclickforgoogle = true
//                logInSharedViewModel.delete()
//            }) {
//
//                    Image(
//                        painter = painterResource(id = R.drawable.google_icon),
//                        contentDescription = "google sign in"
//                    )
//            }
//
//        }
}
}

