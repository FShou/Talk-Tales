package com.capstone.talktales.ui.conversation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.talktales.R
import com.capstone.talktales.data.model.Conversation
import com.capstone.talktales.data.model.Scene
import com.capstone.talktales.data.remote.response.ConversationResponse
import com.capstone.talktales.data.remote.response.PredictionData
import com.capstone.talktales.data.remote.response.ResponseResult
import com.capstone.talktales.databinding.ActivityConversationBinding
import com.capstone.talktales.factory.ConversationViemModelFactory
import com.capstone.talktales.ui.home.HomeActivity
import com.capstone.talktales.ui.utils.setCurrentItemWithSmoothScroll
import com.capstone.talktales.ui.utils.startShimmer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ConversationActivity : AppCompatActivity() {

    private val storyId by lazy { intent.getStringExtra(EXTRA_STORY_ID).toString() }
    private val viewModel by viewModels<ConversationViewModel> {
        ConversationViemModelFactory.getInstance(
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
            Toast.makeText(this, "Please, accept the permission", Toast.LENGTH_LONG).show()
        }
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        this, REQUIRED_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED

    private val exoPlayer by lazy { ExoPlayer.Builder(this).build() }

    private lateinit var conversation: List<Conversation>

    private lateinit var sceneAdapter: RecyclerView.Adapter<*>

    private val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(binding.bottomSheet)
    }


    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN



        viewModel.feedback.observe(this) { handleFeedback(it) }
        viewModel.page.observe(this) { binding.viewPager.setCurrentItemWithSmoothScroll(it, 100) }

        exoPlayer.apply {
            addMediaItem(MediaItem.fromUri("android.resource://" + packageName + "/" + R.raw.correct))
            addMediaItem(MediaItem.fromUri("android.resource://" + packageName + "/" + R.raw.incorrect))
            pauseAtEndOfMediaItems = true
            prepare()
        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showBackAlert()
            }
        })

        binding.btnBack.setOnClickListener { showBackAlert() }

        if (!allPermissionsGranted()) {
            requestPermission.launch(Manifest.permission.RECORD_AUDIO)
            return
        }

        viewModel.getConversation(storyId).observe(this) { handleConversationResponse(it) }

    }

    private fun showBackAlert() {
        MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setTitle(getString(R.string.are_you_sure))
            .setMessage(getString(R.string.this_will_not_be_saved))
            .setPositiveButton(getString(R.string.continu)) { _, _ -> startIntentHome() }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun startIntentHome() {
        startActivity(Intent(this,HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    private fun handleFeedback(predictionData: PredictionData?) {
        binding.btnFeedbackAction.isEnabled = true
        when {
            predictionData == null -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                return
            }

            predictionData.feedback == "Correct" -> {
                exoPlayer.apply {
                    seekTo(0, 0)
                    play()
                }
                binding.btnFeedbackAction.setOnClickListener {
                    viewModel.nextPage()
                    viewModel.setFeedback(null)

                    it.isEnabled = false
                }
                binding.btnFeedbackAction.text = resources.getString(R.string.next)
                binding.tvFeedback.setTextColor(
                    ContextCompat.getColor(
                        this@ConversationActivity, R.color.green
                    )
                )
            }

            predictionData.feedback == "Incorrect" -> {
                exoPlayer.apply {
                    seekTo(1, 0)
                    play()
                }
                binding.btnFeedbackAction.setOnClickListener {
                    viewModel.setFeedback(null)
                    it.isEnabled = false
                }
                binding.btnFeedbackAction.text = resources.getString(R.string.retry)
                binding.tvFeedback.setTextColor(
                    ContextCompat.getColor(
                        this@ConversationActivity, R.color.red
                    )
                )
            }
        }
        binding.apply {
            tvFeedback.text = predictionData!!.feedback
            tvTarget.text = Html.fromHtml(predictionData.html) // TODO: Check More
        }

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun handleConversationResponse(result: ResponseResult<ConversationResponse>) {
        when (result) {
            is ResponseResult.Error -> {
                showLoading(false)
                showError(true, result.msg)
            }

            is ResponseResult.Loading -> {
                showError(false)
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

    private fun showError(isError: Boolean, message: String = "") {
        with(binding) {
            if (isError) {
                errorLayout.tvError.text = message
                errorLayout.btnRetry.setOnClickListener {
                    viewModel.getConversation(storyId).observe(this@ConversationActivity) {
                        handleConversationResponse(it)
                    }
                }
                errorLayout.root.visibility = View.VISIBLE

            } else errorLayout.root.visibility = View.GONE
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
                if (itemCount - 1 == position) {
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
        const val REQUIRED_PERMISSION = Manifest.permission.RECORD_AUDIO
    }
}