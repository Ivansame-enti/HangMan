package com.example.hangman.LoginRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hangman.LoginActivity
import com.example.hangman.databinding.ActivityPlayBinding
import com.example.hangman.gameActivity.GameActivity
import com.google.firebase.auth.FirebaseAuth

class PlayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayBinding
    private lateinit var fireBaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fireBaseAuth = FirebaseAuth.getInstance()

        val email: String = fireBaseAuth.getCurrentUser()?.email ?: "Anonymous"

        if(email.isEmpty()) binding.emailLoginTextView.text = "Anonymous"
        else binding.emailLoginTextView.text = email

        binding.playButton.setOnClickListener{
            val intent = Intent(this@PlayActivity, GameActivity::class.java)
            startActivity(intent)
        }

        binding.logOutButtonPlay.setOnClickListener{
            fireBaseAuth.signOut()
            val intent = Intent(this@PlayActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}