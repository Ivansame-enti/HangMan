package com.example.hangman.gameActivity.model

import com.google.gson.annotations.SerializedName

data class GameGuessLetter(@SerializedName("token") val token :String, @SerializedName("hangman") var word :String, @SerializedName("incorrectGuesses") val incorrectGuesses :Int, @SerializedName("correct") val correct :Boolean){

}
