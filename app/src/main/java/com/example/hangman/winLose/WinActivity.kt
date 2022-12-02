package com.example.hangman.winLose

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hangman.databinding.ActivityWinBinding

class WinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWinBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}