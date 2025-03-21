package com.ujjolch.masterapp

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Timer
import java.util.TimerTask


@Composable
fun BleDeviceScreen(bleScanViewModel: BleScanViewModel ) {
    val availableDevices by bleScanViewModel.availableDevices.observeAsState(emptyList())
    val pairedDevices by bleScanViewModel.pairedDevices.observeAsState(emptyList())
    val updatedAvailableDevices = remember { mutableStateListOf<BluetoothDevice>() }
    val isScanning = remember { mutableStateOf(false) }

    fun updatelist(){
        updatedAvailableDevices.clear()
        updatedAvailableDevices.addAll(availableDevices)
    }
    DisposableEffect(isScanning.value) {
        val timer = Timer()
        if (isScanning.value) {
            timer.schedule(object : TimerTask() {
                override fun run() {
                    updatelist()
                }
            }, 0, 3000) // Update every 3 seconds
        }
        onDispose {
            timer.cancel()
        }
    }
    Column {
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = { bleScanViewModel.getPairedDevices()
                      bleScanViewModel.startDiscovery()
                        isScanning.value = true},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Scanning")
        }
        Button(
            onClick = { bleScanViewModel.stopDiscovery()
                isScanning.value = false},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Stop Scanning")
        }
        Button(onClick = {updatelist() }) {
            Text("Refresh Screen")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn (Modifier.fillMaxWidth()){
            item {
                Text("Select your Device:", fontWeight = FontWeight.Bold)
            }
            items(updatedAvailableDevices) { device ->
                BleDeviceItem(device = device,bleScanViewModel)
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun BleDeviceItem(device: BluetoothDevice,bleScanViewModel: BleScanViewModel) {
    val data by bleScanViewModel.data.observeAsState()
    var showAdvertisement by remember { mutableStateOf(false) }
    var  messagebodyproperties by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = "Name: ${device.name ?: "No Name"}")
        Text(text = "Address: ${device.address}")
        Button(onClick = { showAdvertisement = !showAdvertisement }) {
            Text(if (showAdvertisement) "Hide Advertisement Data" else "Show Advertisement Data")
        }
        if (showAdvertisement) {
            val Device = data?.find { it.address == device.address }
//            Text("Advertisement Data: ${Device?.advertisementData}")
            messagebodyproperties = Device?.advertisementData?.subSequence(30,32).toString()
            Text("Message Body Prperties: ${messagebodyproperties}")
            Text("Data Type: ${getDataType(messagebodyproperties)}")
            Text("Decimal Point: ${getDecimalPoint(messagebodyproperties)}")
            Text("Unit: ${getUnit(messagebodyproperties)}")


            Row {
                Text(
                    "Weight = ${
                        addDecimalPoint2(
                            number = hexToDecimal(
                                ((Device?.advertisementData?.subSequence(
                                    12,
                                    17
                                )).toString()).replace("\\s".toRegex(), "")
                            ), binarydecimalpoint = getDecimalPoint(messagebodyproperties)
                        )
                    } "
                )
                Text(
                    text = if (getUnit(messagebodyproperties) == "00") "KG"
                    else if (getUnit(
                            messagebodyproperties
                        ) == "10"
                    ) "LB"
                    else
                    "KG"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

//fun addDecimalPoint(input: Int): Double {
//    val inputString = input.toString()
//    val length = inputString.length
//
//    // Handle cases where the input is less than 2 digits
//    if (length < 2) {
//        return input.toDouble()
//    }
//
//    val resultString = inputString.substring(0, length - 2) + "." + inputString.substring(length - 2)
//    return resultString.toDouble()
//}


// if (showAdvertisement) {
////            device.advertisementData?.let { data ->
////                Text(text = "Advertisement Data: ${data.joinToString(", ")}")
////            } ?: Text(text = "No Advertisement Data")
////        }