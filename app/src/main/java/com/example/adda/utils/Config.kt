package com.example.adda.utils

import android.content.Context
import android.widget.Toast

class Config {
    companion object {
        val USER_KEY = "addaUser"
        val ACTIVE_KEY = "activeStatus"

        fun showToast(context: Context, message: String?) {
            Toast.makeText(context, message , Toast.LENGTH_LONG).show()
        }
    }


}