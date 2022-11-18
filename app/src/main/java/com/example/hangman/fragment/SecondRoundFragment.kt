package com.example.hangman.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hangman.databinding.ActivityFragmentBinding
import com.example.hangman.databinding.FragmentFirstRoundBinding
import com.example.hangman.databinding.FragmentSecondRoundBinding

class SecondRoundFragment: Fragment() {
    private lateinit var binding: FragmentSecondRoundBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondRoundBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun CheckRound(): Boolean{
        return binding.correct.isChecked
    }
}