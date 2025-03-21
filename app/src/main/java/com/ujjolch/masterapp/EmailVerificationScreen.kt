package com.ujjolch.masterapp

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.common.extensions.isNotNull
import com.example.masterapp.R
import kotlinx.coroutines.delay

@Composable
fun EmailVerificationScreen(userDetailsViewModel: UserDetailsViewModel = viewModel(),
                            authViewModel: AuthViewModel,
                            onVerificationComplete:()->Unit,
                            onLogOutSuccess:()->Unit){

    val currentUser by userDetailsViewModel.currentUser.observeAsState()
    val isVerified by authViewModel.isVerified.observeAsState()
    val userLoggedIn by authViewModel.isLoggedIn.collectAsState()
    var enablecontinue by remember {
        mutableStateOf(false)
    }
    var logoutclick by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    val result by authViewModel.authResult.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(logoutclick) {
        if (logoutclick) {
            loading = true
            while (loading) {
                when (result) {
                    is Result.Success -> {
                        onLogOutSuccess()
                        loading = false
                        logoutclick = false
                        authViewModel.resetAuthResult()
                    }

                    is Result.Error -> {
                        Toast.makeText(
                            context,
                            "${(result as Result.Error).exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        loading = false
                        logoutclick = false
                        authViewModel.resetAuthResult()
                    }

                    else -> {}
                }
                delay(100)
            }
        }
    }

    LaunchedEffect(currentUser) {
        if(currentUser.isNotNull() && userLoggedIn){
            while (true){
                delay(3000)
                Log.d("dfvvbe","${currentUser!!.email}")
                authViewModel.checkIfVerified(currentUser!!.email)
            }
        }
    }

    LaunchedEffect(isVerified) {
        if (isVerified.isNotNull()){
            when (val result = isVerified) {
                is Result.Success -> {
                    if (result.data == true) {
                    onVerificationComplete()
//                        enablecontinue = true
                        authViewModel.resetIsVerified()
                    }
                }

                is Result.Error -> {

                }

                null -> {}
            }
    }
    }
    var send by remember {
        mutableStateOf(0)
    }
    var enableResend by remember {
        mutableStateOf(false)
    }
    var buttonClick by remember {
        mutableStateOf(false)
    }
    var remainingTime by remember { mutableStateOf(60) } // Timer starts from 60 seconds

    // Timer logic
    LaunchedEffect(remainingTime) {
        if (remainingTime > 0 && !enableResend) {
            delay(1000)
            remainingTime--
        } else if (remainingTime == 0) {
            enableResend = true
        }
    }

//    LaunchedEffect(true) {
//        delay(60000)
//        enableResend = true
//    }
    LaunchedEffect(send) {
        authViewModel.sendVerificationEmail()
    }
    LaunchedEffect(buttonClick) {
        if(buttonClick) {
            enableResend = false
            remainingTime = 60
            send++
            buttonClick = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column (
            Modifier
                .align(Alignment.TopCenter)
                .fillMaxHeight(0.4f),
            verticalArrangement = Arrangement.Bottom){
            Icon(painter = painterResource(id = R.drawable.baseline_email_24),
                modifier = Modifier.size(200.dp),
                tint = colorResource(id = R.color.Home_Screen_Blue),
                contentDescription = "email")
        }
        Column (
            Modifier
                .align(Alignment.Center)
                .padding(horizontal = 25.dp)){
            Text(text = "Verify Email",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "An email has been sent to your email address ${currentUser?.email}. " +
                    "Please click on the link to verify your email address")
        }
        Column (
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)){
//            Button(onClick = { onVerificationComplete() },
//                enabled = enablecontinue) {
//                Text(text = "Continue")
//            }
            Button(onClick = { buttonClick =true }
            ,modifier = Modifier
                    .width(200.dp)
                    .height(50.dp),
                enabled = enableResend,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black, // Customize the background color
                    contentColor = Color.White
                )) {
                if (enableResend) {
                    Text(text = "Resend Email")
                } else {
                    Text(text = "Resend in ${remainingTime}s")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { authViewModel.LogOut()
                userDetailsViewModel.clearUserDataAndHist()
                logoutclick = true
                             },
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black, // Customize the background color
                    contentColor = Color.White
                )) {
                Text(text = "Cancel")
            }
        }
    }
}