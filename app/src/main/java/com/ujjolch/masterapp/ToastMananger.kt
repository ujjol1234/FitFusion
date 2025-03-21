package com.ujjolch.masterapp

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

object ToastManager {
    private var toastShown = false

    fun showToast(context: Context, message: String,Length:Int) {
        if (!toastShown) {
            toastShown = true
            Toast.makeText(context, message, Length).show()
            // Reset the flag after a short delay to ensure the toast has time to be dismissed
            Handler(Looper.getMainLooper()).postDelayed({
                toastShown = false
            }, 2000) // Adjust delay as necessary
        }
    }
}
