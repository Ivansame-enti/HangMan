package com.example.hangman.scores.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hangman.R
import com.example.hangman.gameActivity.User

class ScoreAdapter : RecyclerView.Adapter<ScoreViewHolder>() {

    private val userList = ArrayList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_scorelist,
            parent, false
        )
        return ScoreViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {

        val item = userList[position]
        holder.render(item)

    }

    override fun getItemCount(): Int {
        return userList.size
    }

        fun updateUserList(userList : List<User>){
            this.userList.clear()
            this.userList.addAll(userList)
            notifyDataSetChanged()
    }

}