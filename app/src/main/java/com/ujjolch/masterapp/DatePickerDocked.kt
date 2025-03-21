package com.ujjolch.masterapp

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked(onSelectDate:(String)->Unit) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""
    val customDatePickerColors = DatePickerDefaults.colors(
        todayDateBorderColor = Color.Transparent, // Remove the border around today's date
        todayContentColor = Color.Black,

        )
    LaunchedEffect(selectedDate) {
        onSelectDate(selectedDate)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    showDatePicker = false
                })
            })

    {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text("D.O.B") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)

        )

        if (showDatePicker) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 64.dp)
                    .shadow(elevation = 4.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier.pointerInput(Unit) {
                        detectTapGestures(onTap = {

                        })
                    },
                    title = { Text(text = "D.O.B") },
                    headline = {  },
                    showModeToggle = false,
                    colors = customDatePickerColors
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDismiss: () -> Unit,
    onSelectDate:(String)->Unit
) {
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""
    val customDatePickerColors = DatePickerDefaults.colors(
        todayDateBorderColor = Color.Transparent, // Remove the border around today's date
        todayContentColor = Color.Black,

        )
    LaunchedEffect(selectedDate) {
        onSelectDate(selectedDate)
    }

    DatePickerDialog(
        onDismissRequest = {onDismiss()},
        confirmButton = {
            TextButton(onClick = {
               onSelectDate(selectedDate)
                onDismiss()

            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss()}) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState,
            colors = customDatePickerColors)
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}
fun convertDateToMillis(dateString: String): Long {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.parse(dateString)?.time ?: 0L
}
fun millisToAge(millis: Long): Int {
    // Convert milliseconds to LocalDate
    val birthDate = Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    // Get the current date
    val currentDate = LocalDate.now()

    // Calculate the age by finding the period between birthDate and currentDate
    val age = Period.between(birthDate, currentDate).years

    return age
}

fun ageToMillis(age: Int): Long {
    // Get the current date
    val currentDate = LocalDate.now()

    // Subtract the age from the current date to get the birth date
    val birthDate = currentDate.minusYears(age.toLong())

    // Convert the birth date to milliseconds
    val millis = birthDate
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    return millis
}

fun getCurrentDateInMillis():Long{
    val currentDate = LocalDate.now()
    val millis = currentDate.atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    return millis
}

fun timeToMillis(hours: Int, minutes: Int, seconds: Int): Long {
    return (hours * 3600000L) + (minutes * 60000L) + (seconds * 1000L)
}