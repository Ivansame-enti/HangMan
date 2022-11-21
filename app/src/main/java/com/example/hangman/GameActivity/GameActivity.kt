package com.example.hangman.GameActivity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.hangman.databinding.ActivityGameBinding
import com.example.hangman.databinding.ActivitySettingsBinding


class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var gameIntent = 0;
        var volume = true;
        var vibration = true;
        var notification = true;
        var advertising = true;


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

    }
}