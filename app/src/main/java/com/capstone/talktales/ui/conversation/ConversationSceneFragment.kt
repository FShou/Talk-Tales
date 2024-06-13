package com.capstone.talktales.ui.conversation


import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import coil.load
import com.capstone.talktales.R
import com.capstone.talktales.data.model.Conversation
import com.capstone.talktales.data.remote.response.CheckAudioResponse
import com.capstone.talktales.data.remote.response.ResponseResult
import com.capstone.talktales.databinding.FragmentConversationSceneBinding
import com.capstone.talktales.ui.utils.dpToPx
import com.github.squti.androidwaverecorder.RecorderState
import com.github.squti.androidwaverecorder.WaveRecorder
import com.google.android.material.shape.ShapeAppearanceModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class ConversationSceneFragment : Fragment() {
    private var conversationScene: List<Conversation>? = null
    private val viewModel by activityViewModels<ConversationViewModel>()

    private val conversation1 by lazy { conversationScene!![0] }
    private val conversation2 by lazy { conversationScene!![1] }

    private val bubbleRoundSize by lazy { dpToPx(requireActivity(), 32) }

    private val storyLogId by lazy { viewModel.getStoryLogId() }
    private val audioPrefix by lazy { "${storyLogId}_${conversation2.id}" }
    private val filePath by lazy { context?.externalCacheDir?.absolutePath + "/${audioPrefix}_audio.wav" }

    private val exoPlayer by lazy { ExoPlayer.Builder(requireContext()).build() }
    private val exoPlayer2 by lazy { ExoPlayer.Builder(requireContext()).build() }

    private val waveRecorder by lazy { WaveRecorder(filePath) }

    private var _binding: FragmentConversationSceneBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            conversationScene = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableArrayList(ARG_CONVERSATION_SCENE, Conversation::class.java)
            } else {
                it.getParcelableArrayList(ARG_CONVERSATION_SCENE)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConversationSceneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showConversation1()
        showProlog1()
        showProlog2()
        showConversation2()
        prepareAudioPlayback()
        prepareAudioRecord()

        if (conversation1.isSpeechByUser) {
            changeConversation1Layout()
        }
        if (conversation1.isPostLog) {
            changeProlog1Layout()
        }
        if (conversation2.isPostLog) {
            changeProlog2Layout()
        }
        if (conversation2.isSpeechByUser) {
            changeConversation2Layout()
        }

        if (conversation1.isSpeechByUser or conversation2.isSpeechByUser) {
            binding.btnSend.setOnClickListener {
                sendUserAudio()
                exoPlayer2.pause()
                exoPlayer.pause()
            }
        }

        if (!conversation1.isSpeechByUser and !conversation2.isSpeechByUser) {
            binding.btnSend.text = "Next"
            binding.btnSend.setOnClickListener {
                viewModel.nextPage()
                exoPlayer2.pause()
                exoPlayer.pause()
            }
            binding.btnRecord.visibility = View.GONE
        }


        binding.btnRecord.setOnClickListener { waveRecorder.startRecording() }

    }

    private fun changeProlog1Layout() {
        binding.convoBubble1.updateLayoutParams<ConstraintLayout.LayoutParams> {
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            topToBottom = ConstraintLayout.LayoutParams.UNSET
        }

        binding.tvProlog1.updateLayoutParams<ConstraintLayout.LayoutParams> {
            topToTop = ConstraintLayout.LayoutParams.UNSET
            topToBottom = binding.convoBubble1.id
        }

    }

    private fun changeProlog2Layout() {
        binding.convoBubble2.updateLayoutParams<ConstraintLayout.LayoutParams> {
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            topToBottom = ConstraintLayout.LayoutParams.UNSET
        }

        binding.tvProlog2.updateLayoutParams<ConstraintLayout.LayoutParams> {
            topToTop = ConstraintLayout.LayoutParams.UNSET
            topToBottom = binding.convoBubble2.id
        }

    }

    private fun sendUserAudio() {
        val userAudio = File(filePath)
        if (!userAudio.exists()) {
            Toast.makeText(context, "Record first", Toast.LENGTH_SHORT).show()
            return
        }
        val storyConvId = if (conversation1.isSpeechByUser) conversation1.id else conversation2.id
        val requestFile = userAudio.asRequestBody("audio/wav".toMediaTypeOrNull())
        val multipart = MultipartBody.Part.createFormData("user_voice", userAudio.name, requestFile)
        viewModel.predictUserAudio(storyLogId, storyConvId!!, multipart)
            .observe(viewLifecycleOwner) {
                handlePredictResponse(it)
            }
    }

    private fun handlePredictResponse(result: ResponseResult<CheckAudioResponse>) {
        when (result) {
            is ResponseResult.Error -> {
                showLoading(false)
            }

            is ResponseResult.Loading -> {
                showLoading(true)
            }

            is ResponseResult.Success -> {
                showLoading(false)
                viewModel.setFeedback(result.data.data!!)
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.root.alpha = 0.3f
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.root.alpha = 1f
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun prepareAudioRecord() {
        val prevFile = File(filePath)
        if (prevFile.exists()) {
            prevFile.delete()
        }
        waveRecorder.apply {
            noiseSuppressorActive = true
            onStateChangeListener = { handleRecorderStateChange(it) }
        }

    }

    private fun handleRecorderStateChange(recorderState: RecorderState) {
        when (recorderState) {
            RecorderState.RECORDING -> {
                binding.btnRecord.icon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_mic_off)
                binding.btnRecord.setOnClickListener { waveRecorder.stopRecording() }
                binding.btnSend.isEnabled = false
            }

            RecorderState.PAUSE -> {}
            RecorderState.STOP -> {
                binding.btnRecord.icon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_mic_on)
                binding.btnRecord.setOnClickListener { waveRecorder.startRecording() }
                binding.btnSend.isEnabled = true
            }
        }
    }


    private fun prepareAudioPlayback() {
        if (!conversation1.voiceUrl.isNullOrBlank()) {
            exoPlayer.apply {
                setMediaItem(MediaItem.fromUri(conversation1.voiceUrl.toString()))
                repeatMode = Player.REPEAT_MODE_ONE
                prepare()
            }
            with(binding.btnPlay) {
                visibility = View.VISIBLE
                setOnClickListener {
                    exoPlayer2.pause()
                    binding.btnPlay2.icon =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_audio_on)
                    if (exoPlayer.isPlaying) {
                        icon =
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_audio_on)
                        exoPlayer.pause()

                    } else {
                        icon =
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_audio_off)

                        exoPlayer.apply {
                            seekTo(0)
                            play()
                        }
                    }

                }
            }
        }
        if (!conversation2.voiceUrl.isNullOrBlank()) {
            exoPlayer2.apply {
                setMediaItem(MediaItem.fromUri(conversation2.voiceUrl.toString()))
                repeatMode = Player.REPEAT_MODE_ONE
                prepare()
            }
            with(binding.btnPlay2) {
                visibility = View.VISIBLE
                setOnClickListener {
                    exoPlayer.pause()
                    binding.btnPlay.icon =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_audio_on)
                    if (exoPlayer2.isPlaying) {
                        icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_audio_on)
                        exoPlayer2.pause()

                    } else {
                        icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_audio_off)
                        exoPlayer2.apply {
                            seekTo(0)
                            play()
                        }                    }
                }
            }
        }
    }

    private fun changeConversation2Layout() {
        binding.imgChar2.updateLayoutParams<ConstraintLayout.LayoutParams> {
            startToStart = ConstraintLayout.LayoutParams.UNSET
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        }
        binding.convoBubble2.updateLayoutParams<ConstraintLayout.LayoutParams> {
            endToEnd = ConstraintLayout.LayoutParams.UNSET
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToStart = binding.imgChar2.id
        }
        binding.convoBubble2.shapeAppearanceModel =
            ShapeAppearanceModel.builder().setTopLeftCornerSize(0f).setBottomRightCornerSize(0f)
                .setTopRightCornerSize(bubbleRoundSize).setBottomLeftCornerSize(bubbleRoundSize)
                .build()
    }

    private fun changeConversation1Layout() {
        binding.imgChar1.updateLayoutParams<ConstraintLayout.LayoutParams> {
            startToStart = ConstraintLayout.LayoutParams.UNSET
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        }
        binding.convoBubble1.updateLayoutParams<ConstraintLayout.LayoutParams> {
            endToEnd = ConstraintLayout.LayoutParams.UNSET
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToStart = binding.imgChar1.id
        }

        binding.convoBubble1.shapeAppearanceModel =
            ShapeAppearanceModel.builder().setTopLeftCornerSize(0f).setBottomRightCornerSize(0f)
                .setTopRightCornerSize(bubbleRoundSize).setBottomLeftCornerSize(bubbleRoundSize)
                .build()
    }

    private fun showConversation2() {
        if (!conversation2.convText.isNullOrBlank()) {
            binding.imgChar2.visibility = View.VISIBLE
            binding.tvChar2Name.visibility = View.VISIBLE
            binding.tvChar2Convo.visibility = View.VISIBLE

            binding.imgChar2.load(conversation2.characterImg)
            binding.tvChar2Name.text = conversation2.characterName
            binding.tvChar2Convo.text = conversation2.convText
        } else {
            binding.imgChar2.visibility = View.GONE
            binding.tvChar2Name.visibility = View.GONE
            binding.tvChar2Convo.visibility = View.GONE
            binding.convoBubble2.visibility = View.GONE
        }
    }

    private fun showProlog1() {
        if (!conversation1.prologText.isNullOrBlank()) {
            binding.tvProlog1.visibility = View.VISIBLE
            binding.tvProlog1.text = conversation1.prologText
        }
    }

    private fun showProlog2() {
        if (!conversation2.prologText.isNullOrBlank()) {
            binding.tvProlog2.visibility = View.VISIBLE
            binding.tvProlog2.text = conversation2.prologText
        }
    }

    private fun showConversation1() {
        if (!conversation1.convText.isNullOrBlank()) {
            binding.imgChar1.visibility = View.VISIBLE
            binding.tvChar1Name.visibility = View.VISIBLE
            binding.tvChar1Convo.visibility = View.VISIBLE

            binding.imgChar1.load(conversation1.characterImg)
            binding.tvChar1Name.text = conversation1.characterName
            binding.tvChar1Convo.text = conversation1.convText
        } else {
            binding.imgChar1.visibility = View.GONE
            binding.tvChar1Name.visibility = View.GONE
            binding.tvChar1Convo.visibility = View.GONE
            binding.convoBubble1.visibility = View.GONE
        }

    }

    override fun onPause() {
        super.onPause()
        exoPlayer.pause()
        exoPlayer2.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        exoPlayer.release()
        exoPlayer2.release()
    }


    companion object {

        private const val ARG_CONVERSATION_SCENE = "convo-scene"


        @JvmStatic
        fun newInstance(conversationScene: List<Conversation>) = ConversationSceneFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(
                    ARG_CONVERSATION_SCENE, conversationScene as ArrayList<Conversation>
                )
            }
        }
    }
}