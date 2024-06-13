package com.capstone.talktales.ui.conversation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import coil.load
import com.capstone.talktales.R
import com.capstone.talktales.data.model.Conversation
import com.capstone.talktales.databinding.FragmentConversationSceneBinding
import com.capstone.talktales.databinding.FragmentMidSceneBinding

class MidSceneFragment : Fragment() {
    private var conversation: Conversation? = null
    private var _binding: FragmentMidSceneBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<ConversationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            conversation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_CONVERSATION, Conversation::class.java)
            } else {
                it.getParcelable(ARG_CONVERSATION)
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMidSceneBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvProlog.text = conversation!!.prologText
        binding.btnNext.setOnClickListener {
            viewModel.nextPage()
            it.isEnabled = false
        }
        binding.storyBanner.load(conversation!!.characterImg)
    }

    companion object {
        const val ARG_CONVERSATION = "conversation"
        @JvmStatic
        fun newInstance(conversation: Conversation) =
            MidSceneFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CONVERSATION, conversation)
                }
            }
    }
}