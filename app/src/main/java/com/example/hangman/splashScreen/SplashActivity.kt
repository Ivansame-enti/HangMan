package com.example.hangman.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.hangman.LoginActivity
import com.example.hangman.LoginRegister.PlayActivity
import com.example.hangman.RewardActivity
import com.example.hangman.databinding.ActivitySplashBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashProgressBar.max=100

        fireBaseAuth = FirebaseAuth.getInstance()

        var value=0
        Thread(Runnable {
            while (value<=100) {
                value+=5
                Thread.sleep(100)
                binding.splashProgressBar.progress = value
            }

            if (fireBaseAuth.currentUser != null) { //Si ya hay un usuario logueado, cambiamos a la otra actividad
                val intent = Intent(this@SplashActivity, PlayActivity::class.java)
                startActivity(intent)
                finish()
            } else { //Sino vamos al login
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }).start()
    }

}