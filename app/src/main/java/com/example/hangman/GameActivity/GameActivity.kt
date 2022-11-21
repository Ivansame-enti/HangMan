package com.example.hangman.GameActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hangman.databinding.ActivityGameBinding
import com.example.hangman.databinding.ActivitySettingsBinding


class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}