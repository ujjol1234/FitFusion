package com.ujjolch.masterapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogInSharedViewModel:ViewModel(){
    val _LogInEmail:MutableLiveData<String> = MutableLiveData("")
    val LogInEmail: LiveData<String> get() = _LogInEmail

    val _Password:MutableLiveData<String> = MutableLiveData("")
    val Password:LiveData<String> get() = _Password

    fun set(email:String,password:String){
        _LogInEmail.value = email
        _Password.value = password
    }
    fun delete(){
        _LogInEmail.value = ""
        _Password.value = ""
    }
}