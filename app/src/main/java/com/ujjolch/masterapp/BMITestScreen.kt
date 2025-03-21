package com.ujjolch.masterapp

//
//@OptIn(UnstableApi::class)
//@Composable
//fun BmiCalculatorScreen(bleScanViewModel: BleScanViewModel ) {
//    val availableDevices by bleScanViewModel.availableDevices.observeAsState(emptyList())
//    val MacAd = "D8:E7:2F:CA:C6:EA"
//    val data by bleScanViewModel.data.observeAsState()
//    val updateddata = remember {
//        mutableStateOf<Set<BleDevice>>(emptySet())
//    }
//    var finddevice by remember { mutableStateOf(false) }
//    var findmydeviceclick by remember { mutableStateOf(false) }
//    var updatedAvailableDevices by remember { mutableStateOf(emptyList<BluetoothDevice>()) }
//    val isScanning = remember { mutableStateOf(false) }
//    var weight by remember { mutableStateOf(0.0) }
//    var impedence by remember { mutableStateOf(0) }
//    var HeightinFeets by remember {mutableStateOf("")}
//    var HeightinInches by remember {mutableStateOf("")}
//    var Age by remember { mutableStateOf("") }
//    val heightInCm by remember {
//        derivedStateOf {
//            Calculate.convertFeetAndInchesToCm((HeightinFeets?:"0").toInt(), (HeightinInches?:"0.0").toDouble())
//        }
//    }
//    val BMI by remember {
//        derivedStateOf {
//            Calculate.BMI((Calculate.convertFeetAndInchesToCm((HeightinFeets.toDouble()).toInt(),HeightinInches.toDouble()).toDouble()),weight)
//        }
//    }
//
//    fun updateData() {
//        data?.let { devices ->
//            updateddata.value = updateddata.value - updateddata.value
//            updateddata.value = devices
//        }
//    }
//    LaunchedEffect(isScanning.value) {
//        if(isScanning.value) {
//            while (true) {
//                updateData()
//                delay(100)
//            }
//        }
//    }
//
//
//    fun updatelist(){
//        updatedAvailableDevices = availableDevices
////        Log.e("UJC3","${updatedAvailableDevices.toList()}")
//    }
//    DisposableEffect(isScanning.value) {
//        val timer = Timer()
//        if (isScanning.value) {
//            timer.schedule(object : TimerTask() {
//                override fun run() {
//                    updatelist()
//                }
//            }, 0, 3000) // Update every 3 seconds
//        }
//        onDispose {
//            timer.cancel()
//        }
//    }
//    LaunchedEffect(isScanning.value) {
////        if (!finddevice) {
////            for (device in updatedAvailableDevices) {
////                Log.e("UJC2","${device}")
////                if (device.address == MacAd) {
////                    finddevice = true
////                    break
////                }
////            }
////        }
//        if (!finddevice) {
//            val timer = Timer()
//            if (isScanning.value) {
//                timer.schedule(object : TimerTask() {
//                    override fun run() {
//                        for (device in updatedAvailableDevices) {
////                            Log.e("UJC2", "${device}")
//                            if (device.address == MacAd) {
//                                finddevice = true
//                                break
//                            }
//                        }
//                    }
//                }, 0, 3000)
//            }
//        }
//    }
//    Column(
//        Modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(40.dp))
//            OutlinedTextField(
//                value = HeightinFeets,
//                onValueChange = { HeightinFeets = it },
//                label = { Text("Height (Ft)") },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            )
//            OutlinedTextField(
//                value = HeightinInches,
//                onValueChange = { HeightinInches = it },
//                label = { Text("Height (In)") },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            )
//
//        OutlinedTextField(
//            value = Age,
//            onValueChange = { Age = it },
//            label = { Text("Age") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            modifier = Modifier.fillMaxWidth())
//
//
//        Button(onClick = {
//            bleScanViewModel.startDiscovery()
//            findmydeviceclick = true
////            updatelist()
//            isScanning.value = true
//        }) {
//            Text(text = "Find My Device")
//        }
////        Button(onClick = {Log.e("Uj","${data}")
////        updateData()}) {
////            Text(text = "L")
////        }
//        if (!findmydeviceclick) {
//            Text(
//                text = "PLEASE STAND ON THE DEVICE AND THEN CLICK THIS BUTTON",
//                fontWeight = FontWeight.Bold
//            )
//        } else if (finddevice) {
//            Text(text = "Device Found!", fontWeight = FontWeight.Bold)
//        } else if(isScanning.value){
//            Text(text = "Searching for device...", fontWeight = FontWeight.Bold)
//            Spacer(modifier = Modifier.padding(vertical = 16.dp))
//            LinearProgressIndicator(Modifier.fillMaxWidth())
//        }
//        Spacer(modifier = Modifier.height(24.dp))
//        if (finddevice) {
//            updateddata.value.forEach {
//                if (it.address == MacAd) {
//                    if(it.advertisementData.subSequence(30,32).toString() == "25"){
//                        isScanning.value = false
//                    }
//                    weight = addDecimalPoint(
//                        hexToDecimal(
//                            ((it.advertisementData.subSequence(12, 17)).toString())
//                                .replace("\\s".toRegex(), "")
//                        )
//                    )
////                    Text(text = (it.advertisementData.toString()))
////                    Text(text = (it.advertisementData.subSequence(18, 23)).toString())
//                    impedence =
//                        hexToDecimal(
//                            ((it.advertisementData.subSequence(18, 23)).toString())
//                                .replace("\\s".toRegex(), "")
//                        )
//
//                    Attribute(AtributeName = "Weight", AtributeValue = weight.toString())
//                }
//            }
//            Spacer(modifier = Modifier.height(12.dp))
//            Attribute(AtributeName = "BMI", AtributeValue = "${Calculate.BMI((Calculate.convertFeetAndInchesToCm((HeightinFeets.toDouble()).toInt(),HeightinInches.toDouble()).toDouble()),weight)}")
//            Spacer(modifier = Modifier.height(12.dp))
//            Attribute(AtributeName = "Body Water", AtributeValue = "${Calculate.BodyWaterForMale(Age.toInt(),heightInCm,weight)}")
//            Spacer(modifier = Modifier.height(12.dp))
//            Attribute(AtributeName = "Body Water%", AtributeValue = "${Calculate.BodyWaterPercent(BodyWeight = weight, BodyWater = Calculate.BodyWaterForMale(Age.toInt(),heightInCm,weight))}%")
//            Spacer(modifier = Modifier.height(12.dp))
//            Attribute(AtributeName = "Body Fat%", AtributeValue = "${Calculate.BodyFatPercentforMale(Age.toInt(),BMI)}%")
//            Spacer(modifier = Modifier.height(12.dp))
//            Attribute(AtributeName = "Lean Body Mass", AtributeValue = "${Calculate.LeanBodyMass(weight,Calculate.BodyFatPercentforMale(Age.toInt(),BMI))}")
//            Spacer(modifier = Modifier.height(12.dp))
//            Attribute(AtributeName = "BMR", AtributeValue = "${Calculate.BMRforMale(weight,heightInCm,Age.toInt())} Calories/Day")
//            Spacer(modifier = Modifier.height(12.dp))
//            Attribute(AtributeName = "Skeletal Muscle Mass", AtributeValue = "${Calculate.SkeletalMusscleMassforMale(impedence.toDouble(),heightInCm,Age.toInt())}")
//            Spacer(modifier = Modifier.height(12.dp))
//            Attribute(AtributeName = "Bone Weight%", AtributeValue = "${Calculate.BoneWeightPercentforMale(Age.toInt(),heightInCm)}%")
//        }
//
//    }
//}
//
//@Composable
//fun Attribute(AtributeName:String, AtributeValue:String){
//    Row(Modifier.fillMaxWidth()){
//        Text(text = "${AtributeName} = ", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
//        Text(text = AtributeValue, fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.padding(vertical = 3.dp))
//    }
//}
