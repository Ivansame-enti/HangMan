package com.example.hangman.scores

class ScoreProvider {

    companion object{

        val scoreListDef = listOf<ScoreList>( //CREO QUE YA DETECTA QUE ES DE CLASE SCORELIST SIN PONERSELO
            ScoreList(
                "ivansales@gmail.com",
                1000
            ),
            ScoreList(
                "josepromera@gmail.com",
                500
            ),
            ScoreList(
                "pabloperpinan@gmail.com",
                300
            )
        )
    }
}