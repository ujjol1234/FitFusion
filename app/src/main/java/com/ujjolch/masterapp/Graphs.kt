package com.ujjolch.masterapp


import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import co.yml.charts.common.model.Point


@Composable
fun WeightGraph(userHist: List<hist>,weightUnit:String,selectedTimeRange:Int) {

    var dataPoints:List<Point> = emptyList()
    var datehist:List<String> = emptyList()


    if(selectedTimeRange == 0){
       dataPoints = userHist.mapIndexed { index , hist ->
            if(weightUnit == "kg") {
                Point(x = index.toFloat(), y = hist.weight.toFloat())
            }
            else {
                val weightInPounds = Calculate.convertKgToPounds(hist.weight)
                Point(x = index.toFloat(), y = weightInPounds.toFloat())
            }
        }
        datehist =
            userHist.map { convert4digYearToNoYear(it.date) }

    }
    else if(selectedTimeRange == 1){  //Month
            dataPoints = MonthWeightConverter(userHist)
        if(weightUnit!="kg"){
            dataPoints = dataPoints.mapIndexed{ index, wt->
                val weightInPounds = Calculate.convertKgToPounds(wt.y.toDouble())
                Point(x = index.toFloat(), y = weightInPounds.toFloat())
            }
        }
        datehist = getMonthsAndYears(userHist)
    }
    else if(selectedTimeRange == 2){ //Year
            dataPoints = YearWeightConverter(userHist)
        if(weightUnit!="kg"){
            dataPoints = dataPoints.mapIndexed{ index, wt->
                val weightInPounds = Calculate.convertKgToPounds(wt.y.toDouble())
                Point(x = index.toFloat(), y = weightInPounds.toFloat())
            }
        }
        datehist = getYears(userHist)


    }
    if(dataPoints.size>1) {
        CustomGraph(datehist, dataPoints, weightUnit, selectedTimeRange = selectedTimeRange)
    }
    else{
        val data = dataPoints.get(0)
        val date = datehist.get(0)
        GraphForSingleValue(label = date, value = (data.y).toDouble(), unit = weightUnit)
    }


}

@Composable
fun BMIGraph(userHist: List<hist>,HeightInCM:Double,selectedTimeRange:Int){
    var dataPoints:List<Point> = emptyList()
    var datehist:List<String> = emptyList()

    if(selectedTimeRange == 0) {
        dataPoints = userHist.mapIndexed { index, hist ->
            Point(index.toFloat(), Calculate.BMI(HeightInCM, hist.weight).toFloat())
        }
        datehist =  userHist.map { convert4digYearToNoYear(it.date) }
    }
    else if(selectedTimeRange == 1){
        val dp = MonthWeightConverter(userHist)
        dataPoints = dp.mapIndexed{ index,point ->
            Point(index.toFloat(), Calculate.BMI(HeightInCM, (point.y).toDouble()).toFloat())
        }
        datehist = getMonthsAndYears(userHist)
    }
    else if(selectedTimeRange == 2){
        val dp = YearWeightConverter(userHist)
        dataPoints= dp.mapIndexed { index, point ->
            Point(index.toFloat(),Calculate.BMI(HeightInCM, (point.y).toDouble()).toFloat())
        }
        datehist = getYears(userHist)
    }
    if(dataPoints.size>1) {
        CustomGraph(datehist, dataPoints, "",selectedTimeRange)
    }
    else{
        val data = dataPoints.get(0)
        val date = datehist.get(0)
        GraphForSingleValue(label = date, value = data.y.toDouble() , unit = "")
    }

}

