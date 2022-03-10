package com.maxkohl.speedreddit.rapidmode

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxkohl.speedreddit.R
import com.maxkohl.speedreddit.data.RedditPost
import com.maxkohl.speedreddit.databinding.FragmentRapidTextBinding

class RapidTextFragment : Fragment() {
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
        val binding = FragmentRapidTextBinding.inflate(inflater)
        binding.redditPost = redditPost

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(redditPost: RedditPost) =
            RapidTextFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("redditPost", redditPost)
                }
            }
    }
}