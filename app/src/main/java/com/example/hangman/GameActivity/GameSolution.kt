package com.example.hangman.GameActivity

import com.google.gson.annotations.SerializedName

data class GameSolution(@SerializedName("solution") val solution :String, @SerializedName("token") val token :String)
