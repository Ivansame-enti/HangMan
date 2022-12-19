package com.example.hangman.gameActivity

import com.google.gson.annotations.SerializedName

data class GameInfo(@SerializedName("token") val token :String, @SerializedName("hangman") var word :String, @SerializedName("language") val lan :String, @SerializedName("maxTries") val maxTries :Int){

}
