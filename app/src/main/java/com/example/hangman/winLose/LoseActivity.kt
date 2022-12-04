package com.example.hangman.winLose

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hangman.databinding.ActivityLoseBinding
import com.example.hangman.gameActivity.GameActivity
import com.example.hangman.scores.ScoreActivity

class LoseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loseExitButton.setOnClickListener {
            val intent = Intent(this@LoseActivity, ScoreActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.loseRestartButton.setOnClickListener {
            val intent = Intent(this@LoseActivity, GameActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}