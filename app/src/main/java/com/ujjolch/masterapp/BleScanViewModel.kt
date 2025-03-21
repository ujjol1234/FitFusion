package com.ujjolch.masterapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BleScanViewModel(application: Application) : AndroidViewModel(application) {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    private val _pairedDevices = MutableLiveData<List<BluetoothDevice>>()
    val pairedDevices: LiveData<List<BluetoothDevice>> = _pairedDevices

    private val _availableDevices = MutableLiveData<List<BluetoothDevice>>()
    val availableDevices: LiveData<List<BluetoothDevice>> = _availableDevices

    private val _data = MutableLiveData<Set<BleDevice>>()
    val data: LiveData<Set<BleDevice>> = _data

    private val discoveredData = mutableSetOf<BleDevice>()

    private val discoveredDevices = mutableListOf<BluetoothDevice>()

    @SuppressLint("MissingPermission")
    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
//            result?.let {
//                discoveredData.add(
//                    BleDevice(
//                        result.device.name
//                    )
//                )
//                _data.postValue(discoveredData)
//            }
            if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            result?.device?.let {
                val existingDevice = discoveredData.find { it.address == result.device.address }?:BleDevice("","","")
                val bleDevice = BleDevice(
                    name = result.device.name?:"No Name",
                    address = result.device.address?:"No Address",
                    advertisementData = result.scanRecord?.bytes?.joinToString(separator = " ") { String.format("%02X", it) } ?: ""
                )

                if (existingDevice != null) {
                    discoveredData.remove(existingDevice)
                }
                discoveredData.add(bleDevice)
                _data.postValue(discoveredData)
                _data.postValue(discoveredData)
                if (!discoveredDevices.contains(it)) {
                    discoveredDevices.add(it)
                    _availableDevices.postValue(discoveredDevices)
                }
            }
        }
//        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
//            super.onBatchScanResults(results)
//            results?.forEach { result ->
//                result.device?.let {
//                    if (!discoveredDevices.contains(it)) {
//                        discoveredDevices.add(it)
//                        _availableDevices.postValue(discoveredDevices)
//                    }
//                }
//            }
//        }
        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            var newDevicesFound = false
            results?.forEach { result ->
                result.device?.let {
            if (discoveredDevices.add(it)) {
                newDevicesFound = true
            }
        }
    }
    if (newDevicesFound) {
        _availableDevices.postValue(discoveredDevices.toList())
    }
}
        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
        }
    }
    fun startDiscovery() {
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions here if not granted
            return
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                discoveredDevices.clear()
                bluetoothAdapter?.bluetoothLeScanner?.startScan(leScanCallback)
            }
        }
    }
    fun stopDiscovery() {
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions here if not granted
            return
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getPairedDevices() {
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions here if not granted
            return
        }
        val devices = bluetoothAdapter?.bondedDevices?.toList() ?: emptyList()
        _pairedDevices.postValue(devices)
    }

    fun clearAvailableDevicesforRescan() {
        discoveredDevices.clear()
        _availableDevices.postValue(discoveredDevices.toList())
    }
    fun clearDataforRescan() {
        discoveredData.clear()
        _data.postValue(discoveredData)
    }
}