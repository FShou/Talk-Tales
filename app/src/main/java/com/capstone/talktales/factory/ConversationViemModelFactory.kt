package com.capstone.talktales.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.talktales.data.repo.ModelRepository
import com.capstone.talktales.data.repo.UserRepository
import com.capstone.talktales.di.Injection
import com.capstone.talktales.ui.conversation.ConversationViewModel


class ConversationViemModelFactory private constructor(
    private val userRepository: UserRepository, private val modelRepository: ModelRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(ConversationViewModel::class.java) -> return ConversationViewModel(
                userRepository, modelRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ConversationViemModelFactory? = null

        fun getInstance(context: Context): ConversationViemModelFactory =
            instance ?: synchronized(this) {
                instance ?: ConversationViemModelFactory(
                    Injection.provideUserRepository(context),
                    Injection.provideModelRepository()
                )
            }.also { instance = it }
    }
}