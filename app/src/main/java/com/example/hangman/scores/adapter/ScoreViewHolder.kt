package com.example.hangman.scores.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.hangman.databinding.ItemScorelistBinding
import com.example.hangman.gameActivity.User

class ScoreViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemScorelistBinding.bind(view)

    //  val ScoreListName = view.findViewById<TextView>(R.id.ScoreListName)
    //  val ScoreListScore = view.findViewById<TextView>(R.id.ScoreListScore)


    fun render(scoreListModel: User){
        binding.ScoreListName.text = scoreListModel.Name
        binding.ScoreListScore.text = scoreListModel.Score.toString()
    }
}