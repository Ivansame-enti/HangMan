package com.example.hangman.scores

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hangman.LoginActivity
import com.example.hangman.databinding.ActivityScoreBinding
import com.example.hangman.gameActivity.viewModel.GameViewModel
import com.example.hangman.scores.adapter.ScoreAdapter


class ScoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScoreBinding
    private lateinit var viewModel: GameViewModel
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: ScoreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycledView()
    }


    private fun initRecycledView(){

        // binding.RecycledScore.layoutManager  = LinearLayoutManager(this)

        recyclerView= binding.RecycledScore
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ScoreAdapter()
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)


        binding.goBackButton.setOnClickListener{
            val intent = Intent(this@ScoreActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

}