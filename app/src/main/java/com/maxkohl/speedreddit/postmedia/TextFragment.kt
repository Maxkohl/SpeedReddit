package com.maxkohl.speedreddit.postmedia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxkohl.speedreddit.data.RedditPost
import com.maxkohl.speedreddit.databinding.FragmentTextBinding

class TextFragment : Fragment() {
    lateinit var redditPost: RedditPost

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
        val binding = FragmentTextBinding.inflate(inflater)
        binding.redditPost = redditPost

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(redditPost: RedditPost) =
            TextFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("redditPost", redditPost)
                }
            }
    }
}