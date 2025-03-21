package com.ujjolch.masterapp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.extensions.isNotNull
import com.example.masterapp.R
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun HeightSelectionScreen(sharedViewModel: SharedViewModel,
                          userDetailsViewModel: UserDetailsViewModel,
                          onSave:()->Unit){
    val dob by sharedViewModel.dob.observeAsState()
    val gender by sharedViewModel.gender.observeAsState()
    var selectedUnit by remember {
        mutableStateOf("in")
    }
    var feetpickervalue by remember {
        mutableStateOf("")
    }
    var cmpickervalue by remember {
        mutableStateOf(0)
    }
    var test by remember {
        mutableStateOf(0)
    }
    var initialvalue by remember {
        mutableStateOf(HeightInInchesValue[HeightInInchesValue.indexOf(feetpickervalue)])
    }
    val userData by userDetailsViewModel.userData.observeAsState()
    LaunchedEffect(userData) {
        if(userData.isNotNull()){
            var (ft,inches) = Calculate.convertCmToFeetAndInches(userData!!.heightincm)
            var inchesInInt = inches.roundToInt()
            if(inchesInInt==12){
                ft=ft+1
                inchesInInt =0
            }
           initialvalue = "$ft'$inchesInInt\""
        }
    }
    LaunchedEffect(feetpickervalue,selectedUnit) {
        if(feetpickervalue.isNotBlank() && selectedUnit == "in"){
            val inches = if (feetpickervalue.length > 4) feetpickervalue.substring(2, 4) else feetpickervalue[2].toString()
            val convertedfttocmforpicker = (Calculate.convertFeetAndInchesToCm((feetpickervalue[0]+"").toInt(),(inches).toDouble())).roundToInt()
            val cmformappingonpicker = convertedfttocmforpicker-3
                cmpickervalue = cmformappingonpicker
        }
    }
    LaunchedEffect(cmpickervalue,selectedUnit) {
        if(cmpickervalue!=0 && selectedUnit == "cm"){
            val (convertedftforftpicker, convertedinforftpicker) = (Calculate.convertCmToFeetAndInches(cmpickervalue.toDouble()))
            var ft = convertedftforftpicker
            var inch = convertedinforftpicker.roundToInt()
            if(inch==12){
                ft=ft+1
                inch =0
            }
            initialvalue = "$ft'$inch\""
        }
    }
    LaunchedEffect(Unit) {
        userDetailsViewModel.loadcurrentuser()
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 80.dp)){
        Column (Modifier.padding(top = 8.dp)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.yourHeight),
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
            }
        }
        Column (Modifier.align(Alignment.Center)){
            Row (
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                SelectableRow(value = "FT", selected = selectedUnit == "in",
                    {selectedUnit = "in"})
                Spacer(modifier = Modifier.width(20.dp))
                SelectableRow(value = "CM", selected = selectedUnit == "cm",
                    {selectedUnit = "cm"})
            }
            Spacer(modifier = Modifier.height(18.dp))
            if(selectedUnit == "in"){
                InHeightPicker(list = HeightInInchesValue, initialValue = remember(initialvalue) {
                    if(feetpickervalue.isNotBlank()) initialvalue else "3'4\""}) {
                    feetpickervalue = it
                }
            }
            else if(selectedUnit == "cm"){
                CMHeightPicker(initialValue = if(cmpickervalue!=0) cmpickervalue else 100) {
                    cmpickervalue = it
                }
            }
//            Text(text = "$cmpickervalue $feetpickervalue ${feetpickervalue.length} ${initialvalue}")
        }
        Button(
            onClick = {
                if(dob.isNotNull() && !gender.isNullOrBlank()){
                  if(selectedUnit == "cm"){
                      userDetailsViewModel.uploadDetails(UserData(cmpickervalue.toDouble(),
                          sharedViewModel.gender.value!!,
                          sharedViewModel.dob.value!!))
                    onSave()
                  }
                    else if(selectedUnit == "in"){
                      val inches = if (feetpickervalue.length > 4) feetpickervalue.substring(2, 4) else feetpickervalue[2].toString()
                        val convertedtocm = Calculate.convertFeetAndInchesToCm((feetpickervalue[0]+"").toInt(),inches.toDouble()).format(2).toDouble()
                      userDetailsViewModel.uploadDetails(UserData(convertedtocm,
                          sharedViewModel.gender.value!!,
                          sharedViewModel.dob.value!!))
                      onSave()
                    }
                }
                Log.d("Dtag","${userDetailsViewModel.currentUser.value?.email}  and ${UserData(180.00,
                    sharedViewModel.gender.value!!,
                    sharedViewModel.dob.value!!)}")

            },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black, // Customize the background color
                contentColor = Color.White
            )
        ) {
            Text(text = stringResource(id = R.string.Save),
                color = Color.White)
        }

    }
}

@Composable
fun SelectableRow(value: String, selected: Boolean,onTap:()->Unit) {
    val backgroundColor = if (selected) Color.Black else colorResource(id = R.color.Home_Screen_White)
    val textColor = if (selected) colorResource(id = R.color.Home_Screen_White) else Color.Black

    Row(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    onTap()
                })
            }
            .background(backgroundColor, shape = RoundedCornerShape(30.dp))
            .padding(PaddingValues(horizontal = 16.dp, vertical = 8.dp))
            .width(70.dp) // Adjust the width as needed
    ) {
        Text(
            text = value,
            color = textColor,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewHeightSelectionScreen() {
//    HeightSelectionScreen()
//}