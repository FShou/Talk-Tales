package com.capstone.talktales.ui.home

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.helper.widget.Carousel
import coil.load

import androidx.recyclerview.widget.RecyclerView
import coil.transform.RoundedCornersTransformation
import com.capstone.talktales.databinding.CarouselItemBinding
import java.net.URI

class CarouselAdapter(private val items: List<Uri>) :
    RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {
    class ViewHolder(private val binding: CarouselItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uri: Uri) {
            binding.carouselImageView.load(uri) {
                crossfade(true)
                transformations(RoundedCornersTransformation(20f))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CarouselItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}