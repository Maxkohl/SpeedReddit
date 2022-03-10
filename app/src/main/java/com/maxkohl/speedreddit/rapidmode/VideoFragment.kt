package com.maxkohl.speedreddit.rapidmode

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.MediaItem.fromUri
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util
import com.maxkohl.speedreddit.data.RedditPost
import com.maxkohl.speedreddit.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {

    lateinit var redditPost: RedditPost
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var binding: FragmentVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            redditPost = it.getSerializable("redditPost") as RedditPost
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        simpleExoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = simpleExoPlayer

        when (redditPost.mediaType) {
            "rich:video" -> prepareRichVideo()
            else -> prepareRedditVideo()
        }

        simpleExoPlayer.prepare()
        simpleExoPlayer.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()

        simpleExoPlayer.pause()
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
        //release player when done
        simpleExoPlayer.release()
//        simpleExoPlayer = null
    }


    private fun prepareRedditVideo() {

        val url = redditPost.media?.redditVideo?.videoSrc
        val videoMediaItem = url?.let { fromUri(it) }
        val audioMediaItem = url?.let { fromUri(getVideoAudioUri(it)) }

        val dataSourceFactory: DataSource.Factory =
            DefaultHttpDataSource.Factory()
        val videoSource: ProgressiveMediaSource? = videoMediaItem?.let {
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(it)
        }
        val audioSource: ProgressiveMediaSource? = audioMediaItem?.let {
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(it)
        }

        if (videoSource != null && audioSource != null) {
            val mergeSource: MediaSource = MergingMediaSource(videoSource, audioSource)
            simpleExoPlayer.addMediaSource(mergeSource)
        }

        simpleExoPlayer.addListener(object : Player.EventListener {
            override fun onPlayerErrorChanged(error: PlaybackException?) {
                super.onPlayerErrorChanged(error)
                if (videoSource != null) {
                    //TODO Find better way to use only videoSource if audioSource returns bad network call
                    simpleExoPlayer.removeMediaItem(0)
                    simpleExoPlayer.setMediaSource(videoSource)
                    simpleExoPlayer.prepare()

                }
            }
        })
    }

    private fun getVideoAudioUri(url: String): String {
        val reducedUrl = url.split("_".toRegex())[0]

        return "${reducedUrl}_audio.mp4"
    }

    private fun prepareRichVideo() {
        val uri = redditPost.media?.richVideo?.videoSrc
        val formattedUri = uri?.let { formatRichVideo(it) }
        val mediaItem = formattedUri?.let { fromUri(it) }
        if (mediaItem != null) {
            simpleExoPlayer.addMediaItem(mediaItem)
        }
    }

    private fun formatRichVideo(url: String): String {
        val reducedUrl = url.split("-".toRegex())[0]

        return "${reducedUrl}-mobile.mp4"
    }

    companion object {
        @JvmStatic
        fun newInstance(redditPost: RedditPost) =
            VideoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("redditPost", redditPost)
                }
            }
    }

}