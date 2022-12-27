package com.example.hangman.gameActivity.model

import com.google.gson.annotations.SerializedName

data class GameSolution(@SerializedName("language") val language :String, @SerializedName("maxTries") val maxTries :Int, @SerializedName("token") val token :String, @SerializedName("solution") val solution :String, @SerializedName("hangman") val hangman :String, @SerializedName("status") val status :String, @SerializedName("incorrectGuesses") val incorrectGuesses :Int)
