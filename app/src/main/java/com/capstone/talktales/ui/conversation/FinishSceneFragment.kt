package com.capstone.talktales.ui.conversation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.capstone.talktales.R
import com.capstone.talktales.databinding.FragmentFinishSceneBinding
import com.capstone.talktales.ui.home.HomeActivity
import com.capstone.talktales.ui.utils.zoomInFromZero


class FinishSceneFragment : Fragment() {
    private var _binding: FragmentFinishSceneBinding? = null
    private val binding get() = _binding!!

    private var animationDone = false
    private val exoPlayer by lazy { ExoPlayer.Builder(requireContext()).build() }


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
        binding.rewardAnimation.addAnimatorUpdateListener {
            val progress = (it.animatedValue as Float * 100).toInt()
            if ( progress == 30) {
                binding.tvFinished.apply {
                    visibility = View.VISIBLE
                    zoomInFromZero(1000L)
                    exoPlayer.play()
                }
            }
        }

        exoPlayer.apply {
            setMediaItem(MediaItem.fromUri("android.resource://" + activity?.packageName + "/" + R.raw.finished))
            prepare()
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

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FinishSceneFragment()
    }
}