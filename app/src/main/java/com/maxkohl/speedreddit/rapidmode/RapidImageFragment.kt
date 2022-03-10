package com.maxkohl.speedreddit.rapidmode

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.adapters.ImageViewBindingAdapter
import com.maxkohl.speedreddit.R
import com.maxkohl.speedreddit.data.RedditPost
import com.maxkohl.speedreddit.databinding.ActivityMainBinding.inflate
import com.maxkohl.speedreddit.databinding.FragmentRapidImageBinding


class RapidImageFragment : Fragment() {

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
        val binding = FragmentRapidImageBinding.inflate(inflater, container, false)
        binding.redditPost = redditPost

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(redditPost: RedditPost) =
            RapidImageFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("redditPost", redditPost)
                }
            }
    }
}