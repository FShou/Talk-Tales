package com.capstone.talktales.ui.conversation

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.talktales.R
import com.capstone.talktales.data.model.Conversation
import com.capstone.talktales.data.model.Scene
import com.capstone.talktales.data.remote.response.ConversationResponse
import com.capstone.talktales.data.remote.response.PredictionData
import com.capstone.talktales.data.remote.response.ResponseResult
import com.capstone.talktales.databinding.ActivityConversationBinding
import com.capstone.talktales.factory.UserViewModelFactory
import com.capstone.talktales.ui.utils.setCurrentItemWithSmoothScroll
import com.capstone.talktales.ui.utils.startShimmer
import com.google.android.material.bottomsheet.BottomSheetBehavior

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

    private lateinit var conversation: List<Conversation>

    private lateinit var sceneAdapter: RecyclerView.Adapter<*>

    private val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(binding.bottomSheet)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        requestPermission.launch(Manifest.permission.RECORD_AUDIO)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        viewModel.feedback.observe(this) { handleFeedback(it) }
        viewModel.page.observe(this) { binding.viewPager.setCurrentItemWithSmoothScroll(it, 100) }

    }

    private fun handleFeedback(predictionData: PredictionData?) {
        when {
            predictionData == null -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                return
            }

            predictionData.feedback == "Correct" -> {
                binding.btnFeedbackAction.setOnClickListener {
                    viewModel.nextPage()
                    viewModel.setFeedback(null)
                }
                binding.btnFeedbackAction.text = resources.getString(R.string.next)
                binding.tvFeedback.setTextColor(
                    ContextCompat.getColor(
                        this@ConversationActivity,
                        R.color.green
                    )
                )
            }

            predictionData.feedback == "Incorrect" -> {
                binding.btnFeedbackAction.setOnClickListener {
                    viewModel.setFeedback(null)
                }
                binding.btnFeedbackAction.text = resources.getString(R.string.retry)
                binding.tvFeedback.setTextColor(
                    ContextCompat.getColor(
                        this@ConversationActivity,
                        R.color.red
                    )
                )
            }
        }
        binding.apply {
            tvFeedback.text = predictionData!!.feedback
            tvTarget.text = predictionData.target
        }

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun handleConversationResponse(result: ResponseResult<ConversationResponse>) {
        when (result) {
            is ResponseResult.Error -> {
                showLoading(false)
            }

            is ResponseResult.Loading -> {
                showLoading(true)
            }

            is ResponseResult.Success -> {
                showLoading(false)
                conversation = result.data.data!!.conversations
                viewModel.setStoryLogId(result.data.data.storyLogId)
                createScenes()
                showConversation()
            }
        }

    }

    private fun createScenes() {
        val midConversation = conversation.find { it.isMid }
        val scenes = mutableListOf<Scene>()
        if (midConversation == null) {
            val chunkedConversation = conversation.chunked(2)

            chunkedConversation.forEach { scenes.add(Scene(data = it, isMid = false)) }
        } else {

            val firstHalfScenes = conversation.takeWhile { !it.isMid }.chunked(2)
            val secondHalfScenes = conversation.takeLastWhile { !it.isMid }.chunked(2)
            firstHalfScenes.forEach { scenes.add(Scene(isMid = false, data = it)) }
            scenes.add(Scene(isMid = true, data = listOf(midConversation)))
            secondHalfScenes.forEach { scenes.add(Scene(isMid = false, data = it)) }
        }
        sceneAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = scenes.size + 1

            override fun createFragment(position: Int): Fragment {
                if (itemCount-1 == position) {
                    return FinishSceneFragment.newInstance()
                }
                val scene = scenes[position]
                return if (scene.isMid) MidSceneFragment.newInstance(scene.data[0])
                else ConversationSceneFragment.newInstance(scene.data)
            }

        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingSkeleton.visibility = View.VISIBLE
            binding.loadingSkeleton.children.forEach {
                it.startShimmer()
            }
        } else {
            binding.loadingSkeleton.children.forEach {
                it.clearAnimation()
            }
            binding.loadingSkeleton.visibility = View.GONE
        }
    }

    private fun showConversation() {
        with(binding.viewPager) {
            isUserInputEnabled = false
            adapter = sceneAdapter

        }
    }

    companion object {
        const val EXTRA_STORY_ID = "story-id"
    }
}