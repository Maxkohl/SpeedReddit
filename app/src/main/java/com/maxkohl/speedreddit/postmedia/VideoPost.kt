package com.maxkohl.speedreddit.postmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import com.maxkohl.speedreddit.data.RedditPost
import com.maxkohl.speedreddit.databinding.FragmentVideoPostBinding
import com.maxkohl.speedreddit.home.HomeViewModel


class VideoPost : Fragment() {

    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var binding: FragmentVideoPostBinding
    private lateinit var redditPost: RedditPost


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoPostBinding.inflate(inflater)
        binding.lifecycleOwner = this
        redditPost = viewModel.redditPost.value!!

        // Inflate the layout for this fragment
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
        val videoMediaItem = url?.let { MediaItem.fromUri(it) }
        val audioMediaItem = url?.let { MediaItem.fromUri(getVideoAudioUri(it)) }

        val videoDataSourceFactory: DataSource.Factory =
            DefaultHttpDataSource.Factory()
        val videoSource: ProgressiveMediaSource? = videoMediaItem?.let {
            ProgressiveMediaSource.Factory(videoDataSourceFactory)
                .createMediaSource(it)
        }

        val audioSource: ProgressiveMediaSource? = audioMediaItem?.let {
            ProgressiveMediaSource.Factory(videoDataSourceFactory)
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
        val mediaItem = formattedUri?.let { MediaItem.fromUri(it) }
        if (mediaItem != null) {
            simpleExoPlayer.addMediaItem(mediaItem)
        }
    }

    private fun formatRichVideo(url: String): String {
        val reducedUrl = url.split("-".toRegex())[0]

        return "${reducedUrl}-mobile.mp4"
    }
}