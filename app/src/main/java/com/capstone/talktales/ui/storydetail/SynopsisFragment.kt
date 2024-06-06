package com.capstone.talktales.ui.storydetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.capstone.talktales.R
import com.capstone.talktales.databinding.FragmentGlossaryBinding
import com.capstone.talktales.databinding.FragmentSynopsisBinding

class SynopsisFragment : Fragment() {

    private var _binding: FragmentSynopsisBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<StoryDetailViewModel>()

    private lateinit var synopsis: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            synopsis = it.getString(ARG_SYNOPSYS)!!
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSynopsisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tvSynopsis.text = synopsis
            btnNext.setOnClickListener {
                // Todo: Intent Conversation
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.setPageTitle("Synopsis")
    }

    companion object {

        private const val ARG_SYNOPSYS = "synopsis-arg"

        @JvmStatic
        fun newInstance(synopsis: String) =
            SynopsisFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SYNOPSYS, synopsis)
                }
            }
    }
}