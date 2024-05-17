package com.capstone.talktales.ui.home

import androidx.lifecycle.ViewModel
import com.capstone.talktales.data.MainRepository

class HomeViewModel(private val mainRepository: MainRepository): ViewModel() {

    fun getStories() = mainRepository.getStories()
}