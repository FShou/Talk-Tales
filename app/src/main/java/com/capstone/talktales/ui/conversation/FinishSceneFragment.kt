package com.capstone.talktales.ui.conversation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.talktales.R
import com.capstone.talktales.databinding.FragmentConversationSceneBinding
import com.capstone.talktales.databinding.FragmentFinishSceneBinding
import com.capstone.talktales.ui.home.HomeActivity


class FinishSceneFragment : Fragment() {
    private var _binding: FragmentFinishSceneBinding? = null
    private val binding get() = _binding!!

    private var animationDone = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFinishSceneBinding.inflate(inflater, container, false)

        binding.btnBack.setOnClickListener {
            activity?.startActivity(Intent(requireActivity(), HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        if (!animationDone) {
            binding.rewardAnimation.playAnimation()
        }
        animationDone = true
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            FinishSceneFragment()
    }
}