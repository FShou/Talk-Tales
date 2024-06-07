package com.capstone.talktales.ui.storydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.talktales.data.model.Story
import com.capstone.talktales.data.repo.UserRepository

class StoryDetailViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _pageTitle = MutableLiveData<String>()
    val pageTitle: LiveData<String> get() = _pageTitle

    private val _story = MutableLiveData<Story>()
    val story: LiveData<Story> get() = _story


    fun setPageTitle(title: String) {
        _pageTitle.value = title
    }




    fun setStory(story: Story) {
        _story.value = story
    }
    fun getStoryDetail(id: String) = userRepository.getStoryDetail(id)

}