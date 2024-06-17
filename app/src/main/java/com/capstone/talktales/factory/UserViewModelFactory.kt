package com.capstone.talktales.factory

import com.capstone.talktales.di.Injection


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.talktales.data.repo.UserRepository
import com.capstone.talktales.ui.conversation.ConversationViewModel
import com.capstone.talktales.ui.home.HomeViewModel
import com.capstone.talktales.ui.storydetail.StoryDetailViewModel
import com.capstone.talktales.ui.userdetail.UserDetailActivity
import com.capstone.talktales.ui.userdetail.UserDetailViewModel


class UserViewModelFactory private constructor(
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> return HomeViewModel(
                userRepository
            ) as T

            modelClass.isAssignableFrom(UserDetailViewModel::class.java) -> return UserDetailViewModel(
                userRepository
            ) as T

            modelClass.isAssignableFrom(StoryDetailViewModel::class.java) -> return StoryDetailViewModel(
                userRepository
            ) as T


        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        private var instance: UserViewModelFactory? = null
        fun getInstance(context: Context): UserViewModelFactory =
            instance ?: UserViewModelFactory(Injection.provideUserRepository(context)).also {
                instance = it
            }

        fun destroy() {
            instance = null
        }
    }
}