package com.example.hangman.scores

class ScoreProvider {

    companion object{

        val scoreListDef = listOf<ScoreList>( //CREO QUE YA DETECTA QUE ES DE CLASE SCORELIST SIN PONERSELO
            ScoreList(
                "Pablo",
                1000
            ),
            ScoreList(
                "Josep",
                -10
            ),
            ScoreList(
                "Ivan",
                1
            ),
            ScoreList(
                "Willirex",
                0
            ),
            ScoreList(
                "Narnia",
                -3
            )

        )
    }
}