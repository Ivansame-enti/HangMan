package com.example.hangman.GameActivity

import com.google.gson.annotations.SerializedName

data class GameGuessLetter(@SerializedName("hangman") var word :String, @SerializedName("token") val token :String, @SerializedName("correct") val correct :Boolean)
