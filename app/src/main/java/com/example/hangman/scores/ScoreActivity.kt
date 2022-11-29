package com.example.hangman.scores

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hangman.LoginActivity
import com.example.hangman.databinding.ActivityMainBinding
import com.example.hangman.databinding.ActivityScoreBinding
import com.example.hangman.scores.adapter.ScoreAdapter

class ScoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycledView()
    }


    private fun initRecycledView(){

        binding.RecycledScore.layoutManager  = LinearLayoutManager(this)
        binding.RecycledScore.adapter = ScoreAdapter(ScoreProvider.scoreListDef)

        binding.goBackButton.setOnClickListener{
            val intent = Intent(this@ScoreActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

}