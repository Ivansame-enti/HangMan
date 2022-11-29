package com.example.hangman.nasa

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hangman.databinding.ItemNasaImageBinding
import com.squareup.picasso.Picasso

class NasaReciclerViewAdapter: RecyclerView.Adapter<NasaReciclerViewAdapter.NasaVH>() {

    private var photoList: List<NasaImage>? = null

    inner class NasaVH(binding: ItemNasaImageBinding) : RecyclerView.ViewHolder(binding.root){
        val image = binding.nasaImage
        val title = binding.nasaTittle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NasaVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNasaImageBinding.inflate(inflater, parent, false)
        return NasaVH(binding)
    }

    override fun onBindViewHolder(holder: NasaVH, position: Int) {
        val image = photoList?.get(position)
        holder.title.text = image?.title ?: ""
        Picasso.get().load(image?.link).into(holder.image)
        //holder.image
    }

    override fun getItemCount(): Int {
        return photoList?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<NasaImage>){
        photoList = newList
        notifyDataSetChanged()
    }
}