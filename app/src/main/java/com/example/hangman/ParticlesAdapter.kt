package com.example.hangman

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hangman.databinding.ItemParticleBinding

class ParticlesAdapter(private val particles: List<String>) :
    RecyclerView.Adapter<ParticlesAdapter.ParticlesViewHolder>() {

    inner class ParticlesViewHolder(binding: ItemParticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val name = binding.ParticleName
        val image = binding.ParticleImage

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticlesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemParticleBinding.inflate(layoutInflater)
        return ParticlesViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ParticlesViewHolder, position: Int) {

        val particle = particles[position]
        holder.name.text = particle

    }

    override fun getItemCount(): Int {
        return particles.size
    }

}