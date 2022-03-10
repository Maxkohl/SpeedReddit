package com.maxkohl.speedreddit.postmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.maxkohl.speedreddit.data.RedditPost
import com.maxkohl.speedreddit.databinding.FragmentLinkBinding

class LinkFragment : Fragment() {
    lateinit var redditPost: RedditPost

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
        val binding = FragmentLinkBinding.inflate(inflater, container, false)
        binding.redditPost = redditPost
        binding.postImageview.setOnClickListener {
            val defaultBrowser = Intent.makeMainSelectorActivity(
                Intent.ACTION_MAIN,
                Intent.CATEGORY_APP_BROWSER
            )
            defaultBrowser.data = Uri.parse(redditPost.contentUrl)
            startActivity(defaultBrowser)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(redditPost: RedditPost) =
            LinkFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("redditPost", redditPost)
                }
            }
    }
}