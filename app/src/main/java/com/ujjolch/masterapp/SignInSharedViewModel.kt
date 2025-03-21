package com.ujjolch.masterapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignInSharedViewModel:ViewModel() {
    val _Fname: MutableLiveData<String> = MutableLiveData("")
    val Fname: LiveData<String> get() = _Fname

    val _Lname: MutableLiveData<String> = MutableLiveData("")
    val Lname: LiveData<String> get() = _Lname

    val _SignInEmail: MutableLiveData<String> = MutableLiveData("")
    val SignInEmail: LiveData<String> get() = _SignInEmail

    val _Password: MutableLiveData<String> = MutableLiveData("")
    val Password: LiveData<String> get() = _Password

    val _ConfirmPassword: MutableLiveData<String> = MutableLiveData("")
    val ConfirmPassword: LiveData<String> get() = _ConfirmPassword

    fun set(fn:String,ln:String,email:String,password:String,cpassword:String){
        _Fname.value = fn
        _Lname.value = ln
        _SignInEmail.value = email
        _Password.value = password
        _ConfirmPassword.value = cpassword
    }

    fun delete(){
        _Fname.value = ""
        _Lname.value = ""
        _SignInEmail.value =""
        _Password.value = ""
        _ConfirmPassword.value = ""
    }
}