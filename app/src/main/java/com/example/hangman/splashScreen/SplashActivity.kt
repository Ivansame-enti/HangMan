package com.example.hangman.splashScreen

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hangman.LoginActivity
import com.example.hangman.MainActivity
import com.example.hangman.databinding.ActivityLoginBinding
import com.example.hangman.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashProgressBar.max=100
        val currentValue=100

        //ObjectAnimator.ofInt(binding.splashProgressBar, "progress", currentValue).setDuration(2000).start()
        var value=0
        Thread(Runnable {
            while (value<=100) {
                value+=5
                Thread.sleep(100)
                binding.splashProgressBar.progress = value
            }
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }).start()
    }
}