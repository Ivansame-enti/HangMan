package com.example.hangman.GameActivity

import com.google.gson.annotations.SerializedName

data class GameInfo(@SerializedName("hangman") var word :String, @SerializedName("token") val token :String){

}
