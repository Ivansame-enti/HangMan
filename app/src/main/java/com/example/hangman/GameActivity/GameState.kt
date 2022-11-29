package com.example.hangman.GameActivity

sealed class GameState {
    class Running(
        val lettersUsed: String,
        val underscoreWord: String
    ) : GameState()
    class Lost(val wordToGuess: String) : GameState()
    class Won(val wordToGuess: String) : GameState()
}