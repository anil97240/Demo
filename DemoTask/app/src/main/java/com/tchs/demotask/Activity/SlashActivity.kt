package com.tchs.demotask.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.tchs.demotask.R

class SlashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slash)
        Handler().postDelayed({
            if (!isFinishing) {
                val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
            }
        }, 4000)
    }
}