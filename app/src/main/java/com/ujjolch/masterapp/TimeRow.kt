package com.ujjolch.masterapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.masterapp.R

@Composable
fun TimeRow(onSelect:(String)->Unit) {
    var selectedOption by remember { mutableStateOf("Day") }

    val options = listOf("Day", "Month", "Year")

    Row(
        modifier = Modifier
            .padding(16.dp)
            .background(color = colorResource(id = R.color.Tab_UnSelected), shape = RoundedCornerShape(16.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { option ->
            val isSelected = selectedOption == option
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .background(
                        color = if (isSelected) Color.White else colorResource(id = R.color.Tab_UnSelected),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        selectedOption = option
                        onSelect(selectedOption)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    color = if (isSelected) colorResource(id = R.color.Home_Screen_Blue) else Color.Black,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimeRowPreview(){
    TimeRow({})
}