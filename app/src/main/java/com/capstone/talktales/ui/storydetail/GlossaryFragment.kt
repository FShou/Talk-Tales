package com.capstone.talktales.ui.storydetail

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.talktales.R
import com.capstone.talktales.data.model.Story
import com.capstone.talktales.databinding.FragmentGlossaryBinding
import com.google.android.material.divider.MaterialDividerItemDecoration


class GlossaryFragment : Fragment() {

    private var _binding: FragmentGlossaryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<StoryDetailViewModel>()


    private lateinit var story: Story


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            story = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_STORY, Story::class.java)!!
            } else {
                it.getParcelable(ARG_STORY)!!
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGlossaryBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvGlossarium.apply {
            adapter = GlossaryAdapter(story.listGlossary)
            layoutManager = LinearLayoutManager(this.context)
            addItemDecoration(
                MaterialDividerItemDecoration(
                    this.context,
                    LinearLayoutManager.VERTICAL
                ).apply {
                    isLastItemDecorated = false
                    dividerColor = ContextCompat.getColor(requireContext(), R.color.gray_50)
                })
        }

        binding.btnNext.setOnClickListener {
            val synopsisFragment = SynopsisFragment.newInstance(story.synopsis)
            parentFragmentManager.beginTransaction()
                .replace(R.id.content, synopsisFragment)
                .addToBackStack(null)
                .commit()
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.setPageTitle("Glossary")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        private const val ARG_STORY = "story"

        @JvmStatic
        fun newInstance(story: Story) =
            GlossaryFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_STORY, story)
                }
            }

    }

}