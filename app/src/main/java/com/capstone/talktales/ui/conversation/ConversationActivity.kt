package com.capstone.talktales.ui.conversation

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.talktales.R
import com.capstone.talktales.data.model.Conversation
import com.capstone.talktales.data.remote.response.ConversationResponse
import com.capstone.talktales.data.remote.response.ResponseResult
import com.capstone.talktales.databinding.ActivityConversationBinding
import com.capstone.talktales.factory.UserViewModelFactory

class ConversationActivity : AppCompatActivity() {

    private val storyId by lazy { intent.getStringExtra(EXTRA_STORY_ID).toString() }
    private val viewModel by viewModels<ConversationViewModel> {
        UserViewModelFactory.getInstance(
            this
        )
    }
    private val binding by lazy { ActivityConversationBinding.inflate(layoutInflater) }
    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(this, "Granted", Toast.LENGTH_LONG).show()
            viewModel.getConversation(storyId).observe(this) { res ->
                handleConversationResponse(res)
            }
        } else {
            // Todo
        }
    }

    private lateinit var conversation: List<List<Conversation>>

    private lateinit var sceneAdapter: RecyclerView.Adapter<*>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        requestPermission.launch(Manifest.permission.RECORD_AUDIO)


    }

    private fun handleConversationResponse(result: ResponseResult<ConversationResponse>) {
        when (result) {
            is ResponseResult.Error -> {
//                TODO()
            }

            is ResponseResult.Loading -> {
//                TODO()
            }

            is ResponseResult.Success -> {
//                TODO()
                conversation = result.data.data!!.conversations.chunked(2)
                sceneAdapter = object : FragmentStateAdapter(this) {
                    override fun getItemCount(): Int = conversation.size

                    override fun createFragment(position: Int): Fragment = SceneFragment.newInstance(conversation[position])

                }
                viewModel.setStoryLogId(result.data.data.storyLogId)
                showConversation()
            }
        }

    }

    private fun showConversation() {
        with(binding.viewPager) {
//            isUserInputEnabled = false
            adapter = sceneAdapter

        }
    }

    companion object {
        const val EXTRA_STORY_ID = "story-id"
    }
}