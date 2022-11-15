package com.example.hangman.splashScreen

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        ObjectAnimator.ofInt(binding.splashProgressBar, "progress", currentValue).setDuration(2000).start()
    }
}