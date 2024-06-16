package com.capstone.talktales.ui.tutorial

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import androidx.recyclerview.widget.RecyclerView
import com.capstone.talktales.databinding.ImageItemBinding

class TutorialPageAdapter(private val items: List<String>) :
    RecyclerView.Adapter<TutorialPageAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tutorialPage: String) {
            binding.imgItem.load(tutorialPage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}