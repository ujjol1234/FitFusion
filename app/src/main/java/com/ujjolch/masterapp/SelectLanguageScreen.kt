package com.ujjolch.masterapp

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.yml.charts.common.extensions.isNotNull
import com.example.masterapp.R

@Composable
fun SelectLanguageScreen(onNavigateBack:()->Unit) {
    val context = LocalContext.current
    val currentLang = getSavedLanguage(context)
    // State to trigger recomposition
    var localeState by remember { mutableStateOf(0) }
    var selectedLang by remember { mutableStateOf(Language.English.LanguageCode) }
    val LanguageText = remember(localeState) {
        mutableStateOf(R.string.Language)
    }
    LaunchedEffect(currentLang) {
        if (currentLang.isNotNull()) {
            selectedLang = currentLang!!
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row (
                        Modifier
                            .fillMaxWidth()
                            .padding(end = 70.dp)
                        , horizontalArrangement = Arrangement.Center){
                        androidx.compose.material.Text(text = stringResource(id = LanguageText.value),
                            color = Color.Black)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack()}) {
                        androidx.compose.material.Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back")
                    }
                },
                backgroundColor = Color.White,
                modifier = Modifier
                    .height(80.dp)
                    .padding(top = 25.dp),
                elevation = 0.dp)
        },
        content = {
    Box(
        Modifier
            .fillMaxSize()
            .padding(it)
            .padding(vertical = 30.dp)
            .padding(horizontal = 12.dp)
    ) {
        LazyColumn {
            items(listoflanguages) {
                LanguageItemRow(index = it.Language, isSelected = selectedLang == it.LanguageCode) {
                    selectedLang = it.LanguageCode
                    setLocale(context, selectedLang)
                    saveLanguage(context, selectedLang)
                    localeState++
                }
                Spacer(modifier = Modifier.height(15.dp))
            }
            item { 
                Spacer(modifier = Modifier.height(40.dp))
            }

        }

    }
})
    }
@Composable
fun LanguageItemRow(index:String,isSelected: Boolean, Select:()->Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                colorResource(id = R.color.Home_Screen_White),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    Select()
                })
            }
    ) {
        Text(
            text = index,
            modifier = Modifier.weight(1f),
        )
        Icon(
            painter = painterResource(id = R.drawable.baseline_check_circle),
            contentDescription = "Check Icon",
            tint = if (isSelected) Color.Green else Color.Gray
        )
    }
}

