package com.example.hangman.winLose

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hangman.databinding.ActivityLoseBinding

class LoseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoseBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}