@Composable
fun BodyWaterPercentGraph(userHist:List<hist>,HeightInCM:Double,Age:Int,Gender:String="M",selectedTimeRange:Int) {
    var datapoints: List<Point> = emptyList()
    var datehist:List<String> = emptyList()

    if (Gender == "M") {
        if (selectedTimeRange == 0){
            datapoints = userHist.mapIndexed { index, hist ->
                val bwp = Calculate.BodyWaterPercent(
                    Calculate.BodyWaterForMale(
                        Age,
                        HeightInCM,
                        hist.weight
                    ), hist.weight
                )
                if(hist.impedance==0){
                    Point(index.toFloat(), 0f)
                }
                else if (bwp <= 100) {
                    Point(index.toFloat(), bwp.toFloat())
                } else {
                    Point(index.toFloat(), 100f)
                }
            }
            datehist =  userHist.map { convert4digYearToNoYear(it.date) }
    }
        else if(selectedTimeRange == 1){  //Month
            val dp = MonthWeightConverter(userHist)
            datapoints = dp.mapIndexed { index, point ->
                val bwp = Calculate.BodyWaterPercent(
                    Calculate.BodyWaterForMale(
                        Age,
                        HeightInCM,
                        point.y.toDouble()
                    ), point.y.toDouble()
                )
                if (bwp <= 100) {
                    Point(index.toFloat(), bwp.toFloat())
                } else {
                    Point(index.toFloat(), 100f)
                }
            }
            datehist = getMonthsAndYears(userHist)
        }
        else if(selectedTimeRange == 2){ //Year
            val dp = YearWeightConverter(userHist)
            datapoints = dp.mapIndexed { index, point ->
                val bwp = Calculate.BodyWaterPercent(
                    Calculate.BodyWaterForMale(
                        Age,
                        HeightInCM,
                        point.y.toDouble()
                    ), point.y.toDouble()
                )
                if (bwp <= 100) {
                    Point(index.toFloat(), bwp.toFloat())
                } else {
                    Point(index.toFloat(), 100f)
                }
            }
            datehist = getYears(userHist)

        }
}
    else { //Female
        if (selectedTimeRange == 0){
            datapoints = userHist.mapIndexed { index, hist ->
                val bwp = Calculate.BodyWaterPercent(
                    Calculate.BodyWaterForFemale(
                        Age,
                        HeightInCM,
                        hist.weight
                    ), hist.weight
                )
                if(hist.impedance==0){
                    Point(index.toFloat(), 0f)
                }
                else if (bwp <= 100) {
                    Point(index.toFloat(), bwp.toFloat())
                } else {
                    Point(index.toFloat(), 100f)
                }
            }
            datehist =  userHist.map { convert4digYearToNoYear(it.date) }
    }
        else if(selectedTimeRange == 1){ //Month
            val dp = MonthWeightConverter(userHist)
            datapoints = dp.mapIndexed { index, point ->
                val bwp = Calculate.BodyWaterPercent(
                    Calculate.BodyWaterForFemale(
                        Age,
                        HeightInCM,
                        point.y.toDouble()
                    ), point.y.toDouble()
                )
                if (bwp <= 100) {
                    Point(index.toFloat(), bwp.toFloat())
                } else {
                    Point(index.toFloat(), 100f)
                }
            }
            datehist = getMonthsAndYears(userHist)
        }
        else if(selectedTimeRange == 2){ //Year
            val dp = YearWeightConverter(userHist)
            datapoints = dp.mapIndexed { index, point ->
                val bwp = Calculate.BodyWaterPercent(
                    Calculate.BodyWaterForFemale(
                        Age,
                        HeightInCM,
                        point.y.toDouble()
                    ), point.y.toDouble()
                )
                if (bwp <= 100) {
                    Point(index.toFloat(), bwp.toFloat())
                } else {
                    Point(index.toFloat(), 100f)
                }
            }
            datehist = getYears(userHist)

        }
    }
    if(datapoints.size>1) {
        CustomGraph(datehist, datapoints, Unit = "%",selectedTimeRange)
    }
    else{
        val  data = datapoints.get(0)
        val date = datehist.get(0)

        GraphForSingleValue(label = date, value = if(userHist.size==1 && userHist.get(0).impedance == 0) 0.0 else data.y.toDouble(), unit = "%")
    }
}

