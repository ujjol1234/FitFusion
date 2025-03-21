package com.ujjolch.masterapp

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.extensions.isNotNull
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val userRepository: UserRepository
    init {
        userRepository= UserRepository(
            FirebaseAuth.getInstance(),
            Injection.instance()
        )
    }
    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> get() = _authResult

    private val _changePasswordResult = MutableLiveData<Result<Boolean>>()
    val changePasswordResult: LiveData<Result<Boolean>> get() = _changePasswordResult

    val isLoading: StateFlow<Boolean> = userRepository.isLoading.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )


    val isLoggedIn: StateFlow<Boolean> = userRepository.isLoggedIn.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    private val _isVerified = MutableLiveData<Result<Boolean>>()
    val isVerified: LiveData<Result<Boolean>> get() = _isVerified

    fun signUp(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            _authResult.value = userRepository.SignUp(firstName,lastName,password,email)
        }
    }
    fun LogIn(email: String,password:String){
        viewModelScope.launch {
            _authResult.value = userRepository.LogIn(email,password)
        }
    }
    fun LogOut(){
           _authResult.value = userRepository.LogOut()
    }
    fun resetAuthResult() {
        _authResult.value = null
    }
    fun changePassword(oldPassword:String,newPassword:String){
        viewModelScope.launch {
            _changePasswordResult.value = userRepository.changePassword(oldPassword,newPassword)
        }
    }
    fun resetChangePasswordResult() {
        _changePasswordResult.value = null
    }
    fun sendVerificationEmail(){
        viewModelScope.launch {
            userRepository.sendVerificationEmail()
        }
    }
    suspend fun checkIfVerified(email:String){
        viewModelScope.launch {
            _isVerified.value = userRepository.isEmailVerified(email)

        }
    }
    fun isUserVerified(): Boolean {   //Much faster method for checking if user is verified or not
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.isEmailVerified ?: false
    }

    // Function to reset isVerified
    fun resetIsVerified() {
        _isVerified.value = null
    }

    fun resetPassword(   //For sending email on forget password
        email: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    task.exception?.message?.let { onFailure(it) }
                }
            }
    }
}