package com.ujjolch.masterapp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

//import com.example.masterapp.ui.theme.MasterAppTheme

class MainActivity : ComponentActivity() {
    private var btPermissionGranted = false
    private var locationPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        super.onCreate(savedInstanceState)

        fun scanBT() {
            val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
            if (bluetoothAdapter == null) {
                // Device doesn't support Bluetooth
            } else {
                val permissions = mutableListOf<String>()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    // Request both BLUETOOTH_CONNECT and BLUETOOTH_SCAN for Android 12+
                    permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
                    permissions.add(Manifest.permission.BLUETOOTH_SCAN)
                } else {
                    // Request BLUETOOTH and BLUETOOTH_ADMIN for pre-Android 12
                    permissions.add(Manifest.permission.BLUETOOTH)
                    permissions.add(Manifest.permission.BLUETOOTH_ADMIN)
                }
                // Add location permissions
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)

                bluetoothAndLocationPermissionLauncher.launch(permissions.toTypedArray())
            }
        }


        enableEdgeToEdge()
        setContent {
            val bluetoothViewModel: BleScanViewModel = viewModel()
            val userDetailsViewModel:UserDetailsViewModel = viewModel()
            val authViewModel: AuthViewModel = viewModel()
            val controller: NavController = rememberNavController()
            bluetoothViewModel.getPairedDevices()
//                scanBT()
//                BleDeviceScreen( bleScanViewModel = bluetoothViewModel)
//                BmiCalculatorScreen(bleScanViewModel = bluetoothViewModel)
//                            MainView()
//            DummyScreen4()
//                ForgetPasswordScreen({})
            Navigation(navController = controller, authViewModel = authViewModel)
//            scheduleDailyNotification(LocalContext.current,0,1,"Hi Ujjol")
//            dummyscreen()
//            ChatScreen(userDetailsViewModel = userDetailsViewModel , onBack = {})

        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.navigationBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
    private val bluetoothAndLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
            btPermissionGranted = true
            locationPermissionGranted = true
            if (bluetoothAdapter?.isEnabled == false) {
                val enableBTIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                btActivityResultLauncher.launch(enableBTIntent)
            }
            enableLocation()
        } else {
            // Handle the case where the permissions are not granted
        }
    }
    private val btActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            // Bluetooth has been enabled
        } else {
            // Handle the case where the user denied enabling Bluetooth
        }
    }
    private fun enableLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val enableLocationIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(enableLocationIntent)
        }
    }



//    private val leScanCallback = object : ScanCallback() {
//        override fun onScanResult(callbackType: Int, result: ScanResult) {
//            super.onScanResult(callbackType, result)
//            if (ContextCompat.checkSelfPermission(this@MainActivity, BLUETOOTH_CONNECT)
//                == PackageManager.PERMISSION_GRANTED
//            ) {
//                val device = result.device
//                val scanRecord = result.scanRecord
//                val deviceName = device.name
//                val deviceAddress = device.address
//                val advertisementData = scanRecord?.bytes
//
//                // Process the scanned device information
//                Log.d("RESULT","Device Name: $deviceName, Device Address: $deviceAddress")
//                advertisementData?.let {
//                    Log.d("RESULT","Advertisement Data: ${it.joinToString(", ")}")
//                }
//            }
//        }
}




@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MasterAppTheme {
//        Greeting("Android")
//    }
//}