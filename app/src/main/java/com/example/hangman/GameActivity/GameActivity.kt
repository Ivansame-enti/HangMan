package com.example.hangman.GameActivity

import android.content.Context
import android.graphics.Color.RED
import android.hardware.camera2.params.RggbChannelVector.RED
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import com.example.hangman.databinding.ActivityGameBinding
import com.example.hangman.databinding.ActivitySettingsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private val gameManager = GameManager()

    private lateinit var wordTextView: TextView
    private lateinit var lettersUsedTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var gameLostTextView: TextView
    private lateinit var gameWonTextView: TextView
    private lateinit var newGameButton: Button
    private lateinit var lettersLayout: ConstraintLayout

    private var gameToken: String = ""
    private var gameWord: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        gameManager.startNewGame()
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.head.isVisible = false
        binding.body.isVisible = false
        binding.rightArm.isVisible = false
        binding.leftArm.isVisible = false
        binding.rightLeg.isVisible = false
        binding.leftLeg.isVisible = false


        wordTextView = binding.wordTextView
        lettersLayout = binding.lettersLayout

        var gameIntent = 0;
        var volume = true;
        var vibration = true;
        var notification = true;
        var advertising = true;
        val gameState = gameManager.startNewGame()

        updateUI(gameState)

        lettersLayout.children.forEach { letterView ->
            if (letterView is TextView) {
                letterView.setOnClickListener {

                    //letterView.background =
                    //val gameState = gameManager.play((letterView).text[0])
                    //updateUI(gameState)
                    guessLetter((letterView).text[0])
                    gameIntent++
                    //letterView.setTextColor(255,34,34);
                    //letterView.setTextColor(255,0,0);
                    // letterView.visibility = View.GONE //CUANDO DAS UNA LETRA, PONER QUE SE PONGA ROJO

                    // letterView.setBackgroundColor(255);
                }
            }
        }




        if(gameIntent == 1)
            binding.head.isVisible = true;

        if(gameIntent == 2)
            binding.body.isVisible = true;

        if(gameIntent == 3)
            binding.rightArm.isVisible = true

        if(gameIntent == 4)
            binding.leftArm.isVisible = true

        if(gameIntent == 5)
            binding.rightLeg.isVisible = true

        if(gameIntent == 6){
            binding.leftLeg.isVisible = true;
            Toast.makeText(this, "Moriste", Toast.LENGTH_SHORT).show()
        }

        fun loadData(){
            val sharedPref = getSharedPreferences("prefs", Context.MODE_PRIVATE)
            volume = sharedPref.getBoolean("volume",true)
            vibration = sharedPref.getBoolean("vibration",true)
            notification = sharedPref.getBoolean("notification",true)
            advertising = sharedPref.getBoolean("advertising",true)
        }
        loadData()

        startGame()
    }

    private fun updateUI(gameState: Any) {
        when (gameState) {
            is GameState.Lost -> showGameLost(gameState.wordToGuess)
            is GameState.Running -> {
                wordTextView.text = gameState.underscoreWord
                //lettersUsedTextView.text = "Letters used: ${gameState.lettersUsed}"
                //imageView.setImageDrawable(ContextCompat.getDrawable(this, gameState.drawable))
            }
            is GameState.Won -> showGameWon(gameState.wordToGuess)
        }
    }

    private fun startGame(){
        val outside = Retrofit.Builder().baseUrl("https://hangman-api.herokuapp.com/").addConverterFactory(
            GsonConverterFactory.create()).build()

        val services = outside.create(ApiHangman::class.java)

        services.createGame().enqueue(object : Callback<GameInfo> {
            override fun onResponse(call: Call<GameInfo>, response: Response<GameInfo>) {
                gameWord = response.body()?.word ?: ""
                Toast.makeText(this@GameActivity, gameWord, Toast.LENGTH_SHORT).show()
                gameToken = response.body()?.token ?: ""
            }

            override fun onFailure(call: Call<GameInfo>, t: Throwable) {
                Toast.makeText(this@GameActivity, "Error on API", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun guessLetter(letter : Char)
    {
        val outside = Retrofit.Builder().baseUrl("https://hangman-api.herokuapp.com/").addConverterFactory(
            GsonConverterFactory.create()).build()

        val services = outside.create(ApiHangman::class.java)

        services.guessLetter(gameToken, letter.toString()).enqueue(object : Callback<GameGuessLetter>{
            override fun onResponse(call: Call<GameGuessLetter>,response: Response<GameGuessLetter>)
            {
                val letterInWord : Boolean = response.body()?.correct ?: false
                gameWord = response.body()?.word ?: "";
                gameToken = response.body()?.token ?: ""
                Toast.makeText(this@GameActivity, gameWord, Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<GameGuessLetter>, t: Throwable)
            {
                Toast.makeText(this@GameActivity, "Error on API", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun showGameWon(wordToGuess: String) {

    }

    private fun showGameLost(wordToGuess: String) {

    }
}