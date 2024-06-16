package com.capstone.talktales.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import androidx.recyclerview.widget.RecyclerView
import coil.transform.RoundedCornersTransformation
import com.capstone.talktales.data.model.StoryItem
import com.capstone.talktales.databinding.ImageItemBinding
import com.capstone.talktales.ui.storydetail.StoryDetailActivity
import com.capstone.talktales.ui.tutorial.TutorialActivity

class CarouselAdapter(private val items: List<Any>) :
    RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(carouselContent: Any) {
            when (carouselContent) {
                is StoryItem -> {
                    binding.imgItem.load(carouselContent.imgUrl) {
                        transformations(RoundedCornersTransformation(16f))
                    }
                    binding.root.setOnClickListener {
                        binding.root.context.startActivity(
                            Intent(binding.root.context, StoryDetailActivity::class.java)
                                .putExtra(StoryDetailActivity.EXTRA_STORY_ID, carouselContent.id)
                        )
                    }
                }

                is String -> {
                    binding.imgItem.load(carouselContent) {
                        transformations(RoundedCornersTransformation(16f))
                    }

                    binding.root.setOnClickListener {
                        binding.root.context.startActivity(
                            Intent(binding.root.context, TutorialActivity::class.java)
                        )
                    }
                }
            }

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