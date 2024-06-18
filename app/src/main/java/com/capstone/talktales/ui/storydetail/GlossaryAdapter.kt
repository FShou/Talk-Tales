package com.capstone.talktales.ui.storydetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.talktales.data.model.Glossary
import com.capstone.talktales.databinding.GlossaryItemLayoutBinding

class GlossaryAdapter(private val data: List<Glossary>) :
    RecyclerView.Adapter<GlossaryAdapter.ViewHolder>() {
    class ViewHolder(private val binding: GlossaryItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(glossary: Glossary) {
            val (word,explanation) = glossary
            with(binding) {
                tvWord.text = word
                tvExplanation.text = explanation
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GlossaryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

}