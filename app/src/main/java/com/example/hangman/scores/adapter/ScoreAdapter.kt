package com.example.hangman.scores.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hangman.R
import com.example.hangman.scores.ScoreList

class ScoreAdapter(private val ScoreList:List<ScoreList>) : RecyclerView.Adapter<ScoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {

        var layoutInflater = LayoutInflater.from(parent.context)
        return ScoreViewHolder(layoutInflater.inflate(R.layout.item_scorelist, parent,false))

    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {

        val item = ScoreList[position]
        holder.render(item)

    }

    override fun getItemCount(): Int = ScoreList.size


}