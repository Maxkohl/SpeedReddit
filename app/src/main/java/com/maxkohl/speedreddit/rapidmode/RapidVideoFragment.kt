package com.maxkohl.speedreddit.rapidmode

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import com.maxkohl.speedreddit.R
import com.maxkohl.speedreddit.data.RedditPost
import com.maxkohl.speedreddit.databinding.FragmentRapidImageBinding
import com.maxkohl.speedreddit.databinding.FragmentVideoPostBinding

class RapidVideoFragment : Fragment() {

    lateinit var redditPost: RedditPost
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var binding: FragmentVideoPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getSerializable("redditPost")?.let {
            redditPost = it as RedditPost
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoPostBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        simpleExoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = simpleExoPlayer

        val mediaUri = redditPost.media?.redditVideo?.videoSrc

        val mediaItem = mediaUri?.let { MediaItem.fromUri(it) }
        if (mediaItem != null) {
            simpleExoPlayer.addMediaItem(mediaItem)
        }
        simpleExoPlayer.prepare()
        simpleExoPlayer.playWhenReady = true
    }

    override fun onResume() {
        super.onResume()

        simpleExoPlayer.seekTo(0)
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        if (simpleExoPlayer == null) {
            return
        }
        //release player when done
        simpleExoPlayer!!.release()
//        simpleExoPlayer = null
    }

    companion object {
        fun newInstance(redditPost: RedditPost): RapidVideoFragment {
            val args = Bundle()
            args.putSerializable("redditPost", redditPost)
            val fragment = RapidVideoFragment()

            fragment.arguments = args
            return fragment
        }
    }

}