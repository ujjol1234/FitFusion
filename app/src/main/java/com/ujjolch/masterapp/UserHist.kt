package com.ujjolch.masterapp

data class userHist(val history:List<hist>)


data class hist(val weight:Double,
                val impedance:Int,
                val date:String)