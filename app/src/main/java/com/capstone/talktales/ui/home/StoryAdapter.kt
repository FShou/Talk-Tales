package com.capstone.talktales.ui.home

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.capstone.talktales.data.model.StoryItem
import com.capstone.talktales.databinding.StoryItemLayoutBinding
import com.capstone.talktales.ui.storydetail.StoryDetailActivity

class StoryAdapter(private val stories: List<StoryItem>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {
    class ViewHolder(private val binding: StoryItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: StoryItem) {
            val imgUri = Uri.parse(data.imgUrl)
            binding.apply {
                root.setOnClickListener {
                    root.context.startActivity(
                        Intent(binding.root.context, StoryDetailActivity::class.java)
                            .putExtra(StoryDetailActivity.EXTRA_STORY_ID,data.id)
                    )
                }

                storyBanner.load(imgUri) {
                    transformations(
                        RoundedCornersTransformation(16f)
                    )
                }

                storyTitle.text = data.title
                storyCity.text = data.city

            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            StoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = stories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stories[position])
    }

}