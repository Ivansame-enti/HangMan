package com.example.hangman.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hangman.R
import com.example.hangman.databinding.ActivityFragmentBinding
import com.example.hangman.databinding.ActivityMainBinding

class FragmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val colorsRound = FirstRoundFragment()
        val numbersRound = FirstRoundFragment()

        changeFragment(colorsRound)

        binding.bottomNavigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.colorsButton -> changeFragment(colorsRound)
                R.id.numbersButton -> changeFragment(numbersRound)
                R.id.quotesButton -> changeFragment(numbersRound)
            }
            true
        }
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            supportFragmentManager.fragments.forEach {
                hide(it)
            }
            if (!fragment.isAdded) add(binding.fragmentContainer.id, fragment)
            else show(fragment)
            commit()
        }
    }
}