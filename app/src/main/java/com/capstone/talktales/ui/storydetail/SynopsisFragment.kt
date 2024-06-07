package com.capstone.talktales.ui.storydetail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.capstone.talktales.R
import com.capstone.talktales.databinding.FragmentSynopsisBinding
import com.capstone.talktales.ui.conversation.ConversationActivity

class SynopsisFragment : Fragment() {

    private var _binding: FragmentSynopsisBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<StoryDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSynopsisBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.story.observe(viewLifecycleOwner) { story ->
            with(binding) {
                tvSynopsis.text = story.synopsis
                btnNext.setOnClickListener {
                    startActivity(
                        Intent(requireActivity(), ConversationActivity::class.java)
                            .putExtra(ConversationActivity.EXTRA_STORY_ID, story.id)
                    )
                }
            }
        }


    }

    override fun onResume() {
        super.onResume()
        viewModel.setPageTitle(getString(R.string.synopsis))

    }

    companion object {

        @JvmStatic
        fun newInstance() = SynopsisFragment()

    }
}