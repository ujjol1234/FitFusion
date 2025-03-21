package com.ujjolch.masterapp

import androidx.annotation.DrawableRes
import com.example.masterapp.R

sealed class Tab(val title:Int,@DrawableRes val icon:Int){
    object Weight:Tab(R.string.Weight, R.drawable.weight)
    object BMI:Tab(R.string.BMI,R.drawable.calculator)
    object BodyWaterPercent:Tab(R.string.BodyWaterP,R.drawable.baseline_water_drop_24)
    object BodyFatPercent:Tab(R.string.BodyFatP,R.drawable.google_fit)
    object SkeletalMuscleMass:Tab(R.string.SKM,R.drawable.dumbbell)
//    object BoneWeightPercent:Tab("Bone Weight%",R.drawable.bone)
    object LeanBodyMass:Tab(R.string.LBM,R.drawable.baseline_monitor_weight_24)
    object BMR:Tab(R.string.BMR,R.drawable.food_variant)
}

val listOfTabs = listOf(
    Tab.Weight,
    Tab.BMI,
    Tab.BodyWaterPercent,
    Tab.BodyFatPercent,
    Tab.SkeletalMuscleMass,
//    Tab.BoneWeightPercent,
    Tab.LeanBodyMass,
    Tab.BMR
)