@Composable
fun BodyFatPercentGraph(userHist:List<hist>,HeightInCM:Double,Age:Int,Gender:String="M",selectedTimeRange: Int){
    var datapoints:List<Point> = emptyList()
    var datehist:List<String> = emptyList()

    if(Gender=="M"){
        if(selectedTimeRange == 0) {
            datapoints = userHist.mapIndexed { index, hist ->
                val yValue =
                    if(hist.impedance!=0){
                    Calculate.BodyFatPercentforMale(Age, Calculate.BMI(HeightInCM, hist.weight))
                        .toFloat()}
                    else {0f}
                Point(index.toFloat(), if (yValue < 0) 0f else yValue)
            }
            datehist =  userHist.map { convert4digYearToNoYear(it.date) }
        }
        else if(selectedTimeRange == 1){  // Month
            val dp = MonthWeightConverter(userHist)
            datapoints = dp.mapIndexed { index, point ->
                val yValue =
                    Calculate.BodyFatPercentforMale(Age, Calculate.BMI(HeightInCM, (point.y).toDouble()))
                        .toFloat()
                Point(index.toFloat(), if (yValue < 0) 0f else yValue)
            }
            datehist = getMonthsAndYears(userHist)
        }
        else if(selectedTimeRange == 2){ //Year
            val dp = YearWeightConverter(userHist)
            datapoints = dp.mapIndexed { index, point ->
                val yValue =
                    Calculate.BodyFatPercentforMale(Age, Calculate.BMI(HeightInCM, (point.y).toDouble()))
                        .toFloat()
                Point(index.toFloat(), if (yValue < 0) 0f else yValue)
            }
            datehist = getYears(userHist)
        }
    }
    else { //Female
        if (selectedTimeRange == 0){
            datapoints = userHist.mapIndexed { index, hist ->
                val yValue =
                    Calculate.BodyFatPercentforFemale(Age, Calculate.BMI(HeightInCM, hist.weight))
                        .toFloat()
                Point(index.toFloat(), if (yValue < 0) 0f else yValue)
            }
            datehist =  userHist.map { convert4digYearToNoYear(it.date) }
    }
        else if(selectedTimeRange == 1){
            val dp = MonthWeightConverter(userHist)
            datapoints = dp.mapIndexed { index, point ->
                val yValue =
                    Calculate.BodyFatPercentforFemale(Age, Calculate.BMI(HeightInCM, (point.y).toDouble()))
                        .toFloat()
                Point(index.toFloat(), if (yValue < 0) 0f else yValue)
            }
            datehist = getMonthsAndYears(userHist)
        }
        else if(selectedTimeRange == 2){ //Year
            val dp = YearWeightConverter(userHist)
            datapoints = dp.mapIndexed { index, point ->
                val yValue =
                    Calculate.BodyFatPercentforFemale(Age, Calculate.BMI(HeightInCM, (point.y).toDouble()))
                        .toFloat()
                Point(index.toFloat(), if (yValue < 0) 0f else yValue)
            }
            datehist = getYears(userHist)
        }
    }
    if(datapoints.size>1) {
    CustomGraph(datehist, dataPoints = datapoints, Unit = "%",selectedTimeRange)
    }
    else{
        val  data = datapoints.get(0)
        val date = datehist.get(0)
        GraphForSingleValue(label = date, value = if(userHist.size==1 && userHist.get(0).impedance == 0) 0.0 else data.y.toDouble(), unit = "%")
    }

}

