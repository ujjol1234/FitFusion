package com.ujjolch.masterapp

sealed class Result<out T>{   //Here out T means basically that it can be any data type like string oe int
    data class Success<out T>(val data: T): Result<T>()
    data class Error(val exception: Exception): Result<Nothing>()
}