package com.example.hangman.scores.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.hangman.databinding.ItemScorelistBinding
import com.example.hangman.scores.ScoreList

class ScoreViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemScorelistBinding.bind(view)

    //  val ScoreListName = view.findViewById<TextView>(R.id.ScoreListName)
    //  val ScoreListScore = view.findViewById<TextView>(R.id.ScoreListScore)


    fun render(scoreListModel: ScoreList){
        binding.ScoreListName.text = scoreListModel.name
        binding.ScoreListScore.text = scoreListModel.score.toString()
    }
}