@Composable
fun SkeletalMuscleGraph(userHist:List<hist>,HeightInCM: Double,Age: Int,Gender:String="M",selectedTimeRange: Int) {
    var datapoints: List<Point> = emptyList()
    var datehist:List<String> = emptyList()

    if (Gender == "M") {
        if (selectedTimeRange == 0){
            datapoints = userHist.mapIndexed { index, hist ->
                if (hist.impedance != 0) {
                    Point(
                        index.toFloat(),
                        Calculate.SkeletalMusscleMassforMale(
                            hist.impedance.toDouble(),
                            HeightInCM,
                            Age
                        ).toFloat()
                    )
                } else {
                    Point(index.toFloat(), 0f)
                }
            }
            datehist =  userHist.map { convert4digYearToNoYear(it.date) }
    }
        else if(selectedTimeRange == 1){
                val dp = MonthImpedancetConverter(userHist)
                datapoints = dp.mapIndexed { index, point ->
                    if (point.y != 0f) {
                        Point(
                            index.toFloat(),
                            Calculate.SkeletalMusscleMassforMale(
                                point.y.toDouble(),
                                HeightInCM,
                                Age
                            ).toFloat()
                        )
                    } else {
                        Point(index.toFloat(), 0f)
                    }
                }
            datehist = getMonthsAndYears(userHist)
        }
        else if(selectedTimeRange == 2){
            val dp = YearImpedanceConverter(userHist)
            datapoints = dp.mapIndexed { index, point ->
                if (point.y != 0f) {
                    Point(
                        index.toFloat(),
                        Calculate.SkeletalMusscleMassforMale(
                            point.y.toDouble(),
                            HeightInCM,
                            Age
                        ).toFloat()
                    )
                } else {
                    Point(index.toFloat(), 0f)
                }
            }
            datehist = getYears(userHist)
        }
    } else {
        if (selectedTimeRange == 0){
            datapoints = userHist.mapIndexed { index, hist ->
                if (hist.impedance != 0) {
                    Point(
                        index.toFloat(),
                        Calculate.SkeletalMusscleMassforFemale(
                            hist.impedance.toDouble(),
                            HeightInCM,
                            Age
                        ).toFloat()
                    )
                } else {
                    Point(index.toFloat(), 0f)
                }
            }
            datehist =  userHist.map { convert4digYearToNoYear(it.date) }
    }
        else if(selectedTimeRange == 1){
            val dp = MonthImpedancetConverter(userHist)
            datapoints = dp.mapIndexed { index, point ->
                if (point.y != 0f) {
                    Point(
                        index.toFloat(),
                        Calculate.SkeletalMusscleMassforFemale(
                            point.y.toDouble(),
                            HeightInCM,
                            Age
                        ).toFloat()
                    )
                } else {
                    Point(index.toFloat(), 0f)
                }
            }
            datehist = getMonthsAndYears(userHist)
        }
        else if(selectedTimeRange == 2){
            val dp = YearImpedanceConverter(userHist)
            datapoints = dp.mapIndexed { index, point ->
                if (point.y != 0f) {
                    Point(
                        index.toFloat(),
                        Calculate.SkeletalMusscleMassforFemale(
                            point.y.toDouble(),
                            HeightInCM,
                            Age
                        ).toFloat()
                    )
                } else {
                    Point(index.toFloat(), 0f)
                }
            }
            datehist = getYears(userHist)
        }
    }
    if(datapoints.size>1) {
        CustomGraph(datehist, dataPoints = datapoints, Unit = "",selectedTimeRange)
    }
    else{
        val  data = datapoints.get(0)
        val date = datehist.get(0)
        GraphForSingleValue(label = date, value = data.y.toDouble(), unit = "")
    }

}

@Composable
fun LeanBodyMassGraph(userHist:List<hist>,Age:Int,HeightInCM: Double,Gender: String = "M",selectedTimeRange: Int) {
    var datapoints: List<Point> = emptyList()
    var datehist:List<String> = emptyList()

    if (Gender == "M") {
        if(selectedTimeRange == 0) {
            datapoints = userHist.mapIndexed { index, hist ->
                if(hist.impedance!=0) {
                    Point(
                        index.toFloat(),
                        Calculate.LeanBodyMass(
                            hist.weight,
                            Calculate.BodyFatPercentforMale(
                                Age,
                                Calculate.BMI(HeightInCM, hist.weight)
                            )
                        ).toFloat()
                    )
                }
                else{
                    Point(index.toFloat(),0f)
                }
            }
            datehist =  userHist.map { convert4digYearToNoYear(it.date) }
        }
        else if(selectedTimeRange == 1){
            val dp = MonthWeightConverter(userHist)
            datapoints = dp.mapIndexed { index, point ->
                Point(
                    index.toFloat(),
                    Calculate.LeanBodyMass(
                       ( point.y).toDouble(),
                        Calculate.BodyFatPercentforMale(Age, Calculate.BMI(HeightInCM, (point.y).toDouble()))
                    ).toFloat()
                )
            }
            datehist = getMonthsAndYears(userHist)
        }
        else if(selectedTimeRange == 2){
            val dp = YearWeightConverter(userHist)
            datapoints = dp.mapIndexed { index, point ->
                Point(
                    index.toFloat(),
                    Calculate.LeanBodyMass(
                        ( point.y).toDouble(),
                        Calculate.BodyFatPercentforMale(Age, Calculate.BMI(HeightInCM, (point.y).toDouble()))
                    ).toFloat()
                )
            }
            datehist = getYears(userHist)
        }
    }
    else{
        if(selectedTimeRange == 0) {
            datapoints = userHist.mapIndexed { index, hist ->
                if(hist.impedance!=0) {
                    Point(
                        index.toFloat(),
                        Calculate.LeanBodyMass(
                            hist.weight,
                            Calculate.BodyFatPercentforFemale(
                                Age,
                                Calculate.BMI(HeightInCM, hist.weight)
                            )
                        ).toFloat()
                    )
                }
                else{
                    Point(
                        index.toFloat(),
                       0f
                    )
                }
            }
            datehist =  userHist.map { convert4digYearToNoYear(it.date) }
        }
        else if(selectedTimeRange == 1){
            val dp = MonthWeightConverter(userHist)
            datapoints = dp.mapIndexed { index, point ->
                Point(
                    index.toFloat(),
                    Calculate.LeanBodyMass(
                        ( point.y).toDouble(),
                        Calculate.BodyFatPercentforFemale(Age, Calculate.BMI(HeightInCM, (point.y).toDouble()))
                    ).toFloat()
                )
            }
            datehist = getMonthsAndYears(userHist)
        }
        else if(selectedTimeRange == 2){
            val dp = YearWeightConverter(userHist)
            datapoints = dp.mapIndexed { index, point ->
                Point(
                    index.toFloat(),
                    Calculate.LeanBodyMass(
                        ( point.y).toDouble(),
                        Calculate.BodyFatPercentforFemale(Age, Calculate.BMI(HeightInCM, (point.y).toDouble()))
                    ).toFloat()
                )
            }
            datehist = getYears(userHist)
        }

    }
    if(datapoints.size>1) {
        CustomGraph(datehist, dataPoints = datapoints, Unit = "Kgs",selectedTimeRange)
    }
    else{
        val  data = datapoints.get(0)
        val date = datehist.get(0)
        GraphForSingleValue(label = date, value = if(userHist.size==1 && userHist.get(0).impedance == 0) 0.0 else data.y.toDouble(), unit = "Kgs")
    }
}

