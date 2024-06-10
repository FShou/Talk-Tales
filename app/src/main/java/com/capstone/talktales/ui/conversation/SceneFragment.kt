package com.capstone.talktales.ui.conversation

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import coil.load
import com.capstone.talktales.data.model.Conversation
import com.capstone.talktales.databinding.FragmentSceneBinding


class SceneFragment : Fragment() {
    private var conversationScene: List<Conversation>? = null
    private val viewModel by activityViewModels<ConversationViewModel>()

    private val conversation1 get() = conversationScene!![0]
    private val conversation2 get() = conversationScene!![1]

    private var _binding: FragmentSceneBinding? = null
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSceneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showConversation1()
        showProlog1()
        showProlog2()
        showConversation2()

        if (conversation1.isSpeechByUser) {
            changeConversation1Layout()
        }

        if (conversation2.isSpeechByUser) {
            changeConversation2Layout()
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
        }    }

    private fun showConversation2() {
        if (!conversation2.convText.isNullOrBlank()) {
            binding.imgChar2.visibility = View.VISIBLE
            binding.tvChar2Name.visibility = View.VISIBLE
            binding.tvChar2Convo.visibility = View.VISIBLE

            binding.imgChar2.load(conversation2.characterImg)
            binding.tvChar2Name.text = conversation2.characterName
            binding.tvChar2Convo.text = conversation2.convText
            // Todo: Load Audio
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
            // Todo: Load Audio
        } else {
            binding.imgChar1.visibility = View.GONE
            binding.tvChar1Name.visibility = View.GONE
            binding.tvChar1Convo.visibility = View.GONE
            binding.convoBubble1.visibility = View.GONE
        }

    }


    companion object {

        private const val ARG_CONVERSATION_SCENE = "convo-scene"


        @JvmStatic
        fun newInstance(conversationScene: List<Conversation>) =
            SceneFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(
                        ARG_CONVERSATION_SCENE,
                        conversationScene as ArrayList<Conversation>
                    )
                }
            }
    }
}