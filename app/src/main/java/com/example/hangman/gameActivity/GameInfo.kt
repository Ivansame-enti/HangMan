package com.example.hangman.gameActivity

import com.google.gson.annotations.SerializedName

data class GameInfo(@SerializedName("hangman") var word :String, @SerializedName("token") val token :String){

}
