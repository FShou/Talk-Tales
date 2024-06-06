package com.capstone.talktales.ui.storydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.talktales.data.repo.UserRepository

class StoryDetailViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _pageTitle = MutableLiveData<String>()
    val pageTitle: LiveData<String> get() = _pageTitle


    fun setPageTitle(title: String) {
        _pageTitle.value = title
    }
    fun getStoryDetail(id: String) = userRepository.getStoryDetail(id)

}