package com.example.hangman.winLose

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hangman.LoginRegister.PlayActivity
import com.example.hangman.databinding.ActivityWinBinding
import com.example.hangman.gameActivity.GameActivity
import com.example.hangman.scores.ScoreActivity

class WinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val score = intent.getIntExtra("score", 0)

        binding.winNumberTextView.text = score.toString()

        binding.winExitButton.setOnClickListener {
            val intent = Intent(this@WinActivity, ScoreActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.winRestartButton.setOnClickListener {
            val intent = Intent(this@WinActivity, GameActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}