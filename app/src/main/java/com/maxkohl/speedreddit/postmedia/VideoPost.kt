package com.maxkohl.speedreddit.postmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.maxkohl.speedreddit.databinding.FragmentVideoPostBinding
import com.maxkohl.speedreddit.home.HomeViewModel

class VideoPost : Fragment() {

    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var player: SimpleExoPlayer
    private lateinit var binding: FragmentVideoPostBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoPostBinding.inflate(inflater)
        binding.lifecycleOwner = this

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = viewModel.redditPost.value?.title

        val simpleExoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = simpleExoPlayer

        val mediaUri = viewModel.redditPost.value?.media?.redditVideo?.videoSrc

        val mediaItem = mediaUri?.let { MediaItem.fromUri(it) }
        if (mediaItem != null) {
            simpleExoPlayer.addMediaItem(mediaItem)
        }
        simpleExoPlayer.prepare()
        simpleExoPlayer.playWhenReady = true
    }

}