@Composable
fun BMRGraph(userHist:List<hist>,Age:Int,HeightInCM: Double,Gender: String = "M",selectedTimeRange: Int) {
    var datapoints: List<Point> = emptyList()
    var datehist:List<String> = emptyList()

    if (Gender == "M") {
        if(selectedTimeRange == 0) {
            datapoints = userHist.mapIndexed { index, hist ->
                if(hist.impedance!=0) {
                    Point(
                        index.toFloat(),
                        Calculate.BMRforMale(hist.weight, HeightInCM, Age).toFloat()
                    )
                }
                else{
                    Point(
                        index.toFloat(),
                        0f
                    )
                }
            }
            datehist =  userHist.map { convert4digYearToNoYear(it.date) }
        }
        else if(selectedTimeRange == 1){
            val dp = MonthWeightConverter(userHist)
            datapoints = dp.mapIndexed { index, point ->
                Point(index.toFloat(), Calculate.BMRforMale((point.y).toDouble(), HeightInCM, Age).toFloat())
            }
            datehist = getMonthsAndYears(userHist)
        }
        else if(selectedTimeRange == 2){
            val dp = YearWeightConverter(userHist)
            datapoints = dp.mapIndexed { index, point ->
                Point(index.toFloat(), Calculate.BMRforMale((point.y).toDouble(), HeightInCM, Age).toFloat())
            }
            datehist = getYears(userHist)

        }
    }
    else{
        if(selectedTimeRange == 0) {
            datapoints = userHist.mapIndexed { index, hist ->
                Point(
                    index.toFloat(),
                    Calculate.BMRforFemale(hist.weight, HeightInCM, Age).toFloat()
                )
            }
            datehist =  userHist.map { convert4digYearToNoYear(it.date) }

        }
        else if(selectedTimeRange == 1){
            val dp = MonthWeightConverter(userHist)
            datapoints = dp.mapIndexed { index, point ->
                Point(index.toFloat(), Calculate.BMRforFemale((point.y).toDouble(), HeightInCM, Age).toFloat())
            }
            datehist = getMonthsAndYears(userHist)
        }
        else if(selectedTimeRange == 2){
            val dp = YearWeightConverter(userHist)
            datapoints = dp.mapIndexed { index, point ->
                Point(index.toFloat(), Calculate.BMRforFemale((point.y).toDouble(), HeightInCM, Age).toFloat())
            }
            datehist = getYears(userHist)

        }

    }
    if(datapoints.size>1) {
        CustomGraph(datehist, dataPoints = datapoints, Unit = "Kcal",selectedTimeRange)
    }
    else{
        val  data = datapoints.get(0)
        val date = datehist.get(0)
        GraphForSingleValue(label = date, value = if(userHist.size==1 && userHist.get(0).impedance == 0) 0.0 else data.y.toDouble(), unit = "Kcal")
    }

}
