package com.example.adda.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.adda.R
import com.example.adda.utils.SessionManager

class SplashActivity : AppCompatActivity() {

    var sessionManager: SessionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sessionManager = SessionManager(this)

        Handler().postDelayed({
            if (sessionManager!!.isLogedIn) {
                val i = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(i)
            } else {
                val i = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(i)
            }
            finish()
        }, 1500)

    }
}