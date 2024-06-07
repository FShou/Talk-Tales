package com.capstone.talktales.ui.storydetail


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.talktales.R
import com.capstone.talktales.databinding.FragmentGlossaryBinding
import com.google.android.material.divider.MaterialDividerItemDecoration


class GlossaryFragment : Fragment() {

    private var _binding: FragmentGlossaryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<StoryDetailViewModel>()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGlossaryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.story.observe(viewLifecycleOwner) { story ->
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
                val synopsisFragment = SynopsisFragment.newInstance()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.content, synopsisFragment)
                    .addToBackStack(null)
                    .commit()
            }
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
        @JvmStatic
        fun newInstance() = GlossaryFragment()

    }

}